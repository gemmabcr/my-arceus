package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.models.UserProfile
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.MenuItem
import dev.gemmabcr.views.ui.ProfileContent
import kotlinx.html.DIV

class ProfilePage(
    private val profile: UserProfile,
    session: Session,
) : HtmlLayout(CommonI18nKey.PROFILE, session, activeMenuItem = MenuItem.PROFILE) {
    override fun DIV.content() {
        ProfileContent(profile).create(this)
    }
}
