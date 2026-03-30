package dev.gemmabcr.views.ui.flexs

enum class Gap(private val value: String) {
    MIN("0.25"),
    DEFAULT("0.5"),
    MAX("1"),
    ;

    fun text(): String = "gap: ${value}rem;"
}
