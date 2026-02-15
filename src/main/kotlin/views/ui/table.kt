package dev.gemmabcr.views.ui

import kotlinx.html.DIV
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr

fun DIV.table(headers: List<String>, rows: List<List<String>>) {
    table {
        tbody {
            tr { headers.forEach { th { +it } } }
            rows.forEach { row ->
                tr {
                    style = "background-color: #fff"
                    row.forEach {
                        td { +it }
                    }
                }
            }
        }
    }
}
