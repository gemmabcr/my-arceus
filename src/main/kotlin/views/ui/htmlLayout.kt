package dev.gemmabcr.views.ui

import kotlinx.html.BODY
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe

fun HTML.htmlLayout(block: BODY.() -> Unit) {
    head {
        title { +"My Arceus Pokedex" }
        style {
            unsafe {
                raw(
                    """
                                body { font-family: sans-serif; padding: 2em; }
                                table { border-collapse: collapse; width: 100%; }
                                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                                th { background-color: #f2f2f2; }
                                tr:nth-child(even) { background-color: #f9f9f9; }
                            """.trimIndent()
                )
            }
        }
    }
    body {
        block()
    }
}
