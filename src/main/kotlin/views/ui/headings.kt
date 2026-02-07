package dev.gemmabcr.views.ui

import kotlinx.html.FlowContent
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.h5
import kotlinx.html.style

fun FlowContent.h1(value: String) {
    h1 {
        style = createStyle()
        +value
    }
}

fun FlowContent.h2(value: String) {
    h2 {
        style = createStyle()
        +value
    }
}

fun FlowContent.h3(value: String, margin: Boolean = true) {
    h3 {
        style = createStyle(margin)
        +value
    }
}

fun FlowContent.h4(value: String, margin: Boolean = true) {
    h4 {
        style = createStyle(margin)
        +value
    }
}

fun FlowContent.h5(value: String, margin: Boolean = true) {
    h5 {
        style = createStyle(margin)
        +value
    }
}

private fun createStyle(margin: Boolean = true): String {
    var style = "color: ${Colors.DARK_BLUE.hex};"
    if (margin.not()) {
        style += " margin: 0px;"
    }
    return style
}
