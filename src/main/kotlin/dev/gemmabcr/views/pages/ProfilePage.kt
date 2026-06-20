package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.models.UserProfile
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.DIV
import kotlinx.html.p
import kotlinx.html.style

class ProfilePage(
    private val profile: UserProfile,
    session: Session,
) : HtmlLayout(CommonI18nKey.PROFILE, session) {
    override fun DIV.content() {
        column(gap = Gap.MAX) {
            column(
                gap = Gap.MIN,
                style =
                    "background-color: ${Colors.CREAM_LIGHEST}; padding: 1rem; border-radius: 8px; " +
                            "border: 1px solid ${Colors.CREAM};"
            ) {
                p {
                    style = "margin: 0; color: ${Colors.DARKEST_BLUE};"
                    +"Email: ${profile.email}"
                }
                p {
                    style = "margin: 0; color: ${Colors.DARK_BLUE};"
                    +"User id: ${profile.id}"
                }
            }
            buttonLink("/pokemons", "Tornar a la Pokedex")
        }
    }
}
