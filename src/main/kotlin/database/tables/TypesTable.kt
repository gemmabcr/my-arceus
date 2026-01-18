package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table

object TypesTable : Table("types") {
    val id = integer("id")
    val name = varchar("name", 20)
    override val primaryKey = PrimaryKey(id)
}
