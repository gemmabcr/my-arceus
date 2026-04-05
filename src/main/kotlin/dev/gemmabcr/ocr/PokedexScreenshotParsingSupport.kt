package dev.gemmabcr.ocr

import java.text.Normalizer

private const val MIN_POKEMON_NAME_LENGTH = 4
private const val NO_INVALID_CHARS = 0
private const val NUMBER_GROUP_INDEX = 1
private const val SHORT_WORD_LENGTH = 2

internal fun sidebarLines(sidebarText: String): List<String> =
    sidebarText.lines().map { it.trim() }.filter { it.isNotBlank() }

internal fun normalizeOcrText(text: String): String =
    Normalizer.normalize(text, Normalizer.Form.NFD)
        .replace(Regex("""\p{M}+"""), "")
        .uppercase()
        .replace('’', '\'')
        .replace('°', ' ')
        .replace('º', ' ')

internal fun toTitleCase(value: String): String =
    value.lowercase().split(" ").filter { it.isNotBlank() }.joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }

internal fun sanitizePokemonName(value: String): String {
    val words = value.split(" ").filter { it.isNotBlank() }
    val cleanedWords = words.dropWhile { word ->
        word.length <= SHORT_WORD_LENGTH && word.all { it.isUpperCase() }
    }
    return cleanedWords.ifEmpty { words }.joinToString(" ")
}

internal fun extractPokedexNumber(value: String): Int? =
    Regex("""N\D{0,4}(\d{1,3})""")
        .find(normalizeOcrText(value))
        ?.groupValues
        ?.getOrNull(NUMBER_GROUP_INDEX)
        ?.toIntOrNull()

internal fun looksLikePokemonName(value: String): Boolean {
    val trimmed = value.trim()
    val lettersOnly = trimmed.filter { it.isLetter() }
    val invalidChars = trimmed.count {
        !(it.isLetter() || it == ' ' || it == '.' || it == '\'' || it == '-')
    }
    return lettersOnly.length >= MIN_POKEMON_NAME_LENGTH && invalidChars == NO_INVALID_CHARS
}

internal fun looksLikePokedexNumberLine(value: String): Boolean =
    normalizeOcrText(value).contains("N") && value.any(Char::isDigit)
