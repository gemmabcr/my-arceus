package dev.gemmabcr.views.ui.flexs

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.row(
    justifyContent: JustifyContent = JustifyContent.START,
    align: AlignItems = AlignItems.START,
    gap: Gap = Gap.DEFAULT,
    style: String? = null,
    block: DIV.() -> Unit
) {
    flex(FlexDirection.ROW, justifyContent, align, gap, style = maybeAddedStyle(style), block = block)
}

fun FlowContent.column(
    justifyContent: JustifyContent = JustifyContent.START,
    align: AlignItems = AlignItems.START,
    gap: Gap = Gap.DEFAULT,
    style: String? = null,
    block: DIV.() -> Unit
) {
    flex(FlexDirection.COLUMN, justifyContent, align, gap, maybeAddedStyle(style), block)
}

private fun maybeAddedStyle(style: String?): String = style?.let { " $it" } ?: ""

private fun FlowContent.flex(
    direction: FlexDirection,
    justifyContent: JustifyContent,
    align: AlignItems,
    gap: Gap,
    style: String? = null,
    block: DIV.() -> Unit
) {
    div {
        this.style = "display: flex; ${direction.text()} ${justifyContent.text()} " +
                "${align.text()} ${gap.text()};${maybeAddedStyle(style)}"
        block()
    }
}
