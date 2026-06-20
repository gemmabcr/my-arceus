package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = integer("id")
    val email = varchar("email", EMAIL_LENGTH)
    val password = varchar("password", PASSWORD_LENGTH)
    val sessionTokenHash = varchar("session_token_hash", SESSION_TOKEN_HASH_LENGTH).nullable()

    override val primaryKey = PrimaryKey(id)
}

private const val EMAIL_LENGTH = 50
private const val PASSWORD_LENGTH = 50
private const val SESSION_TOKEN_HASH_LENGTH = 64
