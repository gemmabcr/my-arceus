package dev.gemmabcr.views.i18n

interface I18nKey {
    fun itemName(): String
    fun key(): String = itemName().lowercase()
}
