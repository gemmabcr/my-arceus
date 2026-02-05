package dev.gemmabcr.views

enum class QueryCriteriaType {
    NAME,
    NUMBER,
    AREA;

    fun key(): String = this.name.lowercase()
}
