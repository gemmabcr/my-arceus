package dev.gemmabcr.views.ui

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.style

fun FlowContent.typeChips(types: List<Pair<String, String>>) {
    div {
        style = "display: flex;"
        types.forEach {
            div {
                style =
                    "display: flex; justify-content: center; background-color: #3d3d3d; border-radius: 0.25rem; color: #fff; margin-right: 0.5rem; padding: 0.25rem 0.5rem;"
                img(src = "/icons/types/${it.first}.svg") {
                    height = "16"
                    width = "16"
                    style = "background-color: #eee; border-radius: 0.25rem; margin-right: 0.5rem;"
                }
                +it.second
            }
        }
    }
}
