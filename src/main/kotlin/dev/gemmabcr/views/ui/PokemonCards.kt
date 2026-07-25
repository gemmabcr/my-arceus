package dev.gemmabcr.views.ui

import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.button as actionButton
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.row
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.form
import kotlinx.html.hiddenInput
import kotlinx.html.style

class PokemonCards(
    private val pokemons: List<Pokemon>,
    private val teamSize: Int,
    private val redirectTo: String,
    private val canManageTeam: Boolean,
) : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        pokemons.forEach { pokemon ->
            PokemonCard(pokemon, canManageTeam).with {
                row(JustifyContent.CENTER, style = "padding: 1rem;") {
                    if (canManageTeam) teamButton(pokemon)
                    buttonLink(
                        "/pokemons/${pokemon.hisuiId}",
                        translate(CommonI18nKey.MORE_INFO),
                    )
                }
            }.create(this)
        }
    }

    private fun FlowContent.teamButton(pokemon: Pokemon) {
        form(action = "/team", method = FormMethod.post) {
            style = "margin: 0;"
            hiddenInput(name = "pokemonId") { value = pokemon.hisuiId.toString() }
            hiddenInput(name = "redirectTo") { value = this@PokemonCards.redirectTo }
            hiddenInput(name = "action") { value = if (pokemon.inTeam) "remove" else "add" }
            val disabled = teamSize >= TEAM_SIZE && pokemon.inTeam.not()
            actionButton(
                text = translate(if (pokemon.inTeam) CommonI18nKey.REMOVE_FROM_TEAM else CommonI18nKey.ADD_TO_TEAM),
                type = ButtonType.submit,
                style = if (disabled) "opacity: 0.5; cursor: not-allowed;" else null,
            ) {
                this.disabled = disabled
            }
        }
    }
}

private const val TEAM_SIZE = 6
