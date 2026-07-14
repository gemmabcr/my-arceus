package dev.gemmabcr.views.i18n

import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle

object I18n {
    private val defaultLocale = Locale.ENGLISH
    private val supportedLanguages = setOf("en", "ca", "es")
    private val threadLocalLocale = ThreadLocal<Locale>()

    fun setLocale(locale: Locale) {
        threadLocalLocale.set(locale)
    }

    fun getLocale(): Locale = threadLocalLocale.get() ?: defaultLocale

    fun getMessage(i18nKey: I18nKey): String {
        val key = i18nKey.key()
        return try {
            val requestedLanguage = getLocale().language
            val language = requestedLanguage.takeIf { it in supportedLanguages } ?: defaultLocale.language
            val bundle = ResourceBundle.getBundle("messages_$language", Locale.ROOT)
            bundle.getString(key)
        } catch (e: MissingResourceException) {
            print("not found $e")
            key
        }
    }
}
