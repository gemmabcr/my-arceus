package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.views.ui.pokemonImage
import kotlinx.html.BODY
import kotlinx.html.a
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.p

fun BODY.pokemonView(pokemon: Pokemon) {
    h1 { +"HISUI Pokemon List" }
    h2 { +"${pokemon.hisuiId} - ${pokemon.name}" }
    pokemonImage(pokemon.generalId)
    p { +"locations:  ${pokemon.location}" }
    p { +"to dos:  ${pokemon.toDos}" }
    a(href = "/") {
        +"< Atrás"
    }
}
