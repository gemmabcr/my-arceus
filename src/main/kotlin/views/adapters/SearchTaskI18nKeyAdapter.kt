package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.SearchTask
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.i18n.SearchTaskI18Key

class SearchTaskI18nKeyAdapter(private val type: SearchTask): I18nKeyAdapter {
    override fun i18nKey(): I18nKey = when (type) {
        SearchTask.BIDOOF -> SearchTaskI18Key.BIDOOF
        SearchTask.BLISSEY -> SearchTaskI18Key.BLISSEY
        SearchTask.CHIMECHO -> SearchTaskI18Key.CHIMECHO
        SearchTask.CLEFAIRY -> SearchTaskI18Key.CLEFAIRY
        SearchTask.COMBEE -> SearchTaskI18Key.COMBEE
        SearchTask.CROAGUNK -> SearchTaskI18Key.CROAGUNK
        SearchTask.DRIFLOON -> SearchTaskI18Key.DRIFLOON
        SearchTask.EEVEE -> SearchTaskI18Key.EEVEE
        SearchTask.MR_MIME -> SearchTaskI18Key.MR_MIME
        SearchTask.NOSEPASS -> SearchTaskI18Key.NOSEPASS
        SearchTask.PACHIRISU -> SearchTaskI18Key.PACHIRISU
        SearchTask.PARASECT -> SearchTaskI18Key.PARASECT
        SearchTask.PONYTA -> SearchTaskI18Key.PONYTA
        SearchTask.SILCOON_CASCOON -> SearchTaskI18Key.SILCOON_CASCOON
        SearchTask.SUDOWOODO -> SearchTaskI18Key.SUDOWOODO
        SearchTask.SWINUB -> SearchTaskI18Key.SWINUB
        SearchTask.VULPIX -> SearchTaskI18Key.VULPIX
        SearchTask.ZUBAT -> SearchTaskI18Key.ZUBAT
    }
}
