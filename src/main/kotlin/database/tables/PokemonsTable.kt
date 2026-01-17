package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.IntegerColumnType

object PokemonsTable : Table("pokemons") {
    val id = integer("id")
    val locations = array("locations", IntegerColumnType())
    val toDos = array("to_dos", IntegerColumnType())
    override val primaryKey = PrimaryKey(id)
}
