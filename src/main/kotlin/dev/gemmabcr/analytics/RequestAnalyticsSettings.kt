package dev.gemmabcr.analytics

import com.typesafe.config.Config

data class RequestAnalyticsSettings(
    val enabled: Boolean,
    val retentionDays: Long,
) {
    init {
        require(retentionDays > 0) { "Analytics retention must be greater than zero days." }
    }

    companion object {
        fun from(config: Config) = RequestAnalyticsSettings(
            enabled = config.getBoolean("analytics.enabled"),
            retentionDays = config.getLong("analytics.retentionDays"),
        )
    }
}
