package dev.gemmabcr.security

import dev.gemmabcr.installReverseProxySupport
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestRateLimitTest {
    @Test
    fun givenLimitReached_whenRequestComesFromSameIp_thenRejectsOnlyThatIp() = testApplication {
        environment { config = MapApplicationConfig() }
        application {
            installReverseProxySupport()
            installRequestRateLimit(
                RequestRateLimitSettings(
                    enabled = true,
                    requests = 1,
                    periodSeconds = 60,
                )
            )
            routing {
                get("/") { call.respondText("ok") }
            }
        }

        val firstResponse = client.get("/") { header("X-Forwarded-For", FIRST_IP) }
        val limitedResponse = client.get("/") { header("X-Forwarded-For", FIRST_IP) }
        val otherIpResponse = client.get("/") { header("X-Forwarded-For", SECOND_IP) }

        assertEquals(HttpStatusCode.OK, firstResponse.status)
        assertEquals(HttpStatusCode.TooManyRequests, limitedResponse.status)
        assertEquals(HttpStatusCode.OK, otherIpResponse.status)
    }
}

private const val FIRST_IP = "203.0.113.10"
private const val SECOND_IP = "203.0.113.11"
