package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.Attack
import dev.gemmabcr.views.i18n.AttackI18nKey
import dev.gemmabcr.views.i18n.I18nKey

class AttackI18nKeyAdapter(private val type: Attack) :
    I18nKeyAdapter {
    override fun i18nKey(): I18nKey = AttackI18nKey.valueOf(type.name)
}
