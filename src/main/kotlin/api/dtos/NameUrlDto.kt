package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NameUrlDto(val name: String, val url: String)