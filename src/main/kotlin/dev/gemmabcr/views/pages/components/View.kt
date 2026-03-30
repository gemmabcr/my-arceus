package dev.gemmabcr.views.pages.components

import dev.gemmabcr.views.i18n.I18n
import dev.gemmabcr.views.i18n.I18nKey
import kotlinx.html.FlowContent

interface View {
    fun create(content: FlowContent): FlowContent

    fun translate(key: I18nKey): String = I18n.getMessage(key)
}
