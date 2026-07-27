package database

import dev.gemmabcr.analytics.RequestAnalyticsEvent
import dev.gemmabcr.database.ExposedRequestAnalyticsRepository
import dev.gemmabcr.database.tables.RequestAnalyticsTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExposedRequestAnalyticsRepositoryTest {
    private val repository = ExposedRequestAnalyticsRepository()

    @BeforeTest
    fun setup() = TestDatabaseFactory.init()

    @AfterTest
    fun teardown() = TestDatabaseFactory.wipe()

    @Test
    fun givenRequestAnalytics_whenRecordingAndDeletingExpired_thenOnlyRecentRecordsRemain() = runBlocking {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        repository.record(event(now.minusDays(31), "/old"))
        repository.record(event(now, "/pokemons"))

        repository.deleteBefore(now.minusDays(30))

        val storedPaths = transaction {
            RequestAnalyticsTable.selectAll().map { it[RequestAnalyticsTable.path] }
        }
        assertEquals(listOf("/pokemons"), storedPaths)
    }

    private fun event(occurredAt: OffsetDateTime, path: String) = RequestAnalyticsEvent(
        requestId = UUID.randomUUID(),
        occurredAt = occurredAt,
        method = "GET",
        path = path,
        queryKeys = "area,type",
        statusCode = 200,
        durationMillis = 12,
        ipAddress = "203.0.113.10",
        userId = null,
        scheme = "https",
        host = "my-arceus.com",
        userAgent = "test-agent",
        referrer = "https://example.com/source",
        acceptLanguage = "ca",
        requestContentType = null,
        responseContentType = "text/html",
    )
}
