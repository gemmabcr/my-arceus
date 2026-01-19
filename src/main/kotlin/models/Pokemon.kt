package dev.gemmabcr.models

data class Pokemon(
    val hisuiId: Int,
    val generalId: Int,
    val name: String,
    val types: List<Type>,
    val location: List<Location>,
    val toDos: List<ToDo>,
    val specialCondition: SpecialCondition?,
)
