package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.ui.AuthContent
import dev.gemmabcr.views.ui.AuthMode
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.MenuItem
import kotlinx.html.DIV

class LoginPage(
    private val mode: AuthMode = AuthMode.LOGIN,
    private val error: I18nKey? = null,
    private val googleEnabled: Boolean = false,
    private val appleEnabled: Boolean = false,
    session: Session = Session(),
) : HtmlLayout(
    if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN else CommonI18nKey.CREATE_ACCOUNT,
    session,
    false,
    MenuItem.LOGIN,
) {
    override fun DIV.content() {
        AuthContent(mode, error, googleEnabled, appleEnabled).create(this)
    }
}
