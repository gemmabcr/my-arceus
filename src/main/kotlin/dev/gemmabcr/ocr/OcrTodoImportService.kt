package dev.gemmabcr.ocr

import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.models.Pagination
import dev.gemmabcr.models.PokemonDao
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.UserDao
import dev.gemmabcr.models.pokemons.todo.ProgressToDo
import dev.gemmabcr.views.adapters.ToDoTypeAdapter
import dev.gemmabcr.views.i18n.I18n
import java.text.Normalizer
import java.util.Locale

class OcrTodoImportService(
    private val pokemonDao: PokemonDao,
    private val userDao: UserDao
) {
    suspend fun buildPreview(
        result: PokedexScreenshotOcrResult,
        session: Session
    ): OcrTodoImportPreview {
        val pokemon = resolvePokemon(result, session)
        val matchedToDos = pokemon?.let { matchExtractedTasks(toProgressToDos(it.toDos), result.tasks) } ?: emptyList()
        val matchedLabels = matchedToDos.map { normalizeText(it.extractedLabel) }.toSet()
        val unmatchedTasks = result.tasks.filterNot { task -> matchedLabels.contains(normalizeText(task.label)) }

        return OcrTodoImportPreview(
            pokemonId = pokemon?.id,
            pokemonName = pokemon?.name ?: result.pokemonName,
            matchedToDos = matchedToDos,
            unmatchedTasks = unmatchedTasks
        )
    }

    suspend fun importToDos(
        pokemonId: Int,
        extractedToDos: List<OcrMatchedToDo>,
        session: Session
    ) {
        val userId = session.user ?: return
        extractedToDos.forEach { todo ->
            userDao.saveTodoProgress(userId, pokemonId, todo.todoId, todo.done)
        }
    }

    private suspend fun resolvePokemon(
        result: PokedexScreenshotOcrResult,
        session: Session
    ) = criteriaCandidates(result).firstNotNullOfOrNull { criteria ->
        pokemonDao.pokemons(criteria, session.user).results.firstOrNull { pokemon ->
            val numberMatches = result.pokemonNumber?.let { pokemon.id == it } ?: true
            val nameMatches = result.pokemonName?.let { normalizeText(pokemon.name) == normalizeText(it) } ?: true
            numberMatches && nameMatches || result.pokemonNumber == pokemon.id
        }?.let { pokemonDao.pokemon(it.id) }
    }

    private fun criteriaCandidates(result: PokedexScreenshotOcrResult) = listOf(
        QueryCriteria(
            name = result.pokemonName,
            number = result.pokemonNumber,
            pagination = Pagination(pageSize = OCR_POKEMON_SEARCH_LIMIT)
        ),
        QueryCriteria(number = result.pokemonNumber, pagination = Pagination(pageSize = OCR_POKEMON_SEARCH_LIMIT)),
        QueryCriteria(name = result.pokemonName, pagination = Pagination(pageSize = OCR_POKEMON_SEARCH_LIMIT))
    ).distinct()

    private fun matchExtractedTasks(
        toDos: List<ProgressToDo>,
        tasks: List<PokedexTaskProgress>
    ): List<OcrMatchedToDo> = tasks.mapNotNull { extractedTask ->
        val match = toDos
            .mapNotNull { toDo ->
                val matchedLabel = buildTodoLabels(toDo).firstOrNull { label ->
                    labelsMatch(label, extractedTask.label)
                }
                matchedLabel?.let { toDo to it }
            }
            .firstOrNull()
            ?: return@mapNotNull null

        OcrMatchedToDo(
            todoId = match.first.id,
            label = ToDoTypeAdapter(match.first.toDoType).text(),
            extractedLabel = extractedTask.label,
            done = extractedTask.value.coerceAtMost(match.first.goal),
            goal = match.first.goal
        )
    }

    private fun buildTodoLabels(toDo: ProgressToDo): Set<String> =
        OCR_MATCHING_LOCALES.map { locale ->
            withLocale(locale) { ToDoTypeAdapter(toDo.toDoType).text() }
        }.map(::normalizeText).toSet()

    private fun labelsMatch(candidate: String, extracted: String): Boolean {
        val normalizedExtracted = normalizeText(extracted)
        return candidate == normalizedExtracted ||
                candidate.contains(normalizedExtracted) ||
                normalizedExtracted.contains(candidate)
    }

    private fun normalizeText(value: String): String =
        Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace(Regex("""\p{M}+"""), "")
            .lowercase()
            .replace(Regex("""[^a-z0-9]+"""), " ")
            .trim()

    private fun <T> withLocale(locale: Locale, block: () -> T): T {
        val currentLocale = I18n.getLocale()
        return try {
            I18n.setLocale(locale)
            block()
        } finally {
            I18n.setLocale(currentLocale)
        }
    }
}

private fun toProgressToDos(toDos: List<ToDoDto>): List<ProgressToDo> =
    toDos.map { ProgressToDo(it.id, it.description, done = 0, goal = it.goal) }

private const val OCR_POKEMON_SEARCH_LIMIT = 5
private val OCR_MATCHING_LOCALES = listOf(Locale.ENGLISH, Locale("ca"))
