package dev.gemmabcr.views.ui

import dev.gemmabcr.views.i18n.I18n
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.row
import io.ktor.server.html.Template
import kotlinx.html.DIV
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe

abstract class HtmlLayout(private val heading: I18nKey) : Template<HTML> {
    protected fun translate(key: I18nKey) = I18n.getMessage(key)

    override fun HTML.apply() {
        head {
            title { +translate(I18nKey.TITLE) }
            link(rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
            style {
                unsafe {
                    raw(
                        """
                                body { font-family: sans-serif; margin: 0; padding: 0; }
                                table { border-collapse: collapse; width: 100%; }
                                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                                th { background-color: #f2f2f2; }
                            """.trimIndent()
                    )
                }
            }
        }
        body {
            row(
                JustifyContent.SPACE_BETWEEN,
                AlignItems.CENTER,
                style = "background-color: white; box-shadow: -8px 10px 30px -15px rgba(0, 0, 0, 0.44); padding: 1rem;"
            ) {
                h1(translate(I18nKey.HEADING))
                row(gap = Gap.MAX, align = AlignItems.CENTER) {
                    a(href = "/lang/en") {
                        style =
                            "text-decoration: none; color: ${Colors.DARK_BLUE}; font-weight: bold; padding: 0.5rem; border-radius: 4px; border: 1px solid ${Colors.CREAM};"
                        +"EN"
                    }
                    a(href = "/lang/ca") {
                        style =
                            "text-decoration: none; color: ${Colors.DARK_BLUE}; font-weight: bold; padding: 0.5rem; border-radius: 4px; border: 1px solid ${Colors.CREAM};"
                        +"CA"
                    }
                    button(translate(I18nKey.LOGIN))
                }
            }
            div {
                style = "max-width: 800px; margin-left: auto; margin-right: auto;"
                h2(translate(heading))
                content()
            }
            img(
                translate(I18nKey.ALT_IMG),
                "https://www.nintendo.com/eu/media/images/10_share_images/games_15/nintendo_switch_4/H2x1_NSwitch_PokemonLegendsArceus_ES_image1600w.jpg"
            ) {
                style = "margin-top: 4rem; max-height: 800px; object-fit: cover; width: 100%;"
            }
        }
    }

    abstract fun DIV.content()
}
