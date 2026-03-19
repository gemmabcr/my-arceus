package dev.gemmabcr.database.tables

import dev.gemmabcr.Serialization.jsonConfig
import dev.gemmabcr.models.pokemons.todo.ToDo
import dev.gemmabcr.models.pokemons.todo.ToDoCondition
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb

object ToDosTable : Table("to_dos") {
    val id = integer("id")
    val description = jsonb<ToDo<out ToDoCondition>>("description", jsonConfig)
    override val primaryKey = PrimaryKey(id)
}
