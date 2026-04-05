package database

import dev.gemmabcr.database.tables.LocationsTable
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.database.tables.ToDosTable
import dev.gemmabcr.database.tables.UserTeamsTable
import dev.gemmabcr.database.tables.UserToDosTable
import dev.gemmabcr.database.tables.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabaseFactory {
    fun init() {
        Database.connect("jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(UsersTable, PokemonsTable, LocationsTable, ToDosTable, UserTeamsTable, UserToDosTable)
        }
    }
    
    fun wipe() {
        transaction {
            SchemaUtils.drop(UserToDosTable, UserTeamsTable, UsersTable, PokemonsTable, LocationsTable, ToDosTable)
        }
    }
}
