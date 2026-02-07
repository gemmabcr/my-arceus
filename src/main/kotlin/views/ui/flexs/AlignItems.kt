package dev.gemmabcr.views.ui.flexs

enum class AlignItems {
    CENTER,
    START;

    fun text(): String = "align-items: ${this.name.lowercase()};"
}
