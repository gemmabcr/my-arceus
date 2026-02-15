package dev.gemmabcr.views.ui.flexs

enum class AlignItems {
    CENTER,
    END,
    START;

    fun text(): String = "align-items: ${this.name.lowercase()};"
}
