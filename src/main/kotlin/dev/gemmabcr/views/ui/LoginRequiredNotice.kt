package dev.gemmabcr.views.ui

import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18nKey
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.p

class LoginRequiredNotice(private val message: I18nKey) : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        div {
            classes = setOf("login-required-notice")
            p {
                classes = setOf("ui-muted")
                +translate(message)
                +" "
                a(href = "/login") {
                    classes = setOf("login-required-link")
                    +translate(CommonI18nKey.LOGIN)
                }
            }
        }
    }
}
