package dev.gemmabcr.views.ui

import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.style

fun DIV.form(action: String = "/", method: FormMethod = FormMethod.get, block: kotlinx.html.FORM.() -> Unit) {
    form(action = action, method = method) {
        style = "width: 100%;"
        block()
    }
}

fun DIV.button(text: String, type: ButtonType = ButtonType.submit) {
    button(type = type) {
        style = "background-color: #D8D2AB; border: none; padding: 0.5rem 1rem; border-radius: 4px; cursor: pointer; font-weight: bold; color: #4A4A4A;"
        +text
    }
}
