package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.Table

object UserTeamsTable : Table("user_team") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val pokemonId = integer("pokemon_id")
    override val primaryKey = PrimaryKey(id)
}
