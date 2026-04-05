package dev.gemmabcr.ocr

data class PokedexScreenshotOcrResult(
    val pokemonName: String?,
    val pokemonNumber: Int?,
    val progressLevel: Int?,
    val tasks: List<PokedexTaskProgress>,
    val warning: String? = null
)
