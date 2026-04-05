package dev.gemmabcr.ocr

class PokedexScreenshotExtractor {
    fun extract(
        titleText: String,
        tasksText: String,
        sidebarText: String,
        selectedSidebarText: String,
        progressText: String
    ): PokedexScreenshotOcrResult {
        val selectedSidebarNumber = extractPokedexNumber(selectedSidebarText)
        val pokemonName = parsePokemonName(titleText, selectedSidebarText, sidebarText, selectedSidebarNumber)
        val pokemonNumber = parsePokemonNumber(selectedSidebarText, sidebarText, pokemonName)
        val progressLevel = parseProgress(progressText)
        val tasks = parseTasks(tasksText)

        val warnings = buildList {
            if (pokemonName == null) add("No s'ha pogut identificar clarament el nom del Pokémon.")
            if (pokemonNumber == null) add("No s'ha pogut identificar el número de la Pokédex.")
            if (progressLevel == null) add("No s'ha pogut llegir el nivell de progrés.")
            if (tasks.isEmpty()) add("No s'han pogut extreure tasques de la captura.")
        }

        return PokedexScreenshotOcrResult(
            pokemonName = pokemonName,
            pokemonNumber = pokemonNumber,
            progressLevel = progressLevel,
            tasks = tasks,
            warning = warnings.takeIf { it.isNotEmpty() }?.joinToString(" ")
        )
    }
}

private fun parsePokemonName(
    titleText: String,
    selectedSidebarText: String,
    sidebarText: String,
    selectedSidebarNumber: Int?
): String? = listOfNotNull(
    extractPokemonNameFromTitle(titleText),
    extractPokemonNameFromSidebar(selectedSidebarText),
    selectedSidebarNumber?.let { number -> extractPokemonNameNearNumber(sidebarText, number) },
    extractPokemonNameFromSidebar(sidebarText)
).firstOrNull()

private fun extractPokemonNameFromTitle(titleText: String): String? {
    val titleMatch = Regex("""POKEDEX DE ([A-Z][A-Z .'-]+)""")
        .find(normalizeOcrText(titleText))
        ?.groupValues
        ?.getOrNull(TITLE_NAME_GROUP_INDEX)
        ?.trim()
        ?.takeIf { it.isNotBlank() }

    return titleMatch
        ?.takeIf(::looksLikePokemonName)
        ?.let(::toTitleCase)
        ?.let(::sanitizePokemonName)
}

private fun extractPokemonNameFromSidebar(sidebarText: String): String? =
    sidebarLines(sidebarText)
        .let { lines ->
            lines.drop((lines.indexOfFirst(::looksLikePokedexNumberLine) + NEXT_LINE_OFFSET).coerceAtLeast(0))
                .firstOrNull(::looksLikePokemonName)
        }
        ?.trim()
        ?.let(::sanitizePokemonName)

private fun extractPokemonNameNearNumber(sidebarText: String, pokedexNumber: Int): String? {
    val lines = sidebarLines(sidebarText)
    val targetNumber = pokedexNumber.toString().padStart(THREE_DIGITS, '0')
    val numberIndex = lines.indexOfFirst { line ->
        normalizeOcrText(line).contains(targetNumber) || extractPokedexNumber(line) == pokedexNumber
    }

    return lines
        .drop((numberIndex + NEXT_LINE_OFFSET).coerceAtLeast(0))
        .firstOrNull(::looksLikePokemonName)
        ?.trim()
        ?.let(::sanitizePokemonName)
}

private fun parsePokemonNumber(
    selectedSidebarText: String,
    sidebarText: String,
    pokemonName: String?
): Int? =
    extractPokedexNumber(selectedSidebarText)
        ?: extractNumberNearPokemonName(sidebarText, pokemonName)
        ?: sidebarLines(sidebarText).firstNotNullOfOrNull(::extractPokedexNumber)

private fun extractNumberNearPokemonName(sidebarText: String, pokemonName: String?): Int? {
    val normalizedName = pokemonName?.let(::normalizeOcrText) ?: return null
    val sidebarLines = sidebarLines(sidebarText)
    val matchingIndex = sidebarLines.indexOfFirst { normalizeOcrText(it).contains(normalizedName) }
    val nearbyLines = listOfNotNull(
        sidebarLines.getOrNull(matchingIndex - PREVIOUS_LINE_OFFSET),
        sidebarLines.getOrNull(matchingIndex),
        sidebarLines.getOrNull(matchingIndex + NEXT_LINE_OFFSET)
    )
    return nearbyLines.firstNotNullOfOrNull(::extractPokedexNumber)
}

private fun parseProgress(progressText: String): Int? {
    val normalized = normalizeOcrText(progressText)
    return listOf(
        Regex("""COMPLETADO\D{0,4}(\d{1,2})"""),
        Regex("""PROGRESO\D{0,10}(\d{1,2})"""),
        Regex("""\b(\d{1,2})\b""")
    ).firstNotNullOfOrNull { regex ->
        regex.find(normalized)
            ?.groupValues
            ?.getOrNull(NUMBER_GROUP_INDEX)
            ?.toIntOrNull()
    }
}

private fun parseTasks(tasksText: String): List<PokedexTaskProgress> =
    tasksText.lines()
        .map { it.trim() }
        .mapNotNull(::toTaskProgress)
        .distinctBy { it.label }

private fun toTaskProgress(line: String): PokedexTaskProgress? {
    val normalized = normalizeOcrText(line)
    val firstNumber = Regex("""\b\d{1,3}\b""").find(normalized) ?: return null
    val label = line.substringBefore(firstNumber.value).trim().trim('|', ':', '.', ',', ';')
    val cleanedLabel = label.replace(Regex("""^[^A-Za-zÀ-ÿ]+"""), "").trim()
    return when {
        cleanedLabel.length < MIN_TASK_LABEL_LENGTH -> null
        cleanedLabel.startsWith("Image too small", ignoreCase = true) -> null
        cleanedLabel.startsWith("Entradas", ignoreCase = true) -> null
        cleanedLabel.startsWith("Tareas", ignoreCase = true) -> null
        else -> PokedexTaskProgress(cleanedLabel, firstNumber.value.toInt())
    }
}

private const val MIN_TASK_LABEL_LENGTH = 8
private const val THREE_DIGITS = 3
private const val TITLE_NAME_GROUP_INDEX = 1
private const val NUMBER_GROUP_INDEX = 1
private const val NEXT_LINE_OFFSET = 1
private const val PREVIOUS_LINE_OFFSET = 1
