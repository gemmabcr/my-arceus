package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.borders.Border
import dev.gemmabcr.views.ui.borders.BorderRadius
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.style

fun FlowContent.buttonLink(href: String, label: String) {
    div {
        a(href = href) {
            val color = Colors.BLUE
            style =
                "${Border(color, value = 2, radius = BorderRadius.MIN).text()}; color: ${color.hex}; cursor: pointer; " +
                        "font-weight: 600; justify-content: center; padding: 0.25rem 0.5rem;"
            +label
        }
    }
}
