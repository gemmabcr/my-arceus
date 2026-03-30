package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.SpecificToDoType
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.i18n.SpecificToDoI18nKey

class SpecificToDoI18nKeyAdapter(private val type: SpecificToDoType):
    I18nKeyAdapter {
    override fun i18nKey(): I18nKey = when (type) {
        SpecificToDoType.AGILE -> SpecificToDoI18nKey.AGILE
        SpecificToDoType.ARCEUS -> SpecificToDoI18nKey.ARCEUS
        SpecificToDoType.EVOLVED -> SpecificToDoI18nKey.EVOLVED
        SpecificToDoType.EXHAUSTED -> SpecificToDoI18nKey.EXHAUSTED
        SpecificToDoType.FED -> SpecificToDoI18nKey.FED
        SpecificToDoType.FORM -> SpecificToDoI18nKey.FORM
        SpecificToDoType.ROCKS -> SpecificToDoI18nKey.ROCKS
        SpecificToDoType.STARTLED -> SpecificToDoI18nKey.STARTLED
        SpecificToDoType.STRONG -> SpecificToDoI18nKey.STRONG
        SpecificToDoType.TREES -> SpecificToDoI18nKey.TREES
    }
}
