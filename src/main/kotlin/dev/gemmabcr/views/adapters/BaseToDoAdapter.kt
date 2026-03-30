package dev.gemmabcr.views.adapters

import dev.gemmabcr.views.i18n.I18n
import dev.gemmabcr.views.i18n.I18nKey

abstract class BaseToDoAdapter {
    protected open fun baseI18nKey(): I18nKey? = null
    protected open fun adapter(): I18nKeyAdapter? = null
    protected fun translate(key: I18nKey): String = I18n.getMessage(key)
    fun text(): String = "${base()} ${condition()}"

    private fun base(): String = when (val base = baseI18nKey()) {
        null -> ""
        else -> translate(base)
    }

    private fun condition(): String = when (val adapter = adapter()) {
        null -> ""
        else -> translate(adapter.i18nKey())
    }
}
