package dev.gemmabcr.views.pages.components

import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.row
import kotlinx.html.FlowContent
import kotlinx.html.img
import kotlinx.html.style

fun FlowContent.typeChips(types: List<Pair<String, String>>) {
    row {
        types.forEach {
            row(
                JustifyContent.CENTER, style = "background-color: ${Colors.DARKEST_BLUE}; " +
                        "border-radius: 0.25rem; color: ${Colors.ON_DARK_BLUE}; padding: 0.25rem 0.5rem;"
            ) {
                typeImage(it.first)
                +it.second
            }
        }
    }
}

private fun FlowContent.typeImage(name: String) {
    img(src = "https://raw.githubusercontent.com/partywhale/pokemon-type-icons/refs/heads/main/icons/$name.svg") {
        height = "16"
        width = "16"
        style = "margin-right: 0.5rem;"
    }
}
