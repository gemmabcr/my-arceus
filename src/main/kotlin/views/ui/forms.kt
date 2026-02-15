package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.style

fun DIV.form(action: String, method: FormMethod, submitText: String, block: DIV.() -> Unit) {
    form(action = action, method = method) {
        column(style = "width: 100%;") {
            block()
        }
        button(submitText, ButtonType.submit)
    }
}

fun FlowContent.button(text: String, type: ButtonType = ButtonType.button) {
    button(type = type) {
        style = "background-color: ${Colors.CREAM}; border: none; padding: 0.5rem 1rem; border-radius: 4px; " +
                "cursor: pointer; font-weight: bold; color: ${Colors.DARKEST_BLUE}; height: 40px;"
        +text
    }
}
