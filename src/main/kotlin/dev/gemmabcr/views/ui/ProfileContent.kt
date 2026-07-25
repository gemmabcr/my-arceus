package dev.gemmabcr.views.ui

import dev.gemmabcr.models.UserProfile
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.p
import kotlinx.html.style

class ProfileContent(private val profile: UserProfile) : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        column(gap = Gap.MAX) {
            column(gap = Gap.MIN, style = "padding: 1.25rem;") {
                classes = setOf("ui-card")
                p {
                    style = "margin: 0; font-weight: 700;"
                    +"Email: ${profile.email}"
                }
                p {
                    classes = setOf("ui-muted")
                    style = "margin: 0;"
                    +"User id: ${profile.id}"
                }
            }
            buttonLink("/pokemons", "Tornar a la Pokedex")
        }
    }
}
