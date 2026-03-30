package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.borders.Border
import dev.gemmabcr.views.ui.borders.BorderRadius
import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.style

fun FlowContent.buttonLink(href: String, label: String) {
    column {
        a(href = href) {
            val color = Colors.DARK_BLUE
            style =
                "${Border(color, value = 2, radius = BorderRadius.MIN).text()}; color: $color; cursor: pointer; " +
                        "font-weight: 600; justify-content: center; padding: 0.25rem 0.5rem;"
            +label
        }
    }
}
