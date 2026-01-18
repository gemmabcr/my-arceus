package dev.gemmabcr.database.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ToDoDto(
    val id: Int,
    val description: String,
    val goal: Int = 0,
)
