package dev.gemmabcr.views.ui.flexs

enum class JustifyContent {
    SPACE_BETWEEN,
    CENTER,
    START;

    fun text(): String = "justify-content: ${this.name.lowercase().replace("_", "-")};"
}
