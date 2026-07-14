package views.i18n

import dev.gemmabcr.views.i18n.AreaI18nKey
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18n
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class I18nTest {
    @Test
    fun givenCatalanLocale_whenSwitchingToEnglish_thenLoadsEnglishBundle() {
        try {
            I18n.setLocale(Locale.forLanguageTag("ca"))
            assertEquals("La meva Pokédex Arceus", I18n.getMessage(CommonI18nKey.HEADING))

            I18n.setLocale(Locale.ENGLISH)
            assertEquals("My Pokédex Arceus", I18n.getMessage(CommonI18nKey.HEADING))
            assertEquals("Cobalt Coastlands", I18n.getMessage(AreaI18nKey.COASTLANDS))
        } finally {
            I18n.setLocale(Locale.ENGLISH)
        }
    }
}
