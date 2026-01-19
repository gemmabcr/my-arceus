package dev.gemmabcr.database.tables

import dev.gemmabcr.models.Area
import org.jetbrains.exposed.sql.Table

object LocationsTable : Table("locations") {
    val id = integer("id")
    val name = varchar("name", 30)
    val area = enumeration<Area>("area")
    override val primaryKey = PrimaryKey(id)
}
