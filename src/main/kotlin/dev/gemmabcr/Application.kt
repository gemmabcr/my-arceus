package dev.gemmabcr

import com.typesafe.config.ConfigFactory
import dev.gemmabcr.Serialization.jsonConfig
import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.controllers.TodoProgressService
import dev.gemmabcr.database.ExposedAuthDao
import dev.gemmabcr.database.FlywayFactory
import dev.gemmabcr.database.ExposedPokemonDao
import dev.gemmabcr.database.DatabaseFactory
import dev.gemmabcr.database.ExposedUserDao
import dev.gemmabcr.ocr.GameScreenshotOcrService
import dev.gemmabcr.ocr.OcrTodoImportService
import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.views.PageFactory
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val config = ConfigFactory.load()
    FlywayFactory.migrate(config)
    DatabaseFactory.init(config)
    val pokemonDao = ExposedPokemonDao()
    val userDao = ExposedUserDao()
    val authDao = ExposedAuthDao()
    val sessionTokenService = SessionTokenService(authDao)

    install(ContentNegotiation) {
        json(jsonConfig)
    }
    PageFactory(
        Controller(
            pokemonDao,
            userDao
        ),
        authDao,
        TodoProgressService(pokemonDao, userDao),
        GameScreenshotOcrService(),
        OcrTodoImportService(pokemonDao, userDao),
        sessionTokenService,
    ).create(this)
}
