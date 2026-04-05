package dev.gemmabcr.views

import dev.gemmabcr.models.Session
import io.ktor.server.application.Application

interface View {
    fun create(application: Application)

    fun createSession(): Session = Session(1) // TODO
}
