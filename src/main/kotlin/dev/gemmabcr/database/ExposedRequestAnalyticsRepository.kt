package dev.gemmabcr.database

import dev.gemmabcr.analytics.RequestAnalyticsEvent
import dev.gemmabcr.analytics.RequestAnalyticsRepository
import dev.gemmabcr.database.tables.RequestAnalyticsTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import java.time.OffsetDateTime

class ExposedRequestAnalyticsRepository : RequestAnalyticsRepository {
    override suspend fun record(event: RequestAnalyticsEvent): Unit = DatabaseFactory.dbQuery {
        RequestAnalyticsTable.insert {
            it[requestId] = event.requestId
            it[occurredAt] = event.occurredAt
            it[method] = event.method
            it[path] = event.path
            it[queryKeys] = event.queryKeys
            it[statusCode] = event.statusCode.toShort()
            it[durationMillis] = event.durationMillis
            it[ipAddress] = event.ipAddress
            it[userId] = event.userId
            it[scheme] = event.scheme
            it[host] = event.host
            it[userAgent] = event.userAgent
            it[referrer] = event.referrer
            it[acceptLanguage] = event.acceptLanguage
            it[requestContentType] = event.requestContentType
            it[responseContentType] = event.responseContentType
        }
    }

    override suspend fun deleteBefore(cutoff: OffsetDateTime): Unit = DatabaseFactory.dbQuery {
        RequestAnalyticsTable.deleteWhere { occurredAt less cutoff }
    }
}
