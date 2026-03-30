package dev.gemmabcr.database.tables

import dev.gemmabcr.Serialization.jsonConfig
import dev.gemmabcr.models.pokemons.CaughtCondition
import dev.gemmabcr.models.pokemons.Type
import org.jetbrains.exposed.sql.EnumerationColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.IntegerColumnType

object PokemonsTable : Table("pokemons") {
    val id = integer("id")
    val generalId = integer("general_id")
    val name = varchar("name", NAME_LENGTH)
    val locations = array("locations", IntegerColumnType())
    val types = array("types", EnumerationColumnType(Type::class))
    val toDos = jsonb<Map<String, Int>>("to_dos", jsonConfig)
    val specialCondition = enumeration<CaughtCondition>("special_condition").nullable()
    override val primaryKey = PrimaryKey(id)
}

private const val NAME_LENGTH = 20
