package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.Type
import dev.gemmabcr.views.i18n.TypeI18nKey

class TypeI18nKeyAdapter(private val type: Type) : I18nKeyAdapter {
    override fun i18nKey(): TypeI18nKey = TypeI18nKey.valueOf(type.name)
}
