package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.AttackToDo
import dev.gemmabcr.views.i18n.AttackI18nKey

class AttackToDoAdapter(private val toDo: AttackToDo) : BaseToDoAdapter() {
    override fun baseI18nKey() = AttackI18nKey.BASE
    override fun adapter() = AttackI18nKeyAdapter(toDo.condition)
}
