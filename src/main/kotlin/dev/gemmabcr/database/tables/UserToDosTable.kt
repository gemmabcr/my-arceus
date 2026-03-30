package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table

object UserToDosTable : Table("user_todos") {
    val id = integer("id")
    val userId = integer("user_id")
    val pokemonId = integer("pokemon_id")
    val todoId = integer("todo_id")
    val done = integer("done")
    override val primaryKey = PrimaryKey(id)
}
