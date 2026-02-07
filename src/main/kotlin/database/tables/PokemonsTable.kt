package dev.gemmabcr.database.tables

import dev.gemmabcr.models.Type
import dev.gemmabcr.models.SpecialCondition
import org.jetbrains.exposed.sql.EnumerationColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.IntegerColumnType

object PokemonsTable : Table("pokemons") {
    val id = integer("id")
    val generalId = integer("general_id")
    val name = varchar("name", 20)
    val locations = array("locations", IntegerColumnType())
    val types = array("types", EnumerationColumnType(Type::class))
    val toDos = jsonb<Map<String, Int>>("to_dos", Json.Default)
    val specialCondition = enumeration<SpecialCondition>("special_condition").nullable()
    override val primaryKey = PrimaryKey(id)
}
