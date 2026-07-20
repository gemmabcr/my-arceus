package dev.gemmabcr.security

import com.typesafe.config.Config

data class OAuthConfig(
    val publicBaseUrl: String,
    val googleClientId: String?,
    val googleClientSecret: String?,
    val appleClientId: String?,
    val appleClientSecret: String?,
) {
    val appleEnabled: Boolean = !appleClientId.isNullOrBlank() && !appleClientSecret.isNullOrBlank()
    val googleEnabled: Boolean = !googleClientId.isNullOrBlank() && !googleClientSecret.isNullOrBlank()

    companion object {
        fun from(config: Config) = OAuthConfig(
            publicBaseUrl = config.optionalString("oauth.publicBaseUrl") ?: "http://localhost:8080",
            googleClientId = config.optionalString("oauth.google.clientId"),
            googleClientSecret = config.optionalString("oauth.google.clientSecret"),
            appleClientId = config.optionalString("oauth.apple.clientId"),
            appleClientSecret = config.optionalString("oauth.apple.clientSecret"),
        )
    }
}

private fun Config.optionalString(path: String): String? =
    if (hasPath(path)) getString(path).takeIf(String::isNotBlank) else null
