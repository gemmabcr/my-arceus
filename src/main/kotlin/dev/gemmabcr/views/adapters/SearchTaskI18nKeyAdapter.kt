package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.SearchTask
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.i18n.SearchTaskI18Key

class SearchTaskI18nKeyAdapter(private val type: SearchTask) :
    I18nKeyAdapter {
    override fun i18nKey(): I18nKey = SearchTaskI18Key.valueOf(type.name)
}
