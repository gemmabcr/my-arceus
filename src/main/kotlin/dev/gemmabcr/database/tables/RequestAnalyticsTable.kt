package dev.gemmabcr.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object RequestAnalyticsTable : Table("request_analytics") {
    val id = long("id").autoIncrement()
    val requestId = uuid("request_id").uniqueIndex()
    val occurredAt = timestampWithTimeZone("occurred_at")
    val method = varchar("method", METHOD_LENGTH)
    val path = varchar("path", PATH_LENGTH)
    val queryKeys = varchar("query_keys", QUERY_KEYS_LENGTH).nullable()
    val statusCode = short("status_code")
    val durationMillis = long("duration_ms")
    val ipAddress = varchar("ip_address", IP_ADDRESS_LENGTH)
    val userId = integer("user_id").references(UsersTable.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val scheme = varchar("scheme", SCHEME_LENGTH)
    val host = varchar("host", HOST_LENGTH)
    val userAgent = varchar("user_agent", USER_AGENT_LENGTH).nullable()
    val referrer = varchar("referrer", REFERRER_LENGTH).nullable()
    val acceptLanguage = varchar("accept_language", ACCEPT_LANGUAGE_LENGTH).nullable()
    val requestContentType = varchar("request_content_type", CONTENT_TYPE_LENGTH).nullable()
    val responseContentType = varchar("response_content_type", CONTENT_TYPE_LENGTH).nullable()

    override val primaryKey = PrimaryKey(id)
}

private const val ACCEPT_LANGUAGE_LENGTH = 255
private const val CONTENT_TYPE_LENGTH = 100
private const val HOST_LENGTH = 255
private const val IP_ADDRESS_LENGTH = 45
private const val METHOD_LENGTH = 10
private const val PATH_LENGTH = 2048
private const val QUERY_KEYS_LENGTH = 512
private const val REFERRER_LENGTH = 2048
private const val SCHEME_LENGTH = 10
private const val USER_AGENT_LENGTH = 1024
