package dev.gemmabcr.models

interface BasePokemon {
    val hisuiId: Int
    val generalId: Int
    val name: String
    val types: List<Type>
    val toDos: List<ToDo>
}
