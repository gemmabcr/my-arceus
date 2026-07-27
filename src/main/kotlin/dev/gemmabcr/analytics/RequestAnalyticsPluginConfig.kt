package dev.gemmabcr.analytics

import io.ktor.server.application.ApplicationCall

class RequestAnalyticsPluginConfig {
    lateinit var repository: RequestAnalyticsRepository
    var retentionDays: Long = DEFAULT_RETENTION_DAYS
    var userId: (ApplicationCall) -> Int? = { null }
}

private const val DEFAULT_RETENTION_DAYS = 30L
