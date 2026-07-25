package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.onClick
import kotlinx.html.onSubmit
import kotlinx.html.style

fun DIV.form(
    config: FormConfig,
    block: DIV.() -> Unit
) {
    form(action = config.action, method = config.method) {
        config.id?.let { this.id = it }
        config.onSubmit?.let { this.onSubmit = it }
        column(style = "width: 100%;") {
            block()
        }
        button(config.submitText, ButtonType.submit)
    }
}

fun FlowContent.button(
    text: String,
    type: ButtonType = ButtonType.button,
    onClick: String? = null,
    style: String? = null,
    block: (kotlinx.html.BUTTON.() -> Unit)? = null
) {
    button(type = type) {
        classes = setOf("ui-primary-button")
        this.style = style.orEmpty()
        onClick?.let { this.onClick = it }
        +text
        block?.invoke(this)
    }
}
