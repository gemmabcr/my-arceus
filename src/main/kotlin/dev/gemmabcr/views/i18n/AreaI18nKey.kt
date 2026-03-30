package dev.gemmabcr.views.i18n

enum class AreaI18nKey : I18nKey {
    COASTLANDS,
    DISTORTION,
    FIELDLANDS,
    HIGHLANDS,
    ICELANDS,
    MIRELANDS;

    override fun itemName() = "area_$name"
}
