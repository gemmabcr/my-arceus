package dev.gemmabcr.views.i18n

enum class SearchTaskI18Key: I18nKey {
    BIDOOF,
    BLISSEY,
    CHIMECHO,
    CLEFAIRY,
    COMBEE,
    CROAGUNK,
    DRIFLOON,
    EEVEE,
    MR_MIME,
    NOSEPASS,
    PACHIRISU,
    PARASECT,
    PONYTA,
    SILCOON_CASCOON,
    SUDOWOODO,
    SWINUB,
    VULPIX,
    ZUBAT;

    override fun itemName() = "search_$name"
}
