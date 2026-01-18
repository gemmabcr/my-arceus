package dev.gemmabcr.database.tables

import dev.gemmabcr.models.Type
import org.jetbrains.exposed.sql.EnumerationColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.IntegerColumnType

object PokemonsTable : Table("pokemons") {
    val id = integer("id")
    val name = varchar("name", 20)
    val locations = array("locations", IntegerColumnType())
    val types = array("types", EnumerationColumnType(Type::class))
    val toDos = array("to_dos", IntegerColumnType())
    override val primaryKey = PrimaryKey(id)
}
