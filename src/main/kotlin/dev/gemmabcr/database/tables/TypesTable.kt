package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table

object TypesTable : Table("types") {
    val id = integer("id")
    val name = varchar("name", NAME_LENGTH)
    override val primaryKey = PrimaryKey(id)
}

private const val NAME_LENGTH = 20
