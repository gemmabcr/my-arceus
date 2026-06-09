package dev.gemmabcr.views

enum class QueryCriteriaType {
    AREA,
    NAME,
    NUMBER,
    PAGE,
    TO_DO,
    TYPE,
    TEAM,
    COMPLETION;

    fun key(): String = this.name.lowercase()
}
