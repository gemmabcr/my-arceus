package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.CaughtToDoType
import dev.gemmabcr.views.i18n.CaughtConditionI18nKey
import dev.gemmabcr.views.i18n.I18n

class CaughtToDoAdapter(private val toDo: CaughtToDoType) {
    fun text(): String {
        val i18nKey= toDo.condition?.let {
            CaughtConditionI18nKeyAdapter(it).i18nKey()
        } ?: CaughtConditionI18nKey.BASE
        return I18n.getMessage(i18nKey)
    }
}
