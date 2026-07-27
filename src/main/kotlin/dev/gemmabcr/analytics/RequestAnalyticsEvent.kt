package dev.gemmabcr.analytics

import java.time.OffsetDateTime
import java.util.UUID

data class RequestAnalyticsEvent(
    val requestId: UUID,
    val occurredAt: OffsetDateTime,
    val method: String,
    val path: String,
    val queryKeys: String?,
    val statusCode: Int,
    val durationMillis: Long,
    val ipAddress: String,
    val userId: Int?,
    val scheme: String,
    val host: String,
    val userAgent: String?,
    val referrer: String?,
    val acceptLanguage: String?,
    val requestContentType: String?,
    val responseContentType: String?,
)
