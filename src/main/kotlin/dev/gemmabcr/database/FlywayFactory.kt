package dev.gemmabcr.database

import com.typesafe.config.Config
import org.flywaydb.core.Flyway

object FlywayFactory {
    fun migrate(config: Config) {
        val url = config.getString("db.url")
        val user = config.getString("db.user")
        val password = config.getString("db.password")

        Flyway.configure()
            .dataSource(url, user, password)
            .locations("classpath:db/migration")
            .load()
            .migrate()
    }
}
