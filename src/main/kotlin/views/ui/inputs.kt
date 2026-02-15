package dev.gemmabcr.views.ui

import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.select

fun DIV.selectInput(label: String, name: String, options: List<String>, value: String? = null) {
    div {
        label { +label }
        select {
            this.name = name
            options.forEach {
                option {
                    selected = value == it
                    +it
                }
            }
        }
    }
}
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
