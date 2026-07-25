package dev.gemmabcr.views.ui

import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.p
import kotlinx.html.style

class TeamContent(
    private val team: List<Pokemon>,
    private val isLoggedIn: Boolean,
) : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        column(gap = Gap.MAX, style = "min-width: 0; width: 100%;") {
            when {
                !isLoggedIn -> LoginRequiredNotice(CommonI18nKey.LOGIN_REQUIRED_TEAM).create(this)

                team.isEmpty() -> messageCard(
                    translate(CommonI18nKey.SELECT_TEAM),
                    "/pokemons",
                    translate(CommonI18nKey.LIST),
                )

                else -> PokemonCards(
                    pokemons = team,
                    teamSize = team.size,
                    redirectTo = "/team",
                    canManageTeam = true,
                ).create(this)
            }
        }
    }

    private fun FlowContent.messageCard(message: String, href: String, action: String) {
        column(
            JustifyContent.CENTER,
            AlignItems.CENTER,
            gap = Gap.MIN,
            style = "width: 100%; padding: 2rem;",
        ) {
            classes = setOf("ui-card")
            p {
                style = "margin: 0; color: ${Colors.DARKEST_BLUE}; font-weight: 700; text-align: center;"
                +message
            }
            buttonLink(href, action)
        }
    }
}
