package dev.gemmabcr

import com.typesafe.config.ConfigFactory
import dev.gemmabcr.database.FlywayFactory
import dev.gemmabcr.database.ExposedDao
import dev.gemmabcr.database.DatabaseFactory
import dev.gemmabcr.views.PageFactory
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.CallSetup
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import dev.gemmabcr.views.i18n.I18n
import java.util.Locale

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val config = ConfigFactory.load()
    FlywayFactory.migrate(config)
    DatabaseFactory.init(config)

    install(createApplicationPlugin(name = "I18nPlugin") {
        on(CallSetup) { call ->
            val langCookie = call.request.cookies["lang"]
            val acceptLanguage = call.request.headers[HttpHeaders.AcceptLanguage]
            
            val locale = when {
                langCookie != null -> Locale.forLanguageTag(langCookie)
                acceptLanguage != null -> Locale.forLanguageTag(acceptLanguage.split(",").first().trim())
                else -> Locale.ENGLISH
            }
            I18n.setLocale(locale)
        }
    })

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }
    val exposedDao = ExposedDao()
    PageFactory(exposedDao).create(this)
}
