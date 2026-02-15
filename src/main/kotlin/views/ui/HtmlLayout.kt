package dev.gemmabcr.views.ui

import io.ktor.server.html.Template
import kotlinx.html.DIV
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe

abstract class HtmlLayout(private val heading: String, private val block: DIV.() -> Unit) : Template<HTML> {
    override fun HTML.apply() {
        head {
            title { +"My Arceus Pokedex" }
            link(rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
            style {
                unsafe {
                    raw(
                        """
                                body { font-family: sans-serif; margin: 0; padding: 0; }
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
            heading("Pokédex tracking list")
            div {
                style = "max-width: 800px; margin-left: auto; margin-right: auto;"
                h2(heading)
                block()
            }
            img(
                "Thanks for visiting us",
                "https://www.nintendo.com/eu/media/images/10_share_images/games_15/nintendo_switch_4/H2x1_NSwitch_PokemonLegendsArceus_ES_image1600w.jpg"
            ) {
                style = "margin-top: 4rem; max-height: 800px; object-fit: cover; width: 100%;"
            }
        }
    }
}
