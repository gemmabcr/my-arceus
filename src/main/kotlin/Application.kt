package dev.gemmabcr

import com.typesafe.config.ConfigFactory
import dev.gemmabcr.api.PokemonApi
import dev.gemmabcr.database.FlywayFactory
import dev.gemmabcr.database.ExposedDao
import dev.gemmabcr.database.DatabaseFactory
import dev.gemmabcr.views.PageFactory
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val config = ConfigFactory.load()
    FlywayFactory.migrate(config)
    DatabaseFactory.init(config)

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }
    val pokeApi = PokemonApi()
    val exposedDao = ExposedDao()
    PageFactory(pokeApi, exposedDao).create(this)
}
