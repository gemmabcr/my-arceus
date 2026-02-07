package dev.gemmabcr.views.ui

import kotlinx.html.FlowContent
import kotlinx.html.img
import kotlinx.html.style

fun FlowContent.pokemonImage(id: Int) {
    img(src = url(id)) {
        style = "background: #fff; border-radius: 50%;"
    }
}

private fun url(generalId: Int): String {
    val base = "https://raw.githubusercontent.com/HybridShivam/Pokemon/master/assets/images/"
    val number = "%03d".format(generalId.toString())
    return "$base$number${maybeIsHisui(generalId)}.png"
}

private fun maybeIsHisui(generalId: Int): String {
    val isHisui = setOf(58, 59, 100, 101, 157, 549, 570, 571, 628, 705, 706, 713, 724)
    return when (isHisui.contains(generalId)) {
        true -> "-Hisui"
        false -> ""
    }
}
