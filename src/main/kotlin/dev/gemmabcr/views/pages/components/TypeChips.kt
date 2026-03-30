package dev.gemmabcr.views.pages.components

import dev.gemmabcr.models.pokemons.Type
import dev.gemmabcr.views.adapters.TypeI18nKeyAdapter
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.row
import kotlinx.html.FlowContent
import kotlinx.html.img
import kotlinx.html.style

class TypeChips(private val types: List<Type>) : View {
    private fun url(type: String) =
        "https://raw.githubusercontent.com/partywhale/pokemon-type-icons/refs/heads/main/icons/$type.svg"

    override fun create(content: FlowContent): FlowContent = content.apply {
        row {
            types.forEach {
                row(
                    JustifyContent.CENTER, style = "background-color: ${Colors.DARKEST_BLUE}; " +
                            "border-radius: 0.25rem; color: ${Colors.ON_DARK_BLUE}; padding: 0.25rem 0.5rem;"
                ) {
                    img(src = url(it.name.lowercase())) {
                        height = "16"
                        width = "16"
                        style = "margin-right: 0.5rem;"
                    }
                    +translate(TypeI18nKeyAdapter(it).i18nKey())
                }
            }
        }
    }
}
