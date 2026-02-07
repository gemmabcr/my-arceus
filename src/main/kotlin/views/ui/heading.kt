package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.row
import kotlinx.html.BODY

fun BODY.heading(title: String) {
    row(
        JustifyContent.SPACE_BETWEEN,
        AlignItems.CENTER,
        style = "background-color: white; box-shadow: -8px 10px 30px -15px rgba(0, 0, 0, 0.44); padding: 1rem;"
    ) {
        h1(title)
        button("Iniciar sessión")
    }
}
