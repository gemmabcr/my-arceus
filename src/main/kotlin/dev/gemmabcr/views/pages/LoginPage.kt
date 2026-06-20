package dev.gemmabcr.views.pages

import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.models.Session
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.button
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.form
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.p
import kotlinx.html.style

class LoginPage(
    private val error: String? = null,
    private val session: Session = Session(),
) : HtmlLayout(CommonI18nKey.LOGIN, session) {
    override fun DIV.content() {
        column(gap = Gap.MAX) {
            error?.let {
                p {
                    style = "color: #9b1c1c; font-weight: 600;"
                    +it
                }
            }
            form(action = "/login", method = FormMethod.post) {
                style =
                    "display: flex; flex-direction: column; gap: 1rem; max-width: 360px; " +
                            "padding: 1rem; border: 1px solid ${Colors.CREAM}; border-radius: 8px;"
                loginInput("Email", "email", InputType.email)
                loginInput("Password", "password", InputType.password)
                button("Log in", ButtonType.submit)
            }
        }
    }

    private fun FlowContent.loginInput(label: String, name: String, type: InputType) {
        column(gap = Gap.MIN) {
            label {
                style = "font-size: 0.85rem; color: #555; font-weight: 600;"
                +label
            }
            input(type = type, name = name) {
                required = true
                style =
                    "padding: 0.6rem; border: 1px solid #ddd; border-radius: 8px; " +
                            "font-family: inherit;"
            }
        }
    }
}
