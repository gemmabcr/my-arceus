package dev.gemmabcr.views.pages

import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.QueryResult
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.models.pokemons.todo.ToDo
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.MenuItem
import dev.gemmabcr.views.ui.PokemonListContent
import kotlinx.html.DIV

class ListView(
    private val criteria: QueryCriteria,
    private val result: QueryResult<Pokemon>,
    private val todos: List<ToDo>,
    private val team: List<Pokemon>,
    private val redirectTo: String,
    private val session: Session,
) : HtmlLayout(CommonI18nKey.LIST, session, activeMenuItem = MenuItem.POKEDEX) {
    override fun DIV.content() {
        PokemonListContent(
            criteria = criteria,
            result = result,
            todos = todos,
            team = team,
            redirectTo = redirectTo,
            isLoggedIn = session.user != null,
        ).create(this)
    }
}
