package dev.gemmabcr.views

import dev.gemmabcr.models.Session
import dev.gemmabcr.security.SessionTokenService
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall

interface View {
    fun create(application: Application)

    suspend fun ApplicationCall.createSession(sessionTokenService: SessionTokenService): Session =
        sessionTokenService.session(this)
}
