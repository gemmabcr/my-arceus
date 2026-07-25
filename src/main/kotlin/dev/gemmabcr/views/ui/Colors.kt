package dev.gemmabcr.views.ui

enum class Colors(private val hex: String) {
    BLUE_GREY("#6D86AD"),
    CREAM("#D8BC78"),
    CREAM_LIGHEST("#D9E5DC"),
    DARKEST_BLUE("#2F292A"),
    DARK_BLUE("#334E87"),
    EXPEDITION_RED("#C84D4C"),
    MEADOW("#9AB16D"),
    ON_DARK_BLUE("#D9E5DC"),
    PAGE_BACKGROUND("#EEF3EF"),
    SKY("#72B7BE"),
    WHITE("#FFFFFF"),
    ;

    override fun toString(): String = hex
}
