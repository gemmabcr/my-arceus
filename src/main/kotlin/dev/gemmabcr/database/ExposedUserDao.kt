package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.UserTeamDto
import dev.gemmabcr.database.dtos.UserToDoDto
import dev.gemmabcr.database.tables.UserTeamsTable
import dev.gemmabcr.database.tables.UserToDosTable
import dev.gemmabcr.models.UserDao
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll

class ExposedUserDao : UserDao {
    override suspend fun todos(user: Int): List<UserToDoDto> = DatabaseFactory.dbQuery {
        UserToDosTable.selectAll()
            .where(UserToDosTable.userId eq user)
            .map(::userToDoDto)
    }

    private fun userToDoDto(row: ResultRow): UserToDoDto = UserToDoDto(
        userId = row[UserToDosTable.userId],
        pokemonId = row[UserToDosTable.pokemonId],
        todoId = row[UserToDosTable.todoId],
        done = row[UserToDosTable.done],
    )

    override suspend fun todos(user: Int, pokemonId: Int): List<UserToDoDto> = DatabaseFactory.dbQuery {
        UserToDosTable.selectAll()
            .where(UserToDosTable.userId eq user and (UserToDosTable.pokemonId eq pokemonId))
            .map(::userToDoDto)
    }

    override suspend fun team(user: Int): List<UserTeamDto> = DatabaseFactory.dbQuery {
        UserTeamsTable.selectAll()
            .where(UserTeamsTable.userId eq user)
            .orderBy(UserTeamsTable.id)
            .map { row ->
                UserTeamDto(
                    userId = row[UserTeamsTable.userId],
                    pokemonId = row[UserTeamsTable.pokemonId],
                )
            }
    }

    override suspend fun addPokemonToTeam(user: Int, pokemonId: Int): Unit = DatabaseFactory.dbQuery {
        UserTeamsTable.insertIgnore {
            it[userId] = user
            it[this.pokemonId] = pokemonId
        }
    }

    override suspend fun removePokemonFromTeam(user: Int, pokemonId: Int): Unit = DatabaseFactory.dbQuery {
        UserTeamsTable.deleteWhere {
            (UserTeamsTable.userId eq user) and (UserTeamsTable.pokemonId eq pokemonId)
        }
    }
}
