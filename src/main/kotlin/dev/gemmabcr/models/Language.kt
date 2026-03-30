package dev.gemmabcr.models

enum class Language {
    EN,
    CA;

    fun href(): String = "/lang/${name.lowercase()}"
}
