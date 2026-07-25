package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes

fun FlowContent.buttonLink(href: String, label: String) {
    column {
        a(href = href) {
            classes = setOf("ui-secondary-button")
            +label
        }
    }
}
