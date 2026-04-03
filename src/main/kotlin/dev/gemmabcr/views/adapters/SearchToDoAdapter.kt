package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.SearchToDoType
import dev.gemmabcr.views.i18n.CommonI18nKey

class SearchToDoAdapter(private val toDo: SearchToDoType) : BaseToDoAdapter() {
    override fun baseI18nKey() = CommonI18nKey.BASE_SEARCH
    override fun adapter() = SearchTaskI18nKeyAdapter(toDo.condition)
}
