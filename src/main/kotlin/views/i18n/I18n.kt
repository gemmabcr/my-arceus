package dev.gemmabcr.views.i18n

import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle

object I18n {
    private val defaultLocale = Locale.ENGLISH
    private val threadLocalLocale = ThreadLocal<Locale>()

    fun setLocale(locale: Locale) {
        threadLocalLocale.set(locale)
    }

    fun getLocale(): Locale {
        return threadLocalLocale.get() ?: defaultLocale
    }

    fun getMessage(key: I18nKey): String {
        return try {
            val bundle = ResourceBundle.getBundle("messages", getLocale())
            bundle.getString(key.toString())
        } catch (e: MissingResourceException) {
            print("not found $e")
            key.name
        }
    }
}
