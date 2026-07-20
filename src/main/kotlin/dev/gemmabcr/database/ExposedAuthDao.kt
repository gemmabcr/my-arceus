package dev.gemmabcr.database

import dev.gemmabcr.database.tables.UsersTable
import dev.gemmabcr.database.tables.UserIdentitiesTable
import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.models.UserProfile
import dev.gemmabcr.security.PasswordHasher
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class ExposedAuthDao : AuthDao {
    override suspend fun profile(user: Int): UserProfile? = DatabaseFactory.dbQuery {
        UsersTable.selectAll()
            .where(UsersTable.id eq user)
            .singleOrNull()
            ?.let {
                UserProfile(
                    id = it[UsersTable.id],
                    email = it[UsersTable.email],
                )
            }
    }

    override suspend fun authenticate(email: String, password: String): Int? = DatabaseFactory.dbQuery {
        val user = UsersTable.selectAll()
            .singleOrNull { it[UsersTable.email].equals(email.normalizedEmail(), ignoreCase = true) }
            ?: return@dbQuery null
        val storedPassword = user[UsersTable.password] ?: return@dbQuery null
        if (!PasswordHasher.verify(password, storedPassword)) return@dbQuery null

        if (PasswordHasher.needsUpgrade(storedPassword)) {
            UsersTable.update({ UsersTable.id eq user[UsersTable.id] }) {
                it[UsersTable.password] = PasswordHasher.hash(password)
            }
        }
        user[UsersTable.id]
    }

    override suspend fun register(email: String, password: String): Int? = DatabaseFactory.dbQuery {
        val normalizedEmail = email.normalizedEmail()
        if (UsersTable.selectAll().any { it[UsersTable.email].equals(normalizedEmail, ignoreCase = true) }) {
            return@dbQuery null
        }
        UsersTable.insert {
            it[UsersTable.email] = normalizedEmail
            it[UsersTable.password] = PasswordHasher.hash(password)
        } get UsersTable.id
    }

    override suspend fun findOrCreateOAuthUser(provider: String, subject: String, email: String): Int =
        DatabaseFactory.dbQuery {
            val identity = UserIdentitiesTable.selectAll().singleOrNull {
                it[UserIdentitiesTable.provider] == provider && it[UserIdentitiesTable.providerSubject] == subject
            }
            if (identity != null) return@dbQuery identity[UserIdentitiesTable.userId]

            val normalizedEmail = email.normalizedEmail()
            val userId = UsersTable.selectAll()
                .singleOrNull { it[UsersTable.email].equals(normalizedEmail, ignoreCase = true) }
                ?.get(UsersTable.id)
                ?: (UsersTable.insert {
                    it[UsersTable.email] = normalizedEmail
                    it[UsersTable.password] = null
                } get UsersTable.id)

            UserIdentitiesTable.insert {
                it[UserIdentitiesTable.userId] = userId
                it[UserIdentitiesTable.provider] = provider
                it[UserIdentitiesTable.providerSubject] = subject
            }
            userId
        }

    override suspend fun userBySessionTokenHash(sessionTokenHash: String): Int? = DatabaseFactory.dbQuery {
        UsersTable.selectAll()
            .where(UsersTable.sessionTokenHash eq sessionTokenHash)
            .singleOrNull()
            ?.get(UsersTable.id)
    }

    override suspend fun saveSessionTokenHash(user: Int, sessionTokenHash: String): Unit = DatabaseFactory.dbQuery {
        UsersTable.update({ UsersTable.id eq user }) {
            it[UsersTable.sessionTokenHash] = sessionTokenHash
        }
    }

    override suspend fun clearSessionTokenHash(sessionTokenHash: String): Unit = DatabaseFactory.dbQuery {
        UsersTable.update({ UsersTable.sessionTokenHash eq sessionTokenHash }) {
            it[UsersTable.sessionTokenHash] = null
        }
    }
}

private fun String.normalizedEmail(): String = trim().lowercase()
