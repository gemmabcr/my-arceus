package dev.gemmabcr.views.ui

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.grid(
    templateColumns: String,
    style: String? = null,
    block: DIV.() -> Unit
) {
    div {
        this.style = "display: grid; grid-template-columns: $templateColumns;${style?.let { " $it" } ?: ""}"
        block()
    }
}
