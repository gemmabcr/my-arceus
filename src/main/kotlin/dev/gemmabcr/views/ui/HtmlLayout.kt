package dev.gemmabcr.views.ui

import dev.gemmabcr.models.Language
import dev.gemmabcr.models.Session
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18n
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.row
import io.ktor.server.html.Template
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.details
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.head
import kotlinx.html.h1 as htmlH1
import kotlinx.html.h2 as htmlH2
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.style
import kotlinx.html.summary
import kotlinx.html.title
import kotlinx.html.unsafe

abstract class HtmlLayout(
    private val heading: I18nKey,
    private val session: Session = Session(),
) : Template<HTML> {
    protected fun translate(key: I18nKey) = I18n.getMessage(key)

    override fun HTML.apply() {
        pageHead()
        body {
            header()
            mainContent()
            footerImage()
        }
    }

    abstract fun DIV.content()

    private fun HTML.pageHead() {
        head {
            title { +translate(CommonI18nKey.TITLE) }
            link(rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
            style {
                unsafe {
                    raw(
                        """
                                body { font-family: sans-serif; margin: 0; padding: 0; background-color: #fbfaf4; }
                                table { border-collapse: collapse; width: 100%; }
                                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                                a { color: inherit; }
                            """.trimIndent()
                    )
                }
            }
        }
    }

    private fun FlowContent.header() {
        row(
            JustifyContent.SPACE_BETWEEN,
            AlignItems.CENTER,
            style = headerStyle,
        ) {
            htmlH1 {
                style = "margin: 0; color: ${Colors.DARKEST_BLUE}; font-size: 1.45rem;"
                +translate(CommonI18nKey.HEADING)
            }
            row(gap = Gap.MAX, align = AlignItems.CENTER, style = "flex-wrap: wrap;") {
                mainMenu()
                languageSelector()
                if (session.user == null) {
                    headerLink("/login") {
                        +translate(CommonI18nKey.LOGIN)
                    }
                } else {
                    authenticatedLinks()
                }
            }
        }
    }

    private fun DIV.authenticatedLinks() {
        headerLink("/profile") {
            +"Perfil"
        }
        form(action = "/logout", method = FormMethod.post) {
            style = "margin: 0;"
            button {
                style = headerButtonStyle
                +"Tancar sessio"
            }
        }
    }

    private fun FlowContent.mainContent() {
        div {
            style = "max-width: 980px; margin-left: auto; margin-right: auto; padding: 0 1rem;"
            htmlH2 {
                style = "color: ${Colors.DARKEST_BLUE}; margin: 1.5rem 0 1rem;"
                +translate(heading)
            }
            content()
        }
    }

    private fun FlowContent.footerImage() {
        img(
            translate(CommonI18nKey.ALT_IMG),
            ImageSource.FOOTER.url,
        ) {
            style = "margin-top: 4rem; max-height: 800px; object-fit: cover; width: 100%;"
        }
    }

    private fun DIV.headerLink(href: String, block: kotlinx.html.A.() -> Unit) {
        a(href = href) {
            style = headerButtonStyle
            block()
        }
    }

    private fun DIV.mainMenu() {
        details {
            style = "position: relative;"
            summary {
                style = headerButtonStyle
                +"Menu"
            }
            div {
                style = menuStyle
                menuItem("/pokemons", "Pokemons")
                if (session.user != null) {
                    menuItem("/pokemons#my-team", "El meu equip")
                    menuItem("/ocr", "Puja el teu proces")
                    menuItem("/profile", "Perfil")
                }
            }
        }
    }

    private val headerButtonStyle =
        "text-decoration: none; color: ${Colors.DARK_BLUE}; font-weight: bold;" +
                " padding: 0.5rem 0.75rem; border-radius: 8px; border: 1px solid ${Colors.CREAM};" +
                " background-color: white; cursor: pointer; font-family: inherit; font-size: 0.95rem;"

    private val headerStyle =
        "background-color: white; box-shadow: -8px 10px 30px -15px rgba(0, 0, 0, 0.32); " +
                "padding: 0.85rem 1rem; position: sticky; top: 0; z-index: 10; flex-wrap: wrap;"

    private val menuStyle =
        "position: absolute; right: 0; top: 2.6rem; min-width: 220px; background-color: white; " +
                "border: 1px solid ${Colors.CREAM}; border-radius: 8px; " +
                "box-shadow: rgba(0,0,0,0.18) 0 8px 20px; " +
                "padding: 0.4rem; display: flex; flex-direction: column; gap: 0.25rem;"
}

private fun DIV.menuItem(href: String, label: String) {
    a(href = href) {
        style =
            "display: block; text-decoration: none; color: ${Colors.DARKEST_BLUE}; padding: 0.65rem 0.75rem; " +
                    "border-radius: 6px; font-weight: 600;"
        +label
    }
}

private fun DIV.languageSelector() {
    row(
        gap = Gap.MIN,
        align = AlignItems.CENTER,
        style = "background-color: ${Colors.CREAM_LIGHEST}; padding: 0.2rem; border-radius: 999px;",
    ) {
        Language.entries.forEach {
            a(href = it.href()) {
                style =
                    "text-decoration: none; color: ${Colors.DARK_BLUE}; font-size: 0.78rem; font-weight: 700; " +
                            "padding: 0.28rem 0.5rem; border-radius: 999px;"
                +it.name
            }
        }
    }
}
