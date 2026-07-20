package dev.gemmabcr.security

import dev.gemmabcr.Serialization.jsonConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigInteger
import java.security.KeyFactory
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.RSAPublicKeySpec
import java.time.Instant
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap

@Suppress("TooManyFunctions")
class OAuthService(private val config: OAuthConfig) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(jsonConfig) }
    }
    private val states = ConcurrentHashMap<String, OAuthState>()
    val secureCookies = config.publicBaseUrl.startsWith("https://")

    fun authorizationUrl(provider: OAuthProvider): String {
        require(isEnabled(provider)) { "OAuth provider is not configured" }
        removeExpiredStates()
        val state = secureToken()
        val nonce = secureToken()
        states[state] = OAuthState(provider, nonce, Instant.now().plusSeconds(STATE_LIFETIME_SECONDS))

        val builder = URLBuilder(provider.authorizationEndpoint)
        builder.parameters.apply {
            append("client_id", provider.clientId(config))
            append("redirect_uri", redirectUri(provider))
            append("response_type", "code")
            append("scope", if (provider == OAuthProvider.GOOGLE) "openid email" else "name email")
            append("state", state)
            append("nonce", nonce)
            if (provider == OAuthProvider.GOOGLE) {
                append("prompt", "select_account")
            } else {
                append("response_mode", "form_post")
            }
        }
        return builder.buildString()
    }

    fun isEnabled(provider: OAuthProvider): Boolean = when (provider) {
        OAuthProvider.APPLE -> config.appleEnabled
        OAuthProvider.GOOGLE -> config.googleEnabled
    }

    suspend fun authenticateGoogle(code: String, state: String): OAuthIdentity {
        val oauthState = consumeState(state, OAuthProvider.GOOGLE)
        val token: OAuthTokenResponse = client.submitForm(
            url = OAuthProvider.GOOGLE.tokenEndpoint,
            formParameters = Parameters.build {
                append("client_id", OAuthProvider.GOOGLE.clientId(config))
                append("client_secret", config.googleClientSecret.orEmpty())
                append("code", code)
                append("grant_type", "authorization_code")
                append("redirect_uri", redirectUri(OAuthProvider.GOOGLE))
            },
        ).body()
        val profile: GoogleProfile = client.get(GOOGLE_USERINFO_ENDPOINT) {
            bearerAuth(token.accessToken)
        }.body()
        require(profile.emailVerified && profile.email.isNotBlank()) { "Google has not verified this email" }

        val claims = token.idToken?.let(::decodeNonceClaims)
        require(claims?.nonce == oauthState.nonce) { "Invalid OAuth nonce" }
        return OAuthIdentity(OAuthProvider.GOOGLE, profile.subject, profile.email)
    }

    suspend fun authenticateApple(code: String, state: String): OAuthIdentity {
        val oauthState = consumeState(state, OAuthProvider.APPLE)
        val token: OAuthTokenResponse = client.submitForm(
            url = OAuthProvider.APPLE.tokenEndpoint,
            formParameters = Parameters.build {
                append("client_id", OAuthProvider.APPLE.clientId(config))
                append("client_secret", config.appleClientSecret.orEmpty())
                append("code", code)
                append("grant_type", "authorization_code")
                append("redirect_uri", redirectUri(OAuthProvider.APPLE))
            },
        ).body()
        val idToken = token.idToken ?: error("Apple did not return an identity token")
        val claims = verifyAppleToken(idToken)
        require(claims.nonce == oauthState.nonce) { "Invalid OAuth nonce" }
        require(claims.emailVerified == "true" && !claims.email.isNullOrBlank()) { "Apple has not verified this email" }
        return OAuthIdentity(OAuthProvider.APPLE, claims.subject, claims.email)
    }

    private fun consumeState(state: String, provider: OAuthProvider): OAuthState {
        val stored = states.remove(state) ?: error("Invalid or expired OAuth state")
        require(stored.provider == provider && stored.expiresAt.isAfter(Instant.now())) {
            "Invalid or expired OAuth state"
        }
        return stored
    }

    private fun decodeClaims(token: String): IdentityClaims {
        val parts = token.split('.')
        require(parts.size == JWT_PARTS) { "Invalid identity token" }
        return jsonConfig.decodeFromString(
            IdentityClaims.serializer(),
            String(Base64.getUrlDecoder().decode(parts[1])),
        )
    }

    private fun decodeNonceClaims(token: String): NonceClaims {
        val parts = token.split('.')
        require(parts.size == JWT_PARTS) { "Invalid identity token" }
        return jsonConfig.decodeFromString(
            NonceClaims.serializer(),
            String(Base64.getUrlDecoder().decode(parts[1])),
        )
    }

    private suspend fun verifyAppleToken(token: String): IdentityClaims {
        val parts = token.split('.')
        require(parts.size == JWT_PARTS) { "Invalid Apple identity token" }
        val header = jsonConfig.decodeFromString(
            JwtHeader.serializer(),
            String(Base64.getUrlDecoder().decode(parts[0])),
        )
        require(header.algorithm == "RS256") { "Unexpected Apple token algorithm" }
        val jwks: JwkSet = client.get(APPLE_JWKS_ENDPOINT).body()
        val jwk = jwks.keys.singleOrNull { it.keyId == header.keyId && it.algorithm == header.algorithm }
            ?: error("Apple signing key was not found")
        val publicKey = KeyFactory.getInstance("RSA").generatePublic(
            RSAPublicKeySpec(
                BigInteger(1, Base64.getUrlDecoder().decode(jwk.modulus)),
                BigInteger(1, Base64.getUrlDecoder().decode(jwk.exponent)),
            )
        )
        val verified = Signature.getInstance("SHA256withRSA").run {
            initVerify(publicKey)
            update("${parts[0]}.${parts[1]}".toByteArray())
            verify(Base64.getUrlDecoder().decode(parts[2]))
        }
        require(verified) { "Invalid Apple token signature" }

        val claims = decodeClaims(token)
        require(claims.issuer == APPLE_ISSUER) { "Invalid Apple token issuer" }
        require(claims.audience == config.appleClientId) { "Invalid Apple token audience" }
        require(claims.expiresAt > Instant.now().epochSecond) { "Expired Apple identity token" }
        return claims
    }

    private fun redirectUri(provider: OAuthProvider): String =
        "${config.publicBaseUrl.trimEnd('/')}/auth/${provider.id}/callback"

    private fun removeExpiredStates() {
        val now = Instant.now()
        states.entries.removeIf { it.value.expiresAt.isBefore(now) }
    }

    private fun secureToken(): String = ByteArray(STATE_BYTES)
        .also(SecureRandom()::nextBytes)
        .let(Base64.getUrlEncoder().withoutPadding()::encodeToString)
}

enum class OAuthProvider(
    val id: String,
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
) {
    APPLE("apple", "https://appleid.apple.com/auth/authorize", "https://appleid.apple.com/auth/token"),
    GOOGLE("google", "https://accounts.google.com/o/oauth2/v2/auth", "https://oauth2.googleapis.com/token");

    fun clientId(config: OAuthConfig): String = when (this) {
        APPLE -> config.appleClientId
        GOOGLE -> config.googleClientId
    }.orEmpty()
}

data class OAuthIdentity(val provider: OAuthProvider, val subject: String, val email: String)

private data class OAuthState(val provider: OAuthProvider, val nonce: String, val expiresAt: Instant)

@Serializable
private data class OAuthTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("id_token") val idToken: String? = null,
)

@Serializable
private data class GoogleProfile(
    @SerialName("sub") val subject: String,
    val email: String,
    @SerialName("email_verified") val emailVerified: Boolean,
)

@Serializable
private data class IdentityClaims(
    @SerialName("sub") val subject: String,
    @SerialName("iss") val issuer: String? = null,
    @SerialName("aud") val audience: String? = null,
    @SerialName("exp") val expiresAt: Long = 0,
    val nonce: String? = null,
    val email: String? = null,
    @SerialName("email_verified") val emailVerified: String? = null,
)

@Serializable
private data class NonceClaims(val nonce: String? = null)

@Serializable
private data class JwtHeader(
    @SerialName("alg") val algorithm: String,
    @SerialName("kid") val keyId: String,
)

@Serializable
private data class JwkSet(val keys: List<Jwk>)

@Serializable
private data class Jwk(
    @SerialName("kid") val keyId: String,
    @SerialName("alg") val algorithm: String,
    @SerialName("n") val modulus: String,
    @SerialName("e") val exponent: String,
)

private const val APPLE_ISSUER = "https://appleid.apple.com"
private const val APPLE_JWKS_ENDPOINT = "https://appleid.apple.com/auth/keys"
private const val GOOGLE_USERINFO_ENDPOINT = "https://openidconnect.googleapis.com/v1/userinfo"
private const val JWT_PARTS = 3
private const val STATE_BYTES = 32
private const val STATE_LIFETIME_SECONDS = 600L
