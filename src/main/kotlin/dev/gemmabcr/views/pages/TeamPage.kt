package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.MenuItem
import dev.gemmabcr.views.ui.TeamContent
import kotlinx.html.DIV

class TeamPage(
    private val team: List<Pokemon>,
    private val session: Session,
) : HtmlLayout(CommonI18nKey.MY_TEAM, session, activeMenuItem = MenuItem.MY_TEAM) {
    override fun DIV.content() {
        TeamContent(team, session.user != null).create(this)
    }
}
