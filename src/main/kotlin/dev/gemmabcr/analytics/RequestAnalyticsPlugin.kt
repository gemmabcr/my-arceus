package dev.gemmabcr.analytics

import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.CallSetup
import io.ktor.server.application.hooks.ResponseSent
import io.ktor.server.plugins.origin
import io.ktor.server.request.host
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.util.AttributeKey
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.net.URI
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

val RequestAnalyticsPlugin = createApplicationPlugin(
    name = "RequestAnalytics",
    createConfiguration = ::RequestAnalyticsPluginConfig,
) {
    val repository = pluginConfig.repository
    val retentionDays = pluginConfig.retentionDays
    val resolveUserId = pluginConfig.userId
    val exclude = pluginConfig.exclude
    val lastCleanup = AtomicLong(0)

    require(retentionDays > 0) { "Analytics retention must be greater than zero days." }

    on(CallSetup) { call ->
        call.attributes.put(RequestStartedAtKey, System.nanoTime())
    }

    on(ResponseSent) { call ->
        if (exclude(call)) return@on
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val event = call.toAnalyticsEvent(now, resolveUserId(call))
        application.launch {
            runCatching { repository.record(event) }
                .onFailure { logger.warn("Could not persist request analytics.", it) }
            cleanupExpiredRecords(repository, retentionDays, now, lastCleanup)
        }
    }
}

private fun ApplicationCall.toAnalyticsEvent(
    now: OffsetDateTime,
    userId: Int?,
): RequestAnalyticsEvent = RequestAnalyticsEvent(
    requestId = UUID.randomUUID(),
    occurredAt = now,
    method = request.httpMethod.value.take(MAX_METHOD_LENGTH),
    path = request.path().take(MAX_PATH_LENGTH),
    queryKeys = request.queryParameters.names()
        .filterNot(SENSITIVE_QUERY_KEYS::contains)
        .sorted()
        .joinToString(",")
        .take(MAX_QUERY_KEYS_LENGTH)
        .ifBlank { null },
    statusCode = response.status()?.value ?: DEFAULT_STATUS_CODE,
    durationMillis = ((System.nanoTime() - attributes[RequestStartedAtKey]) / NANOS_PER_MILLISECOND).coerceAtLeast(0),
    ipAddress = request.origin.remoteHost.take(MAX_IP_ADDRESS_LENGTH),
    userId = userId,
    scheme = request.origin.scheme.take(MAX_SCHEME_LENGTH),
    host = request.host().take(MAX_HOST_LENGTH),
    userAgent = request.headers[HttpHeaders.UserAgent].limitedTo(MAX_USER_AGENT_LENGTH),
    referrer = request.headers[HttpHeaders.Referrer].sanitizedReferrer(),
    acceptLanguage = request.headers[HttpHeaders.AcceptLanguage].limitedTo(MAX_ACCEPT_LANGUAGE_LENGTH),
    requestContentType = request.headers[HttpHeaders.ContentType].limitedTo(MAX_CONTENT_TYPE_LENGTH),
    responseContentType = response.headers[HttpHeaders.ContentType].limitedTo(MAX_CONTENT_TYPE_LENGTH),
)

private suspend fun cleanupExpiredRecords(
    repository: RequestAnalyticsRepository,
    retentionDays: Long,
    now: OffsetDateTime,
    lastCleanup: AtomicLong,
) {
    val currentEpochDay = now.toLocalDate().toEpochDay()
    if (lastCleanup.getAndSet(currentEpochDay) == currentEpochDay) return
    runCatching { repository.deleteBefore(now.minusDays(retentionDays)) }
        .onFailure { logger.warn("Could not delete expired request analytics.", it) }
}

private fun String?.limitedTo(maxLength: Int): String? =
    this?.trim()?.take(maxLength)?.ifBlank { null }

private fun String?.sanitizedReferrer(): String? {
    return limitedTo(MAX_REFERRER_LENGTH)?.let { rawReferrer ->
        runCatching { URI(rawReferrer) }.getOrNull()?.let { uri ->
            val path = uri.rawPath?.ifBlank { "/" } ?: "/"
            val sanitized = if (uri.isAbsolute) {
                runCatching { URI(uri.scheme, uri.authority, path, null, null).toASCIIString() }.getOrNull()
            } else {
                path
            }
            sanitized.limitedTo(MAX_REFERRER_LENGTH)
        }
    }
}

private val RequestStartedAtKey = AttributeKey<Long>("RequestAnalyticsStartedAt")
private val logger = LoggerFactory.getLogger("RequestAnalytics")
private val SENSITIVE_QUERY_KEYS = setOf(
    "access_token",
    "code",
    "id_token",
    "password",
    "session",
    "state",
    "token",
)

private const val DEFAULT_STATUS_CODE = 200
private const val NANOS_PER_MILLISECOND = 1_000_000
private const val MAX_ACCEPT_LANGUAGE_LENGTH = 255
private const val MAX_CONTENT_TYPE_LENGTH = 100
private const val MAX_HOST_LENGTH = 255
private const val MAX_IP_ADDRESS_LENGTH = 45
private const val MAX_METHOD_LENGTH = 10
private const val MAX_PATH_LENGTH = 2048
private const val MAX_QUERY_KEYS_LENGTH = 512
private const val MAX_REFERRER_LENGTH = 2048
private const val MAX_SCHEME_LENGTH = 10
private const val MAX_USER_AGENT_LENGTH = 1024
