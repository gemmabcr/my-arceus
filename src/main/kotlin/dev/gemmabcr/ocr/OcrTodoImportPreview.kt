package dev.gemmabcr.ocr

data class OcrTodoImportPreview(
    val pokemonId: Int?,
    val pokemonName: String?,
    val matchedToDos: List<OcrMatchedToDo>,
    val unmatchedTasks: List<PokedexTaskProgress>,
)
