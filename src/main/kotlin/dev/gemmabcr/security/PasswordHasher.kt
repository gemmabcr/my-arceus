package dev.gemmabcr.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val HASH_BYTES = 32
    private const val ITERATIONS = 600_000
    private const val PREFIX = "pbkdf2_sha256"
    private const val SALT_BYTES = 16

    fun hash(password: String): String {
        val salt = ByteArray(SALT_BYTES).also(SecureRandom()::nextBytes)
        val hash = derive(password, salt, ITERATIONS)
        return listOf(
            PREFIX,
            ITERATIONS.toString(),
            Base64.getEncoder().encodeToString(salt),
            Base64.getEncoder().encodeToString(hash),
        ).joinToString("$")
    }

    fun verify(password: String, storedPassword: String): Boolean {
        val parts = storedPassword.split('$')
        if (parts.size != HASH_PARTS || parts.first() != PREFIX) {
            return MessageDigest.isEqual(password.toByteArray(), storedPassword.toByteArray())
        }

        return runCatching {
            val iterations = parts[1].toInt()
            val salt = Base64.getDecoder().decode(parts[2])
            val expected = Base64.getDecoder().decode(parts[HASH_INDEX])
            MessageDigest.isEqual(derive(password, salt, iterations), expected)
        }.getOrDefault(false)
    }

    fun needsUpgrade(storedPassword: String): Boolean = !storedPassword.startsWith("$PREFIX$")

    private fun derive(password: String, salt: ByteArray, iterations: Int): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, HASH_BYTES * Byte.SIZE_BITS)
        return try {
            SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private const val HASH_INDEX = 3
    private const val HASH_PARTS = 4
}
