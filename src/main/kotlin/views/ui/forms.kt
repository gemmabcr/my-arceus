package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.style

fun DIV.form(action: String = "/", method: FormMethod = FormMethod.get, block: DIV.() -> Unit) {
    form(action = action, method = method) {
        column(style = "width: 100%;") {
            block()
        }
    }
}

fun DIV.button(text: String, type: ButtonType = ButtonType.submit) {
    button(type = type) {
        style = "background-color: ${Colors.CREAM.hex}; border: none; padding: 0.5rem 1rem; border-radius: 4px; " +
                "cursor: pointer; font-weight: bold; color: ${Colors.GREY.hex}; height: 40px;"
        +text
    }
}
