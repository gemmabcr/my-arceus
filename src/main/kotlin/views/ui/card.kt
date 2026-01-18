package dev.gemmabcr.views.ui

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.card(block: FlowContent.() -> Unit) {
    div {
        style =
            "background-color: #F2F0E3; border-radius: 0 0 1rem 1rem; box-shadow: rgba(0, 0, 0, 0.16) 0 1px 4px; width: 100%;"
        block()
    }
}
