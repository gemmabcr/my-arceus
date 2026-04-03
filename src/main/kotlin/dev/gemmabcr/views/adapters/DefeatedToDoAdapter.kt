package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.DefeatedToDoType
import dev.gemmabcr.views.i18n.CommonI18nKey

class DefeatedToDoAdapter(private val toDo: DefeatedToDoType) : BaseToDoAdapter() {
    override fun baseI18nKey() = CommonI18nKey.BASE_DEFEATED
    override fun adapter() = toDo.condition?.let {
        TypeI18nKeyAdapter(
            toDo.condition
        )
    }
}
