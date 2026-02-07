package dev.gemmabcr.views.ui.borders

enum class BorderRadius(private val value: String) {
    MIN("0.25"),
    DEFAULT("0.5"),
    MAX("1"),
    ;

    fun text(): String = "border-radius: ${value}rem;"
}
