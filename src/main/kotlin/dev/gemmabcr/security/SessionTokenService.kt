package dev.gemmabcr.security

import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.models.Session
import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.util.AttributeKey
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
        val cachedSession = call.attributes.getOrNull(SessionKey)
        val resolvedSession = cachedSession ?: call.sessionToken()
            ?.let { Session(authDao.userBySessionTokenHash(hash(it))) }
            ?: Session()
        if (cachedSession == null) call.rememberSession(resolvedSession)
        return resolvedSession
    }

    fun rememberUser(call: ApplicationCall, user: Int) = call.rememberSession(Session(user))

    fun cachedUser(call: ApplicationCall): Int? = call.attributes.getOrNull(SessionKey)?.user

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

    private fun ApplicationCall.rememberSession(session: Session) {
        attributes.put(SessionKey, session)
    }

    companion object {
        const val SESSION_TOKEN_COOKIE = "sessionToken"
        const val SESSION_TOKEN_HEADER = "X-Session-Token"
        private const val BEARER_PREFIX = "Bearer "
        private const val TOKEN_BYTES = 32
        private val SessionKey = AttributeKey<Session>("UserSession")
        private val secureRandom = SecureRandom()
    }
}
