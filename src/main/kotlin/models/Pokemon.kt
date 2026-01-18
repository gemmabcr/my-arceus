package dev.gemmabcr.models

import dev.gemmabcr.models.models.Type

data class Pokemon(
    val hisuiId: Int,
    val generalId: Int,
    val name: String,
    val types: List<Type>,
    val location: List<Int>,
    val toDos: List<ToDo>,
)
