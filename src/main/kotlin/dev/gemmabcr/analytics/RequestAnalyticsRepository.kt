package dev.gemmabcr.analytics

import java.time.OffsetDateTime

interface RequestAnalyticsRepository {
    suspend fun record(event: RequestAnalyticsEvent)

    suspend fun deleteBefore(cutoff: OffsetDateTime)
}
