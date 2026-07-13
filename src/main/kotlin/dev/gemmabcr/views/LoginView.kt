package dev.gemmabcr.views

import dev.gemmabcr.models.LoginRequest
import dev.gemmabcr.models.LoginResponse
import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.security.SessionTokenService.Companion.SESSION_TOKEN_COOKIE
import dev.gemmabcr.views.pages.LoginPage
import dev.gemmabcr.views.pages.ProfilePage
import io.ktor.http.CookieEncoding
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.contentType
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

class LoginView(
    private val authDao: AuthDao,
    private val sessionTokenService: SessionTokenService,
) : View {
    override fun create(application: Application) {
        application.routing {
            loginRoutes()
            logoutRoutes()
            profileRoutes()
        }
    }

    private fun Route.loginRoutes() {
        route("/login") {
            get {
                val session = call.createSession(sessionTokenService)
                if (session.user != null) {
                    call.respondRedirect("/profile")
                    return@get
                }
                call.applyLocale()
                call.respondHtmlTemplate(LoginPage()) {}
            }
            post {
                val request = call.loginRequest()
                val user = authDao.authenticate(request.email, request.password)
                if (user == null) {
                    call.respondUnauthorized()
                    return@post
                }

                val token = sessionTokenService.create(user)
                call.setSessionCookie(token)
                call.respondLogin(token)
            }
        }
    }

    private fun Route.logoutRoutes() {
        route("/logout") {
            post {
                sessionTokenService.clear(call)
                call.clearSessionCookie()
                call.respondRedirect("/login")
            }
        }
    }

    private fun Route.profileRoutes() {
        route("/profile") {
            get {
                val session = call.createSession(sessionTokenService)
                val user = session.user
                if (user == null) {
                    call.respondRedirect("/login")
                    return@get
                }

                val profile = authDao.profile(user)
                if (profile == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.applyLocale()
                call.respondHtmlTemplate(ProfilePage(profile, session)) {}
            }
        }
    }

    private fun io.ktor.server.application.ApplicationCall.setSessionCookie(token: String) {
        response.cookies.append(
            name = SESSION_TOKEN_COOKIE,
            value = token,
            path = "/",
            httpOnly = true,
            extensions = mapOf("SameSite" to "Lax"),
            encoding = CookieEncoding.URI_ENCODING,
        )
    }

    private fun io.ktor.server.application.ApplicationCall.clearSessionCookie() {
        response.cookies.append(
            name = SESSION_TOKEN_COOKIE,
            value = "",
            path = "/",
            maxAge = 0L,
            httpOnly = true,
        )
    }

    private suspend fun io.ktor.server.application.ApplicationCall.respondLogin(token: String) {
        if (request.contentType().match(io.ktor.http.ContentType.Application.Json)) {
            respond(LoginResponse(token))
        } else {
            respondRedirect("/pokemons")
        }
    }

    private suspend fun io.ktor.server.application.ApplicationCall.loginRequest(): LoginRequest =
        if (request.contentType().match(io.ktor.http.ContentType.Application.Json)) {
            receive()
        } else {
            val parameters = receiveParameters()
            LoginRequest(
                email = parameters["email"].orEmpty(),
                password = parameters["password"].orEmpty(),
            )
        }

    private suspend fun io.ktor.server.application.ApplicationCall.respondUnauthorized() {
        if (request.contentType().match(io.ktor.http.ContentType.Application.Json)) {
            respond(HttpStatusCode.Unauthorized)
        } else {
            applyLocale()
            respondHtmlTemplate(LoginPage("Invalid email or password.")) {}
        }
    }
}
