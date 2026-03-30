package dev.gemmabcr.views.i18n

enum class SpecificToDoI18nKey : I18nKey {
    AGILE,
    ARCEUS,
    EVOLVED,
    EXHAUSTED,
    FED,
    FORM,
    ROCKS,
    STARTLED,
    STRONG,
    TREES;

    override fun itemName() = "specific_todo_$name"
}
