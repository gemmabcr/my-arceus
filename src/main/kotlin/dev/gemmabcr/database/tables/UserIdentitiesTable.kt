package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table

object UserIdentitiesTable : Table("user_identities") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UsersTable.id)
    val provider = varchar("provider", PROVIDER_LENGTH)
    val providerSubject = varchar("provider_subject", PROVIDER_SUBJECT_LENGTH)

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(provider, providerSubject)
    }
}

private const val PROVIDER_LENGTH = 20
private const val PROVIDER_SUBJECT_LENGTH = 255
