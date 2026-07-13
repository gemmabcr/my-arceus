package dev.gemmabcr.views

import dev.gemmabcr.models.Language
import dev.gemmabcr.models.Session
import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.views.i18n.I18n
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import java.util.Locale

interface View {
    fun create(application: Application)

    suspend fun ApplicationCall.createSession(sessionTokenService: SessionTokenService): Session =
        sessionTokenService.session(this)
}

fun ApplicationCall.applyLocale() {
    val cookieLanguage = Language.fromTag(request.cookies[LANGUAGE_COOKIE])
    val acceptedRanges = request.headers[HttpHeaders.AcceptLanguage]
        ?.let { runCatching { Locale.LanguageRange.parse(it) }.getOrDefault(emptyList()) }
        .orEmpty()
    val acceptedLanguage = acceptedRanges.firstNotNullOfOrNull { Language.fromTag(it.range) }
    val language = cookieLanguage ?: acceptedLanguage ?: Language.EN
    I18n.setLocale(Locale.forLanguageTag(language.tag))
}

const val LANGUAGE_COOKIE = "lang"
