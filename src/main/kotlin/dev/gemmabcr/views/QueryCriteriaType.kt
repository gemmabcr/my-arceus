package dev.gemmabcr.views

enum class QueryCriteriaType {
    AREA,
    NAME,
    NUMBER,
    PAGE;

    fun key(): String = this.name.lowercase()
}
