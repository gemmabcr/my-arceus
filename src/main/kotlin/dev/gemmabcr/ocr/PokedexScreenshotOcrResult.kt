package dev.gemmabcr.ocr

data class PokedexScreenshotOcrResult(
    val pokemonName: String?,
    val pokemonNumber: Int?,
    val progressLevel: Int?,
    val tasks: List<PokedexTaskProgress>,
    val warning: List<OcrWarning>? = null
)

enum class OcrWarning {
    MISSING_NAME,
    MISSING_NUMBER,
    MISSING_PROGRESS,
    MISSING_TASKS,
}
