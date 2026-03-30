package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.CaughtCondition
import dev.gemmabcr.views.i18n.CaughtConditionI18nKey
import dev.gemmabcr.views.i18n.I18nKey

class CaughtConditionI18nKeyAdapter(private val type: CaughtCondition):
    I18nKeyAdapter {
    override fun i18nKey(): I18nKey = when (type) {
        CaughtCondition.AFTERNOON -> CaughtConditionI18nKey.AFTERNOON
        CaughtCondition.ALPHA -> CaughtConditionI18nKey.ALPHA
        CaughtCondition.AIR -> CaughtConditionI18nKey.AIR
        CaughtCondition.HEAVY -> CaughtConditionI18nKey.HEAVY
        CaughtCondition.DAY_TIME -> CaughtConditionI18nKey.DAY_TIME
        CaughtCondition.LARGE -> CaughtConditionI18nKey.LARGE
        CaughtCondition.LIGHT -> CaughtConditionI18nKey.LIGHT
        CaughtCondition.NIGHT_TIME -> CaughtConditionI18nKey.NIGHT_TIME
        CaughtCondition.NOT_FOGGY_WEATHER -> CaughtConditionI18nKey.NOT_FOGGY_WEATHER
        CaughtCondition.SMALL -> CaughtConditionI18nKey.SMALL
        CaughtCondition.SLEEPING -> CaughtConditionI18nKey.SLEEPING
        CaughtCondition.WITHOUT_DETECTED -> CaughtConditionI18nKey.WITHOUT_DETECTED
    }
}
