package dev.gemmabcr.models

enum class Language(val tag: String, val label: String, val flagPath: String) {
    EN("en", "English", "/icons/flags/en.svg"),
    CA("ca", "Català", "/icons/flags/ca.svg"),
    ES("es", "Español", "/icons/flags/es.svg");

    fun href(): String = "/lang/$tag"

    companion object {
        fun fromTag(tag: String?): Language? {
            val baseTag = tag?.lowercase()?.substringBefore('-')
            return entries.firstOrNull { it.tag == baseTag }
        }
    }
}
