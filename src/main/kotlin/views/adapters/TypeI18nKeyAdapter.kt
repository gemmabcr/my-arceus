package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.Type
import dev.gemmabcr.views.i18n.TypeI18nKey

class TypeI18nKeyAdapter(private val type: Type): I18nKeyAdapter {
    override fun i18nKey(): TypeI18nKey = when (type) {
        Type.BUG -> TypeI18nKey.BUG
        Type.DARK -> TypeI18nKey.DARK
        Type.DRAGON -> TypeI18nKey.DRAGON
        Type.ELECTRIC -> TypeI18nKey.ELECTRIC
        Type.FAIRY -> TypeI18nKey.FAIRY
        Type.FIGHTING -> TypeI18nKey.FIGHTING
        Type.FIRE -> TypeI18nKey.FIRE
        Type.FLYING -> TypeI18nKey.FLYING
        Type.GHOST -> TypeI18nKey.GHOST
        Type.GRASS -> TypeI18nKey.GRASS
        Type.GROUND -> TypeI18nKey.GROUND
        Type.ICE -> TypeI18nKey.ICE
        Type.NORMAL -> TypeI18nKey.NORMAL
        Type.POISON -> TypeI18nKey.POISON
        Type.PSYCHIC -> TypeI18nKey.PSYCHIC
        Type.ROCK -> TypeI18nKey.ROCK
        Type.STEEL -> TypeI18nKey.STEEL
        Type.WATER -> TypeI18nKey.WATER
    }
}
