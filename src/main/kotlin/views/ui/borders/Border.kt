package dev.gemmabcr.views.ui.borders

import dev.gemmabcr.views.ui.Colors

data class Border(
    val color: Colors,
    val value: Int = 1,
    val type: String = "solid",
    val radius: BorderRadius? = null
) {
    fun text(): String = "border: ${value}px $type $color;${maybeBorderRadius()}"

    private fun maybeBorderRadius(): String = when (radius) {
        null -> ""
        else -> radius.text()
    }
}
