package dev.gemmabcr.views

import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.views.pages.PrivacyPage
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class PrivacyView(private val sessionTokenService: SessionTokenService) : View {
    override fun create(application: Application) {
        application.routing {
            get("/privacy") {
                val session = call.createSession(sessionTokenService)
                call.applyLocale()
                call.respondHtmlTemplate(PrivacyPage(session)) {}
            }
        }
    }
}
