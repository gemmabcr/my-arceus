package dev.gemmabcr

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationProxyTest {
    @Test
    fun givenForwardedHttpsRequest_whenSettingSecureCookie_thenCookieIsAccepted() = testApplication {
        environment {
            config = MapApplicationConfig()
        }
        application {
            installReverseProxySupport()
            routing {
                get("/secure-cookie") {
                    call.response.cookies.append(
                        name = "sessionToken",
                        value = "test-token",
                        secure = true,
                    )
                    call.respondText("ok")
                }
            }
        }

        val response = client.get("/secure-cookie") {
            header("X-Forwarded-Proto", "https")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.headers.getAll(HttpHeaders.SetCookie).orEmpty().any { "Secure" in it })
    }
}
