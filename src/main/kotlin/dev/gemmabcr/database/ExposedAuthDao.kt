package dev.gemmabcr.database

import dev.gemmabcr.database.tables.UsersTable
import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.models.UserProfile
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
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
        UsersTable.selectAll()
            .where((UsersTable.email eq email) and (UsersTable.password eq password))
            .singleOrNull()
            ?.get(UsersTable.id)
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
