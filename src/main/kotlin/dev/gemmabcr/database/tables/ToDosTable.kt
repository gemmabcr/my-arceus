package dev.gemmabcr.database.tables

import dev.gemmabcr.Serialization.jsonConfig
import dev.gemmabcr.models.pokemons.todo.ToDoType
import dev.gemmabcr.models.pokemons.todo.ToDoCondition
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb

object ToDosTable : Table("to_dos") {
    val id = integer("id")
    val description = jsonb<ToDoType<out ToDoCondition>>("description", jsonConfig)
    override val primaryKey = PrimaryKey(id)
}
