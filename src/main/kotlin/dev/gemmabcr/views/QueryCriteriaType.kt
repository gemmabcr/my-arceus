package dev.gemmabcr.views

enum class QueryCriteriaType {
    AREA,
    NAME,
    NUMBER,
    PAGE,
    TO_DO;

    fun key(): String = this.name.lowercase()
}
