package dev.gemmabcr.views.i18n

enum class CaughtConditionI18nKey : I18nKey {
    AFTERNOON,
    ALPHA,
    AIR,
    BASE,
    HEAVY,
    DAY_TIME,
    LARGE,
    LIGHT,
    NIGHT_TIME,
    NOT_FOGGY_WEATHER,
    SMALL,
    SLEEPING,
    WITHOUT_DETECTED;

    override fun itemName() = "caught_$name"
}
