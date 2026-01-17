package dev.gemmabcr.database.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ToDoDto(
    val id: Int,
    val goal: Int,
)
