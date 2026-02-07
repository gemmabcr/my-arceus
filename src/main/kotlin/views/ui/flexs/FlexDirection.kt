package dev.gemmabcr.views.ui.flexs

enum class FlexDirection {
    COLUMN,
    ROW,
    ;

    fun text(): String = "flex-direction: ${this.name.lowercase()};"
}
