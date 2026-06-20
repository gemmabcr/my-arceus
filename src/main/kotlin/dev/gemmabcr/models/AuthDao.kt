package dev.gemmabcr.models

interface AuthDao {
    suspend fun profile(user: Int): UserProfile?

    suspend fun authenticate(email: String, password: String): Int?

    suspend fun userBySessionTokenHash(sessionTokenHash: String): Int?

    suspend fun saveSessionTokenHash(user: Int, sessionTokenHash: String)

    suspend fun clearSessionTokenHash(sessionTokenHash: String)
}
