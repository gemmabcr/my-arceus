package dev.gemmabcr.models

data class Pagination(
    val page: Int = 1,
    val pageSize: Int = 20,
) {
    val offset: Long
        get() = ((page - 1) * pageSize).toLong()
}
