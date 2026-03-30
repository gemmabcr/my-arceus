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
    flex(
        FlexConfig(
            FlexDirection.ROW,
            justifyContent,
            align,
            gap,
            style
        ),
        block = block
    )
}

fun FlowContent.column(
    justifyContent: JustifyContent = JustifyContent.START,
    align: AlignItems = AlignItems.START,
    gap: Gap = Gap.DEFAULT,
    style: String? = null,
    block: DIV.() -> Unit
) {
    flex(
        FlexConfig(
            FlexDirection.COLUMN,
            justifyContent,
            align,
            gap,
            style
        ), block
    )
}

private fun FlowContent.flex(
    config: FlexConfig,
    block: DIV.() -> Unit
) {
    div {
        this.style = config.style()
        block()
    }
}
