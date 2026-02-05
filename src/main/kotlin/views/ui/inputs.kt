package dev.gemmabcr.views.ui

import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.input
import kotlinx.html.label

fun DIV.numberInput(label: String, name: String, value: String? = null) {
    input(label, name, InputType.number, value)
}

fun DIV.textInput(label: String, name: String, value: String? = null) {
    input(label, name, InputType.text, value)
}

private fun DIV.input(label: String, name: String, type: InputType, value: String?) {
    div {
        label { +label }
        input(type = type, name = name) {
            this.value = value ?: ""
        }
    }
}
