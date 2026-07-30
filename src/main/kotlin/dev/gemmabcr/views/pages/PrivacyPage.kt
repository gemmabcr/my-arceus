package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.MenuItem
import dev.gemmabcr.views.ui.PrivacyContent
import kotlinx.html.DIV

class PrivacyPage(session: Session) :
    HtmlLayout(CommonI18nKey.PRIVACY_POLICY, session, activeMenuItem = MenuItem.PRIVACY) {
    override fun DIV.content() {
        PrivacyContent().create(this)
    }
}
