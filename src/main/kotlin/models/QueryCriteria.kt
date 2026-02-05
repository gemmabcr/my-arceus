package dev.gemmabcr.models

data class QueryCriteria(
    val name: String? = null,
    val number: Int? = null,
    val area: Area? = null,
) {
    fun isFiltered(): Boolean = name.isNullOrBlank().not() || number != null || area != null
}
