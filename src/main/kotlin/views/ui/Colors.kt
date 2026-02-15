package dev.gemmabcr.views.ui

enum class Colors(private val hex: String) {
    CREAM("#E6DBD0"),
    DARK_BLUE("#1A4A63"),
    ON_DARK_BLUE("#EFF7FB"),
    DARKEST_BLUE("#0D2430"),
    LIGHT_GREY("#F4F3EE"),
    ;

    override fun toString(): String = hex
}
