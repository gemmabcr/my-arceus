package dev.gemmabcr.ocr

data class OcrMatchedToDo(
    val todoId: Int,
    val label: String,
    val extractedLabel: String,
    val done: Int,
    val goal: Int,
)
