package dev.gemmabcr.views.ui

import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.input
import kotlinx.html.label

fun DIV.input(label: String, name: String, type: InputType = InputType.text) {
    div {
        label { +label }
        input(type = type, name = name)
    }
}
