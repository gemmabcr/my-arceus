package dev.gemmabcr.security

import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.models.Session
import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

class SessionTokenService(private val authDao: AuthDao) {
    suspend fun create(user: Int): String {
        val token = generateToken()
        authDao.saveSessionTokenHash(user, hash(token))
        return token
    }

    suspend fun session(call: ApplicationCall): Session {
        val token = call.sessionToken() ?: return Session()
        return Session(authDao.userBySessionTokenHash(hash(token)))
    }

    suspend fun clear(call: ApplicationCall) {
        val token = call.sessionToken() ?: return
        authDao.clearSessionTokenHash(hash(token))
    }

    private fun ApplicationCall.sessionToken(): String? =
        request.authorizationBearerToken()
            ?: request.headers[SESSION_TOKEN_HEADER]
            ?: request.cookies[SESSION_TOKEN_COOKIE]

    private fun io.ktor.server.request.ApplicationRequest.authorizationBearerToken(): String? {
        val authorization = headers[HttpHeaders.Authorization] ?: return null
        return authorization
            .takeIf { it.startsWith(BEARER_PREFIX, ignoreCase = true) }
            ?.substring(BEARER_PREFIX.length)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    private fun generateToken(): String {
        val bytes = ByteArray(TOKEN_BYTES)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun hash(token: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(token.toByteArray(Charsets.UTF_8))
            .joinToString(separator = "") { "%02x".format(it) }

    companion object {
        const val SESSION_TOKEN_COOKIE = "sessionToken"
        const val SESSION_TOKEN_HEADER = "X-Session-Token"
        private const val BEARER_PREFIX = "Bearer "
        private const val TOKEN_BYTES = 32
        private val secureRandom = SecureRandom()
    }
}
