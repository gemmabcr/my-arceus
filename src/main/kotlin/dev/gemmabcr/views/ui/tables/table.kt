package dev.gemmabcr.views.ui.tables

import dev.gemmabcr.views.ui.Colors
import kotlinx.html.DIV
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr

fun DIV.table(config: TableConfig) {
    table {
        tbody {
            tr {
                style = "background-color: ${Colors.BLUE_GREY}"
                config.headers.forEach {
                    th {
                        style = "color: ${Colors.DARKEST_BLUE}"
                        +it
                    }
                }
            }
            config.rows.forEach { row ->
                tr {
                    style = "background-color: ${row.background}"
                    row.cells.forEach {
                        td { +it }
                    }
                }
            }
        }
    }
}
