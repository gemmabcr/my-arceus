package dev.gemmabcr.views

import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.models.LoginRequest
import dev.gemmabcr.models.LoginResponse
import dev.gemmabcr.security.OAuthIdentity
import dev.gemmabcr.security.OAuthProvider
import dev.gemmabcr.security.OAuthService
import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.security.SessionTokenService.Companion.SESSION_TOKEN_COOKIE
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.pages.AuthMode
import dev.gemmabcr.views.pages.LoginPage
import dev.gemmabcr.views.pages.ProfilePage
import io.ktor.http.ContentType
import io.ktor.http.CookieEncoding
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
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

@Suppress("TooManyFunctions")
class LoginView(
    private val authDao: AuthDao,
    private val sessionTokenService: SessionTokenService,
    private val oauthService: OAuthService,
) : View {
    override fun create(application: Application) {
        application.routing {
            loginRoutes()
            registerRoutes()
            oauthRoutes()
            logoutRoutes()
            profileRoutes()
        }
    }

    private fun Route.loginRoutes() {
        route("/login") {
            get {
                if (call.redirectAuthenticatedUser()) return@get
                call.respondAuthPage(AuthMode.LOGIN)
            }
            post {
                val request = call.loginRequest()
                val user = authDao.authenticate(request.email, request.password)
                if (user == null) {
                    call.respondUnauthorized()
                    return@post
                }
                call.respondAuthenticated(user)
            }
        }
    }

    private fun Route.registerRoutes() {
        route("/register") {
            get {
                if (call.redirectAuthenticatedUser()) return@get
                call.respondAuthPage(AuthMode.REGISTER)
            }
            post {
                val parameters = call.receiveParameters()
                val email = parameters["email"].orEmpty().trim()
                val password = parameters["password"].orEmpty()
                val confirmation = parameters["confirmPassword"].orEmpty()
                val error = when {
                    email.length > MAXIMUM_EMAIL_LENGTH || !EMAIL_REGEX.matches(email) -> CommonI18nKey.INVALID_EMAIL
                    password.length !in MINIMUM_PASSWORD_LENGTH..MAXIMUM_PASSWORD_LENGTH ->
                        CommonI18nKey.PASSWORD_REQUIREMENTS
                    password != confirmation -> CommonI18nKey.PASSWORD_MISMATCH
                    else -> null
                }
                if (error != null) {
                    call.respondAuthPage(AuthMode.REGISTER, error)
                    return@post
                }
                val user = authDao.register(email, password)
                if (user == null) {
                    call.respondAuthPage(AuthMode.REGISTER, CommonI18nKey.ACCOUNT_EXISTS)
                    return@post
                }
                call.respondAuthenticated(user)
            }
        }
    }

    private fun Route.oauthRoutes() {
        route("/auth/google") {
            get { call.startOAuth(OAuthProvider.GOOGLE) }
        }
        route("/auth/google/callback") {
            get {
                call.finishOAuth(
                    provider = OAuthProvider.GOOGLE,
                    code = call.request.queryParameters["code"],
                    state = call.request.queryParameters["state"],
                )
            }
        }
        route("/auth/apple") {
            get { call.startOAuth(OAuthProvider.APPLE) }
        }
        route("/auth/apple/callback") {
            post {
                val parameters = call.receiveParameters()
                call.finishOAuth(
                    provider = OAuthProvider.APPLE,
                    code = parameters["code"],
                    state = parameters["state"],
                )
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

    private suspend fun ApplicationCall.startOAuth(provider: OAuthProvider) {
        if (!oauthService.isEnabled(provider)) {
            respondAuthPage(AuthMode.LOGIN, CommonI18nKey.OAUTH_NOT_CONFIGURED)
            return
        }
        respondRedirect(oauthService.authorizationUrl(provider))
    }

    private suspend fun ApplicationCall.finishOAuth(provider: OAuthProvider, code: String?, state: String?) {
        if (code.isNullOrBlank() || state.isNullOrBlank()) {
            respondAuthPage(AuthMode.LOGIN, CommonI18nKey.OAUTH_ERROR)
            return
        }
        val identity = runCatching {
            when (provider) {
                OAuthProvider.APPLE -> oauthService.authenticateApple(code, state)
                OAuthProvider.GOOGLE -> oauthService.authenticateGoogle(code, state)
            }
        }.getOrElse {
            logger.warn("OAuth login failed for ${provider.id}", it)
            respondAuthPage(AuthMode.LOGIN, CommonI18nKey.OAUTH_ERROR)
            return
        }
        respondAuthenticated(identity.userId())
    }

    private suspend fun OAuthIdentity.userId(): Int =
        authDao.findOrCreateOAuthUser(provider.id, subject, email)

    private suspend fun ApplicationCall.respondAuthPage(mode: AuthMode, error: I18nKey? = null) {
        applyLocale()
        respondHtmlTemplate(
            LoginPage(
                mode = mode,
                error = error,
                googleEnabled = oauthService.isEnabled(OAuthProvider.GOOGLE),
                appleEnabled = oauthService.isEnabled(OAuthProvider.APPLE),
            )
        ) {}
    }

    private suspend fun ApplicationCall.redirectAuthenticatedUser(): Boolean {
        if (createSession(sessionTokenService).user == null) return false
        respondRedirect("/profile")
        return true
    }

    private fun ApplicationCall.setSessionCookie(token: String) {
        response.cookies.append(
            name = SESSION_TOKEN_COOKIE,
            value = token,
            path = "/",
            httpOnly = true,
            secure = oauthService.secureCookies,
            extensions = mapOf("SameSite" to "Lax"),
            encoding = CookieEncoding.URI_ENCODING,
        )
    }

    private fun ApplicationCall.clearSessionCookie() {
        response.cookies.append(
            name = SESSION_TOKEN_COOKIE,
            value = "",
            path = "/",
            maxAge = 0L,
            httpOnly = true,
        )
    }

    private suspend fun ApplicationCall.respondAuthenticated(user: Int) {
        val token = sessionTokenService.create(user)
        setSessionCookie(token)
        if (request.contentType().match(ContentType.Application.Json)) {
            respond(LoginResponse(token))
        } else {
            respondRedirect("/pokemons")
        }
    }

    private suspend fun ApplicationCall.loginRequest(): LoginRequest =
        if (request.contentType().match(ContentType.Application.Json)) {
            receive()
        } else {
            val parameters = receiveParameters()
            LoginRequest(
                email = parameters["email"].orEmpty(),
                password = parameters["password"].orEmpty(),
            )
        }

    private suspend fun ApplicationCall.respondUnauthorized() {
        if (request.contentType().match(ContentType.Application.Json)) {
            respond(HttpStatusCode.Unauthorized)
        } else {
            respondAuthPage(AuthMode.LOGIN, CommonI18nKey.INVALID_CREDENTIALS)
        }
    }

    private companion object {
        val logger = org.slf4j.LoggerFactory.getLogger(LoginView::class.java)
        val EMAIL_REGEX = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        const val MAXIMUM_EMAIL_LENGTH = 50
        const val MAXIMUM_PASSWORD_LENGTH = 128
        const val MINIMUM_PASSWORD_LENGTH = 8
    }
}
