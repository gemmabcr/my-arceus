package dev.gemmabcr.models

data class Pokemon(
    val hisuiId: Int,
    val generalId: Int,
    val name: String,
    val location: List<Int>,
    val toDos: List<Int>
)
