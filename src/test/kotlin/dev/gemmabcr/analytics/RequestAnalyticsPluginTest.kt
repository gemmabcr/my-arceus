package dev.gemmabcr.analytics

import dev.gemmabcr.installReverseProxySupport
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.request.host
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeout
import java.time.OffsetDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RequestAnalyticsPluginTest {
    @Test
    fun givenProxiedRequest_whenResponseIsSent_thenStoresSanitizedAnalytics() = testApplication {
        val repository = RecordingRepository()
        environment { config = MapApplicationConfig() }
        application {
            installReverseProxySupport()
            install(RequestAnalyticsPlugin) {
                this.repository = repository
                userId = { TEST_USER_ID }
            }
            routing {
                get("/pokemons") { call.respondText("ok") }
            }
        }

        client.get("/pokemons?type=FIRE&code=secret&area=FIELDLANDS") {
            header("X-Forwarded-For", TEST_IP)
            header("X-Forwarded-Proto", "https")
            header(HttpHeaders.UserAgent, "analytics-test-agent")
            header(HttpHeaders.Referrer, "https://example.com/source?token=secret")
            header(HttpHeaders.AcceptLanguage, "ca-ES")
        }

        val event = withTimeout(2_000) { repository.recorded.await() }
        assertEquals(TEST_IP, event.ipAddress)
        assertEquals("https", event.scheme)
        assertEquals("/pokemons", event.path)
        assertEquals("area,type", event.queryKeys)
        assertEquals(TEST_USER_ID, event.userId)
        assertEquals("https://example.com/source", event.referrer)
        assertFalse(event.referrer.orEmpty().contains("secret"))
    }

    @Test
    fun givenExcludedHost_whenResponseIsSent_thenDoesNotStoreAnalytics() = testApplication {
        val repository = RecordingRepository()
        environment { config = MapApplicationConfig() }
        application {
            install(RequestAnalyticsPlugin) {
                this.repository = repository
                exclude = { call -> call.request.host() == "localhost" }
            }
            routing {
                get("/pokemons") { call.respondText("ok") }
            }
        }

        client.get("/pokemons") {
            header(HttpHeaders.Host, "localhost")
        }

        assertFalse(repository.recorded.isCompleted)
    }
}

private class RecordingRepository : RequestAnalyticsRepository {
    val recorded = CompletableDeferred<RequestAnalyticsEvent>()

    override suspend fun record(event: RequestAnalyticsEvent) {
        recorded.complete(event)
    }

    override suspend fun deleteBefore(cutoff: OffsetDateTime) = Unit
}

private const val TEST_IP = "203.0.113.10"
private const val TEST_USER_ID = 7
