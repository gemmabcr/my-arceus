package dev.gemmabcr.views.pages.components

import kotlinx.html.FlowContent
import kotlinx.html.img
import kotlinx.html.style

fun FlowContent.pokemonImage(id: Int) {
    img(src = url(id)) {
        style = "height: 120px; width: 120px;"
    }
}

private fun url(generalId: Int): String {
    val base = "https://raw.githubusercontent.com/HybridShivam/Pokemon/master/assets/images/"
    val number = generalId.toString().padStart(3, '0')
    return "$base$number${maybeIsHisui(generalId)}.png"
}

private fun maybeIsHisui(generalId: Int): String {
    val isHisui = setOf(58, 59, 100, 101, 157, 549, 570, 571, 628, 705, 706, 713, 724)
    return when (isHisui.contains(generalId)) {
        true -> "-Hisui"
        false -> ""
    }
}
