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
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.footer
import kotlinx.html.head
import kotlinx.html.h1 as htmlH1
import kotlinx.html.h2 as htmlH2
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.span
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe

abstract class HtmlLayout(
    private val heading: I18nKey,
    private val session: Session = Session(),
    private val showHeading: Boolean = true,
    private val activeMenuItem: MenuItem = MenuItem.POKEDEX,
) : Template<HTML> {
    private val locale = I18n.getLocale()

    protected fun translate(key: I18nKey) = I18n.getMessage(key)

    override fun HTML.apply() {
        I18n.setLocale(locale)
        pageHead()
        body {
            attributes["data-active-menu"] = activeMenuItem.name.lowercase()
            header()
            mainContent()
            pageFooter()
        }
    }

    abstract fun DIV.content()

    private fun HTML.pageHead() {
        head {
            title { +translate(CommonI18nKey.TITLE) }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1"
            }
            link(rel = "icon", href = "/icons/favicon.svg", type = "image/svg+xml")
            style {
                unsafe {
                    raw(
                        """
                                body {
                                    font-family: sans-serif;
                                    margin: 0;
                                    padding: 0;
                                    background-color: ${Colors.PAGE_BACKGROUND};
                                }
                                * { box-sizing: border-box; }
                                table { border-collapse: collapse; width: 100%; }
                                th, td { border: 1px solid ${Colors.BLUE_GREY}; padding: 8px; text-align: left; }
                                a { color: inherit; }
                                $sharedComponentStyles
                                $privacyStyles
                                $pokemonListStyles
                                $ocrStyles
                                @media (max-width: 680px) {
                                    .pokemon-card-body { grid-template-columns: 1fr !important; }
                                }
                            """.trimIndent()
                    )
                }
            }
        }
    }

    private val sharedComponentStyles =
        """
            :root {
                --hisui-sky: ${Colors.SKY};
                --hisui-mist: ${Colors.CREAM_LIGHEST};
                --hisui-mountain: ${Colors.BLUE_GREY};
                --hisui-deep: ${Colors.DARK_BLUE};
                --hisui-meadow: ${Colors.MEADOW};
                --hisui-gold: ${Colors.CREAM};
                --hisui-red: ${Colors.EXPEDITION_RED};
                --hisui-charcoal: ${Colors.DARKEST_BLUE};
            }
            .ui-card {
                background: ${Colors.CREAM_LIGHEST} !important;
                border: 1px solid ${Colors.BLUE_GREY};
                border-radius: 12px;
                box-shadow: 0 3px 12px rgba(20, 45, 61, 0.08);
            }
            .ui-panel {
                background: ${Colors.CREAM_LIGHEST};
                border: 1px solid ${Colors.BLUE_GREY};
                border-radius: 10px;
            }
            .ui-primary-button,
            .ui-secondary-button {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                min-height: 40px;
                padding: 0.55rem 0.9rem;
                border-radius: 8px;
                cursor: pointer;
                font: inherit;
                font-size: 0.85rem;
                font-weight: 700;
                line-height: 1.2;
                text-decoration: none;
                transition: transform 0.15s ease, box-shadow 0.15s ease, background-color 0.15s ease;
            }
            .ui-primary-button {
                color: ${Colors.ON_DARK_BLUE};
                background: ${Colors.DARK_BLUE};
                border: 1px solid ${Colors.DARK_BLUE};
            }
            .ui-secondary-button {
                color: ${Colors.DARK_BLUE};
                background: ${Colors.CREAM_LIGHEST};
                border: 1px solid ${Colors.BLUE_GREY};
            }
            .ui-primary-button:hover,
            .ui-secondary-button:hover {
                transform: translateY(-1px);
                box-shadow: 0 4px 10px rgba(20, 45, 61, 0.14);
            }
            .ui-primary-button:focus-visible,
            .ui-secondary-button:focus-visible,
            .ui-field:focus-visible {
                outline: 3px solid ${Colors.CREAM};
                outline-offset: 2px;
            }
            .ui-primary-button:disabled,
            .ui-secondary-button:disabled {
                opacity: 0.45;
                cursor: not-allowed;
                transform: none;
                box-shadow: none;
            }
            .ui-field {
                width: 100%;
                min-height: 40px;
                padding: 0.6rem 0.7rem;
                border: 1px solid ${Colors.BLUE_GREY};
                border-radius: 8px;
                background: ${Colors.WHITE};
                color: ${Colors.DARKEST_BLUE};
                font: inherit;
                font-size: 0.85rem;
            }
            .ui-label {
                display: block;
                margin-bottom: 0.35rem;
                color: ${Colors.DARKEST_BLUE};
                font-size: 0.78rem;
                font-weight: 700;
            }
            .ui-muted {
                color: ${Colors.DARK_BLUE};
                font-size: 0.85rem;
                line-height: 1.5;
            }
            .pokemon-card-body { background: transparent; }
            .menu-item-active {
                background-color: ${Colors.BLUE_GREY} !important;
                color: ${Colors.ON_DARK_BLUE} !important;
                box-shadow: 0 1px 3px rgba(20, 45, 61, 0.18);
            }
            .auth-privacy-notice {
                margin: -0.2rem 0 0;
                color: ${Colors.DARK_BLUE};
                font-size: 0.72rem;
                line-height: 1.45;
            }
            .auth-privacy-notice a,
            .page-footer-links a {
                color: ${Colors.DARK_BLUE};
                font-weight: 700;
            }
            .page-footer-links {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 0.65rem;
                max-width: 980px;
                margin: 3rem auto 0;
                padding: 1rem;
                color: ${Colors.DARK_BLUE};
                font-size: 0.8rem;
            }
        """.trimIndent()

    private val pokemonListStyles =
        """
            .pokemon-list-layout {
                display: grid;
                grid-template-columns: minmax(220px, 260px) minmax(0, 1fr);
                gap: 1.5rem;
                align-items: start;
            }
            .pokemon-filter-sidebar {
                position: sticky;
                top: 5.5rem;
                max-height: calc(100vh - 6.5rem);
                overflow-y: auto;
                overscroll-behavior: contain;
                box-sizing: border-box;
                padding: 1rem;
            }
            .pokemon-filter-sidebar select,
            .pokemon-filter-sidebar input[type="text"],
            .pokemon-filter-sidebar input[type="number"] {
                width: 100%;
                min-width: 0;
                box-sizing: border-box;
            }
            .pokemon-filter-sidebar form,
            .pokemon-filter-sidebar form > div,
            .pokemon-filter-fields,
            .pokemon-filter-fields > div {
                width: 100%;
                min-width: 0;
                box-sizing: border-box;
            }
            .pokemon-filter-fields {
                display: grid !important;
                grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
                gap: 0.75rem;
                margin: 1rem 0;
            }
            .area-filter-options {
                display: grid;
                grid-template-columns: repeat(2, minmax(0, 1fr));
                gap: 0.5rem;
                width: 100%;
            }
            .area-filter-option-input {
                position: absolute;
                opacity: 0;
                pointer-events: none;
            }
            .area-filter-option {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                gap: 0.3rem;
                min-width: 0;
                padding: 0.45rem;
                border: 2px solid transparent;
                border-radius: 8px;
                background-color: ${Colors.CREAM_LIGHEST};
                color: ${Colors.DARKEST_BLUE};
                cursor: pointer;
                text-align: center;
                font-size: 0.72rem;
                font-weight: 600;
                box-sizing: border-box;
            }
            .area-filter-option img {
                width: 64px;
                height: 64px;
                object-fit: contain;
            }
            .area-filter-option-all { grid-column: 1 / -1; min-height: 38px; }
            .area-filter-option-input:checked + .area-filter-option {
                border-color: ${Colors.DARK_BLUE};
                background-color: ${Colors.DARK_BLUE};
                color: ${Colors.ON_DARK_BLUE};
                box-shadow: rgba(0, 0, 0, 0.12) 0 1px 3px;
            }
            .area-filter-option-input:focus-visible + .area-filter-option {
                outline: 3px solid ${Colors.DARK_BLUE};
                outline-offset: 2px;
            }
            .type-filter-options {
                display: grid;
                grid-template-columns: repeat(2, minmax(0, 1fr));
                gap: 0.4rem;
                width: 100%;
            }
            .type-filter-option-input {
                position: absolute;
                opacity: 0;
                pointer-events: none;
            }
            .type-filter-option {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.35rem;
                min-width: 0;
                min-height: 36px;
                padding: 0.35rem 0.45rem;
                border: 2px solid transparent;
                border-radius: 0.25rem;
                background-color: ${Colors.DARK_BLUE};
                color: ${Colors.ON_DARK_BLUE};
                cursor: pointer;
                font-size: 0.72rem;
                font-weight: 600;
                box-sizing: border-box;
                opacity: 0.72;
            }
            .type-filter-option:hover { opacity: 1; }
            .type-filter-option img { width: 18px; height: 18px; flex: 0 0 auto; }
            .type-filter-option span { min-width: 0; overflow-wrap: anywhere; }
            .type-filter-option-all { grid-column: 1 / -1; }
            .type-filter-option-input:checked + .type-filter-option {
                border-color: ${Colors.CREAM};
                box-shadow: 0 0 0 2px ${Colors.DARK_BLUE};
                opacity: 1;
            }
            .type-filter-option-input:focus-visible + .type-filter-option {
                outline: 3px solid ${Colors.CREAM};
                outline-offset: 2px;
            }
            .pokemon-filter-sidebar form > div > button { width: 100%; }
            @media (max-width: 560px) {
                .pokemon-list-layout { grid-template-columns: 1fr; }
                .pokemon-filter-sidebar {
                    position: static;
                    max-height: none;
                    overflow-y: visible;
                }
            }
            @media (max-width: 720px) {
                .auth-layout { grid-template-columns: 1fr !important; }
            }
        """.trimIndent()

    private val ocrStyles =
        """
            .ocr-layout { width: 100%; }
            .ocr-card { width: 100%; overflow: hidden; }
            .ocr-card-header {
                padding: 0.7rem 1rem;
                background: ${Colors.DARK_BLUE};
            }
            .ocr-card-header h3 {
                margin: 0;
                color: ${Colors.ON_DARK_BLUE};
                font-size: 1rem;
                line-height: 1.3;
            }
            .ocr-card-body { padding: 1.25rem; }
            .ocr-card-body p,
            .login-required-notice p,
            .ocr-message p,
            .ocr-message h3,
            .ocr-stat-card p,
            .ocr-task-row p { margin: 0; }
            .ocr-description { max-width: 720px; }
            .login-required-notice {
                width: 100%;
                padding: 0.15rem 0 0.15rem 0.75rem;
                border-left: 3px solid ${Colors.BLUE_GREY};
            }
            .login-required-link {
                color: ${Colors.DARK_BLUE};
                font-weight: 700;
                text-decoration: underline;
                text-underline-offset: 2px;
            }
            .ocr-upload-form {
                display: grid;
                grid-template-columns: minmax(0, 1fr) auto;
                align-items: center;
                gap: 0.75rem;
                width: 100%;
            }
            .ocr-file-picker {
                position: relative;
                display: flex;
                align-items: center;
                overflow: hidden;
                cursor: pointer;
                background: ${Colors.WHITE};
            }
            .ocr-file-picker span {
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
            .ocr-file-input {
                position: absolute;
                inset: 0;
                width: 100%;
                height: 100%;
                opacity: 0;
                cursor: pointer;
            }
            .ocr-form-disabled { opacity: 0.55; }
            .ocr-form-disabled .ocr-file-picker,
            .ocr-form-disabled .ocr-file-input { cursor: not-allowed; }
            .ocr-submit-button { min-width: 130px; }
            .ocr-stats {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
                gap: 0.75rem;
                width: 100%;
            }
            .ocr-stat-card { padding: 1rem 1.15rem; }
            .ocr-stat-label {
                color: ${Colors.DARK_BLUE};
                font-size: 0.78rem;
                font-weight: 700;
            }
            .ocr-stat-value {
                margin-top: 0.3rem !important;
                color: ${Colors.DARKEST_BLUE};
                font-size: 1.55rem;
                font-weight: 700;
            }
            .ocr-task-row {
                display: grid;
                grid-template-columns: minmax(0, 1fr) auto;
                align-items: center;
                gap: 1rem;
                padding: 0.7rem 0.85rem;
                color: ${Colors.DARKEST_BLUE};
            }
            .ocr-task-value {
                color: ${Colors.DARK_BLUE};
                font-weight: 700;
            }
            .ocr-import-form {
                display: flex;
                flex-direction: column;
                align-items: flex-start;
                gap: 0.75rem;
            }
            .ocr-unmatched-task {
                padding: 0.6rem 0.75rem;
                border-left: 3px solid ${Colors.BLUE_GREY};
                color: ${Colors.DARKEST_BLUE};
            }
            .ocr-message { padding: 1rem 1.15rem; }
            .ocr-message h3 {
                color: ${Colors.DARK_BLUE};
                font-size: 1rem;
            }
            .ocr-message p {
                margin-top: 0.35rem;
                color: ${Colors.DARKEST_BLUE};
            }
            .ocr-message-error {
                background: color-mix(in srgb, ${Colors.EXPEDITION_RED} 15%, ${Colors.CREAM_LIGHEST}) !important;
                border-color: ${Colors.EXPEDITION_RED};
            }
            .ocr-message-warning {
                background: color-mix(in srgb, ${Colors.BLUE_GREY} 15%, ${Colors.CREAM_LIGHEST}) !important;
            }
            @media (max-width: 560px) {
                .ocr-upload-form { grid-template-columns: 1fr; }
                .ocr-submit-button { width: 100%; }
                .ocr-task-row { align-items: start; }
            }
        """.trimIndent()

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
                if (session.user != null) logoutButton()
            }
        }
    }

    private fun DIV.logoutButton() {
        form(action = "/logout", method = FormMethod.post) {
            style = "margin: 0;"
            button {
                style = headerButtonStyle
                +translate(CommonI18nKey.LOGOUT)
            }
        }
    }

    private fun FlowContent.mainContent() {
        div {
            style = "max-width: 980px; margin-left: auto; margin-right: auto; padding: 0 1rem;"
            if (showHeading) {
                htmlH2 {
                    style = "color: ${Colors.DARK_BLUE}; margin: 1.5rem 0 1rem;"
                    +translate(heading)
                }
            }
            content()
        }
    }

    private fun FlowContent.pageFooter() {
        footer {
            div {
                classes = setOf("page-footer-links")
                a(href = "/privacy") { +translate(CommonI18nKey.PRIVACY_POLICY) }
                span { +"·" }
                a(href = "mailto:$PRIVACY_EMAIL") { +PRIVACY_EMAIL }
            }
            img(
                translate(CommonI18nKey.ALT_IMG),
                ImageSource.FOOTER.url,
            ) {
                style = "max-height: 800px; object-fit: cover; width: 100%;"
            }
        }
    }

    private fun DIV.mainMenu() {
        row(
            gap = Gap.MIN,
            align = AlignItems.CENTER,
            style =
                "background-color: ${Colors.DARK_BLUE}; padding: 0.25rem; border-radius: 8px; " +
                        "flex-wrap: wrap;"
        ) {
            menuItem(
                "/pokemons",
                translate(CommonI18nKey.LIST),
                MenuItem.POKEDEX,
                active = activeMenuItem == MenuItem.POKEDEX,
            )
            menuItem(
                "/team",
                translate(CommonI18nKey.MY_TEAM),
                MenuItem.MY_TEAM,
                active = activeMenuItem == MenuItem.MY_TEAM,
            )
            menuItem(
                "/ocr",
                translate(CommonI18nKey.UPLOAD_PROGRESS),
                MenuItem.OCR,
                active = activeMenuItem == MenuItem.OCR,
            )
            if (session.user != null) {
                menuItem(
                    "/profile",
                    translate(CommonI18nKey.PROFILE),
                    MenuItem.PROFILE,
                    active = activeMenuItem == MenuItem.PROFILE,
                )
            } else {
                menuItem(
                    "/login",
                    translate(CommonI18nKey.LOGIN),
                    MenuItem.LOGIN,
                    active = activeMenuItem == MenuItem.LOGIN,
                )
            }
        }
    }

    private val headerButtonStyle =
        "text-decoration: none; color: ${Colors.DARK_BLUE}; font-weight: bold;" +
                " padding: 0.5rem 0.75rem; border-radius: 8px; border: 1px solid ${Colors.BLUE_GREY};" +
                " background-color: ${Colors.CREAM_LIGHEST}; cursor: pointer; font-family: inherit;" +
                " font-size: 0.95rem;"

    private val headerStyle =
        "background-color: ${Colors.CREAM_LIGHEST}; box-shadow: -8px 10px 30px -15px rgba(0, 0, 0, 0.32); " +
                "padding: 0.85rem 1rem; position: sticky; top: 0; z-index: 10; flex-wrap: wrap;"

}

enum class MenuItem { LOGIN, MY_TEAM, OCR, POKEDEX, PRIVACY, PROFILE }

private fun DIV.menuItem(
    href: String,
    label: String,
    item: MenuItem,
    enabled: Boolean = true,
    active: Boolean = false,
) {
    val style =
                "display: inline-flex; align-items: center; min-height: 34px; box-sizing: border-box; " +
                "text-decoration: none; color: ${Colors.DARKEST_BLUE}; padding: 0.4rem 0.65rem; " +
                "border-radius: 6px; background-color: ${Colors.CREAM_LIGHEST}; font-size: 0.88rem; " +
                "font-weight: 600; " +
                "white-space: nowrap;"
    if (enabled) {
        a(href = href) {
            this.style = style
            attributes["data-menu-item"] = item.name.lowercase()
            if (active) attributes["class"] = "menu-item-active"
            if (active) attributes["aria-current"] = "page"
            +label
        }
    } else {
        span {
            this.style = "$style opacity: 0.45; cursor: not-allowed;"
            attributes["aria-disabled"] = "true"
            +label
        }
    }
}

private fun DIV.languageSelector() {
    row(
        gap = Gap.MIN,
        align = AlignItems.CENTER,
        style = "background-color: ${Colors.DARK_BLUE}; padding: 0.2rem; border-radius: 999px;",
    ) {
        Language.entries.forEach {
            a(href = it.href()) {
                style =
                    "display: flex; align-items: center; text-decoration: none; padding: 0.28rem; " +
                            "border-radius: 999px;"
                title = it.label
                attributes["aria-label"] = it.label
                img(src = it.flagPath, alt = it.label) {
                    style = "display: block; width: 24px; height: 16px; object-fit: cover; border-radius: 2px;"
                }
            }
        }
    }
}
