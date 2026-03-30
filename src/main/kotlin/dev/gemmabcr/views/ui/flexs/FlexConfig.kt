package dev.gemmabcr.views.ui.flexs

class FlexConfig(
    val direction: FlexDirection,
    val justifyContent: JustifyContent,
    val align: AlignItems,
    val gap: Gap,
    val style: String? = null,
) {
    fun style(): String =  "display: flex; ${direction.text()} ${justifyContent.text()} " +
            "${align.text()} ${gap.text()};${maybeAddedStyle(style)}"

    private fun maybeAddedStyle(style: String?): String = style?.let { " $it" } ?: ""
}
