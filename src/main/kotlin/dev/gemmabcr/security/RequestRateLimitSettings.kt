package dev.gemmabcr.security

import com.typesafe.config.Config
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import kotlin.time.Duration.Companion.seconds

data class RequestRateLimitSettings(
    val enabled: Boolean,
    val requests: Int,
    val periodSeconds: Int,
) {
    init {
        require(requests > 0) { "Rate limit requests must be greater than zero." }
        require(periodSeconds > 0) { "Rate limit period must be greater than zero seconds." }
    }

    companion object {
        fun from(config: Config) = RequestRateLimitSettings(
            enabled = config.getBoolean("security.rateLimit.enabled"),
            requests = config.getInt("security.rateLimit.requests"),
            periodSeconds = config.getInt("security.rateLimit.periodSeconds"),
        )
    }
}

fun Application.installRequestRateLimit(settings: RequestRateLimitSettings) {
    if (!settings.enabled) return

    install(RateLimit) {
        global {
            rateLimiter(
                limit = settings.requests,
                refillPeriod = settings.periodSeconds.seconds,
            )
            requestKey { call -> call.request.origin.remoteHost }
        }
    }
}
