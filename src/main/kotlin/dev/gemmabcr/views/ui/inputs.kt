package dev.gemmabcr.views.ui

import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.checkBoxInput
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.onChange
import kotlinx.html.option
import kotlinx.html.select
import kotlinx.html.style

fun DIV.checkBox(
    label: String,
    name: String,
    value: Boolean,
    onChange: String
) {
    column {
        labelText(label)
        checkBoxInput {
            this.name = name
            this.value = true.toString()
            this.checked = value
            this.onChange = onChange
        }
    }
}

fun DIV.selectInput(
    label: String,
    name: String,
    options: Map<String, String>,
    value: String? = null,
    onChange: String? = null
) {
    column {
        labelText(label)
        select {
            this.name = name
            this.onChange = onChange ?: ""
            style =
                "padding: 0.5rem; border: 1px solid #ddd; border-radius: 8px; " +
                        "background-color: white; font-family: inherit; cursor: pointer; min-width: 150px;"
            options.forEach {
                option {
                    selected = value == it.key
                    this.value = it.key
                    +it.value
                }
            }
        }
    }
}

private fun DIV.labelText(label: String) {
    label {
        style = "font-size: 0.85rem; color: #666; margin-bottom: 0.2rem; font-weight: 500;"
        +label
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
        labelText(label)
        input(type = type, name = name) {
            this.value = value ?: ""
            this.onChange = onChange ?: ""
            style =
                "padding: 0.5rem; border: 1px solid #ddd; border-radius: 8px; " +
                        "font-family: inherit; transition: border-color 0.2s;"
            attributes["onfocus"] = "this.style.borderColor='${Colors.CREAM}'"
            attributes["onblur"] = "this.style.borderColor='#ddd'"
        }
    }
}
