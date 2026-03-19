package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.SpecificToDo

class SpecificToDoAdapter(private val toDo: SpecificToDo) : BaseToDoAdapter() {
    override fun adapter() = SpecificToDoI18nKeyAdapter(toDo.condition)
}
