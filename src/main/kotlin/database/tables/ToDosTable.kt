package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table

object ToDosTable : Table("to_dos") {
    val id = integer("id")
    val description = text("description")
    override val primaryKey = PrimaryKey(id)
}