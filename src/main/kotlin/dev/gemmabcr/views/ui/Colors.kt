package dev.gemmabcr.views.ui

enum class Colors(private val hex: String) {
    CREAM("#E6DBD0"),
    CREAM_LIGHEST("#F1EBE4"),
    DARK_BLUE("#1A4A63"),
    ON_DARK_BLUE("#EFF7FB"),
    DARKEST_BLUE("#0D2430"),
    WHITE("#FFFFFF"),
    BLUE_GREY("#99BBD6"),
    ;

    override fun toString(): String = hex
}
