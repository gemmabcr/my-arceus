package dev.gemmabcr.api

data class PokemonEntry(
    val hisuiId: Int,
    val name: String,
    val url: String
) {
    fun id(): Int = url.split("/").last { it.isNotBlank() }.toInt()
}
