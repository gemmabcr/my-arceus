package dev.gemmabcr.views.ui

import kotlinx.html.FlowContent
import kotlinx.html.img

fun FlowContent.pokemonImage(id: Int) {
    img(src = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png")
}
