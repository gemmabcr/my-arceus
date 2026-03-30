package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.onMouseOut
import kotlinx.html.onMouseOver
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
    block: (kotlinx.html.BUTTON.() -> Unit)? = null
) {
    button(type = type) {
        style = "background-color: ${Colors.CREAM}; border: none; padding: 0.6rem 1.2rem; border-radius: 8px; " +
                "cursor: pointer; font-weight: bold; color: ${Colors.DARKEST_BLUE}; height: 42px; " +
                "transition: all 0.2s ease-in-out; box-shadow: 0 2px 4px rgba(0,0,0,0.1);"
        onMouseOver =
            "this.style.backgroundColor='#EAE4C3'; " +
                    "this.style.transform='translateY(-1px)'; " +
                    "this.style.boxShadow='0 4px 6px rgba(0,0,0,0.15)'"
        onMouseOut =
            "this.style.backgroundColor='${Colors.CREAM}'; this.style.transform='translateY(0)';" +
                    " this.style.boxShadow='0 2px 4px rgba(0,0,0,0.1)'"
        +text
        block?.invoke(this)
    }
}
