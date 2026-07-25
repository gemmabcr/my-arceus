package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.models.pokemons.DetailedPokemon
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.MenuItem
import dev.gemmabcr.views.ui.PokemonDetailContent
import kotlinx.html.DIV

class DetailView(
    private val pokemon: DetailedPokemon,
    private val session: Session,
) : HtmlLayout(CommonI18nKey.DETAIL, session, activeMenuItem = MenuItem.POKEDEX) {
    override fun DIV.content() {
        PokemonDetailContent(pokemon, session.user != null).create(this)
    }
}
