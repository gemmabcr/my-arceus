package dev.gemmabcr.database

import com.typesafe.config.Config
import org.jetbrains.exposed.sql.Database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {
    fun init(config: Config) {
        Database.Companion.connect(
            url = config.getString("db.url"),
            driver = config.getString("db.driver"),
            user = config.getString("db.user"),
            password = config.getString("db.password")
        )
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}