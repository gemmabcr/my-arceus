package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.views.i18n.AreaI18nKey
import dev.gemmabcr.views.i18n.I18nKey

class AreaI18nKeyAdapter(private val type: Area): I18nKeyAdapter {
    override fun i18nKey(): I18nKey = when (type) {
        Area.COASTLANDS -> AreaI18nKey.COASTLANDS
        Area.DISTORTION -> AreaI18nKey.DISTORTION
        Area.FIELDLANDS -> AreaI18nKey.FIELDLANDS
        Area.HIGHLANDS -> AreaI18nKey.HIGHLANDS
        Area.ICELANDS -> AreaI18nKey.ICELANDS
        Area.MIRELANDS -> AreaI18nKey.MIRELANDS
    }
}
