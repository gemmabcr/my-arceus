package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.CaughtToDoType
import dev.gemmabcr.views.i18n.CaughtConditionI18nKey

class CaughtToDoAdapter(private val toDo: CaughtToDoType) : BaseToDoAdapter() {
    override fun baseI18nKey() = CaughtConditionI18nKey.BASE

    override fun adapter(): I18nKeyAdapter? = toDo.condition?.let {
        CaughtConditionI18nKeyAdapter(it)
    }
}
