package dev.gemmabcr.views.ui

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.style

fun FlowContent.buttonLink(href: String, label: String) {
    a(href = href) {
        style =
            "border: 2px solid #3695bb; border-radius: 0.25rem; color: #1684b0; cursor: pointer; " +
                    "font-weight: 600; justify-content: center; padding: 0.25rem 0.5rem;"
        +label
    }
}
