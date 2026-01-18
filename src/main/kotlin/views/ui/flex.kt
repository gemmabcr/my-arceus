package dev.gemmabcr.views.ui

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.row(justifyContent: String? = null, block: DIV.() -> Unit) {
    div {
        style = "display: flex; $justifyContent"
        block()
    }
}

fun FlowContent.column(block: DIV.() -> Unit) {
    div {
        style = "display: flex; flex-direction: column"
        block()
    }
}
