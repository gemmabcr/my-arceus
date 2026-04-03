package dev.gemmabcr.models

data class QueryResult<T>(
    val results: List<T>,
    val hasNextPage: Boolean
)
