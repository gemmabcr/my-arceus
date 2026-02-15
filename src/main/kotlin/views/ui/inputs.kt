package dev.gemmabcr.views.ui

import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.select

import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.onChange
import kotlinx.html.style

fun DIV.selectInput(
    label: String,
    name: String,
    options: List<String>,
    value: String? = null,
    onChange: String? = null
) {
    column {
        label {
            style = "font-size: 0.85rem; color: #666; margin-bottom: 0.2rem; font-weight: 500;"
            +label
        }
        select {
            this.name = name
            this.onChange = onChange ?: ""
            style =
                "padding: 0.5rem; border: 1px solid #ddd; border-radius: 8px; background-color: white; font-family: inherit; cursor: pointer; min-width: 150px;"
            options.forEach {
                option {
                    selected = value == it
                    +it
                }
            }
        }
    }
}

fun DIV.numberInput(label: String, name: String, value: String? = null, onChange: String? = null) {
    input(label, name, InputType.number, value, onChange)
}

fun DIV.textInput(label: String, name: String, value: String? = null, onChange: String? = null) {
    input(label, name, InputType.text, value, onChange)
}

private fun DIV.input(label: String, name: String, type: InputType, value: String?, onChange: String?) {
    column {
        label {
            style = "font-size: 0.85rem; color: #666; margin-bottom: 0.2rem; font-weight: 500;"
            +label
        }
        input(type = type, name = name) {
            this.value = value ?: ""
            this.onChange = onChange ?: ""
            style =
                "padding: 0.5rem; border: 1px solid #ddd; border-radius: 8px; font-family: inherit; transition: border-color 0.2s;"
            attributes["onfocus"] = "this.style.borderColor='${Colors.CREAM}'"
            attributes["onblur"] = "this.style.borderColor='#ddd'"
        }
    }
}
