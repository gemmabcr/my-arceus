package dev.gemmabcr.views.pages

import dev.gemmabcr.models.CompletionFilter
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.QueryResult
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.models.pokemons.Type
import dev.gemmabcr.models.pokemons.todo.ToDo
import dev.gemmabcr.views.QueryCriteriaType
import dev.gemmabcr.views.adapters.AreaI18nKeyAdapter
import dev.gemmabcr.views.adapters.ToDoTypeAdapter
import dev.gemmabcr.views.adapters.TypeI18nKeyAdapter
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.pages.components.PokemonCard
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.FormConfig
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.ImageSource
import dev.gemmabcr.views.ui.button as actionButton
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.checkBox
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.form
import dev.gemmabcr.views.ui.numberInput
import dev.gemmabcr.views.ui.textInput
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.selectInput
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h3
import kotlinx.html.hiddenInput
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.onClick
import kotlinx.html.p
import kotlinx.html.style

class ListView(
    private val criteria: QueryCriteria,
    private val result: QueryResult<Pokemon>,
    private val todos: List<ToDo>,
    private val team: List<Pokemon>,
    private val redirectTo: String,
    session: Session,
) :
    HtmlLayout(CommonI18nKey.LIST, session) {
    private val disableEmptyFieldsScript = "Array.from(this.form.elements)" +
            ".forEach(e=>{if(e.name&&!e.value)e.disabled=true})"
    private val disableEmptyFieldsByFormIdScript =
        "Array.from(document.getElementById('filter-form').elements)" +
                ".forEach(e=>{if(e.name&&!e.value)e.disabled=true})"
    private val isLoggedIn = session.user != null

    override fun DIV.content() {
        val autoSubmit =
            "document.getElementById('page-input').value='1'; " +
                    "$disableEmptyFieldsScript; " +
                    "this.form.submit()"
        div {
            classes = setOf("pokemon-list-layout")
            div {
                classes = setOf("pokemon-filter-sidebar")
                filtersForm(autoSubmit)
            }
            column(gap = Gap.MAX, style = "min-width: 0; width: 100%;") {
                if (isLoggedIn) {
                    row(style = "flex-wrap: wrap;") {
                        buttonLink("/ocr", translate(CommonI18nKey.UPLOAD_PROGRESS))
                    }
                }
                if (isLoggedIn) {
                    teamSection()
                }
                val pokemons = result.results
                when {
                    pokemons.isEmpty() -> row(JustifyContent.CENTER) {
                        p { +translate(CommonI18nKey.NO_RESULTS) }
                        img(src = ImageSource.NO_RESULT.url) {
                            height = "120"
                            width = "120"
                        }
                    }

                    else -> {
                        pagination()
                        result.results.forEach { pokemon ->
                            PokemonCard(pokemon).with {
                                row(JustifyContent.CENTER, style = "padding: 1rem;") {
                                    if (isLoggedIn) {
                                        teamButton(pokemon)
                                    }
                                    buttonLink(
                                        "/pokemons/${pokemon.hisuiId}",
                                        translate(CommonI18nKey.MORE_INFO)
                                    )
                                }
                            }.create(this)
                        }
                        pagination()
                    }
                }
            }
        }
    }

    private fun DIV.teamSection() {
        column(
            gap = Gap.MIN,
            style =
                "background-color: white; padding: 1rem; border-radius: 8px; " +
                        "border: 1px solid ${Colors.CREAM}; box-shadow: rgba(0, 0, 0, 0.08) 0 1px 3px;"
        ) {
            id = "my-team"
            h3 {
                style = "margin: 0; color: ${Colors.DARK_BLUE};"
                +"${translate(CommonI18nKey.MY_TEAM)} (${team.size}/6)"
            }
            when {
                team.isEmpty() -> p {
                    style = "margin: 0; color: ${Colors.DARK_BLUE};"
                    +translate(CommonI18nKey.SELECT_TEAM)
                }

                else -> row(style = "flex-wrap: wrap;") {
                    team.forEach { pokemon ->
                        buttonLink("/pokemons/${pokemon.hisuiId}", "#${pokemon.hisuiId} ${pokemon.name}")
                    }
                }
            }
        }
    }

    private fun FlowContent.teamButton(pokemon: Pokemon) {
        form(action = "/team", method = FormMethod.post) {
            style = "margin: 0;"
            hiddenInput(name = "pokemonId") { this.value = pokemon.hisuiId.toString() }
            hiddenInput(name = "redirectTo") { this.value = redirectTo }
            hiddenInput(name = "action") { this.value = if (pokemon.inTeam) "remove" else "add" }
            val disabled = team.size >= TEAM_SIZE && pokemon.inTeam.not()
            val styleButton = if (disabled) "opacity: 0.5; cursor: not-allowed;" else null
            actionButton(
                text = translate(if (pokemon.inTeam) CommonI18nKey.REMOVE_FROM_TEAM else CommonI18nKey.ADD_TO_TEAM),
                type = ButtonType.submit,
                style = styleButton
            ) {
                this.disabled = disabled
            }
        }
    }

    private fun DIV.filtersForm(autoSubmit: String) {
        form(
            FormConfig(
                action = "/pokemons",
                method = FormMethod.get,
                submitText = translate(CommonI18nKey.FILTER),
                id = "filter-form",
                onSubmit = "Array.from(this.elements).forEach(e=>{if(e.name&&!e.value)e.disabled=true})"
            )
        ) {
            input(name = QueryCriteriaType.PAGE.key()) {
                id = "page-input"
                style = "display: none;"
                value = criteria.pagination.page.toString()
            }
            div {
                classes = setOf("pokemon-filter-fields")
                inputs(autoSubmit)
            }
        }
    }

    private fun DIV.inputs(autoSubmit: String) {
        selectInput(
            translate(CommonI18nKey.AREA),
            QueryCriteriaType.AREA.key(),
            areaOptions(),
            value = criteria.area?.name ?: "",
            onChange = autoSubmit
        )
        selectInput(
            translate(CommonI18nKey.TYPE),
            QueryCriteriaType.TYPE.key(),
            typeOptions(),
            value = criteria.type?.name ?: "",
            onChange = autoSubmit
        )
        textInput(
            translate(CommonI18nKey.NAME),
            QueryCriteriaType.NAME.key(),
            value = criteria.name,
            onChange = autoSubmit
        )
        numberInput(
            translate(CommonI18nKey.NUMBER),
            QueryCriteriaType.NUMBER.key(),
            criteria.number?.toString(),
            onChange = autoSubmit
        )
        selectInput(
            translate(CommonI18nKey.TODOS),
            QueryCriteriaType.TO_DO.key(),
            toDoOptions(),
            value = criteria.toDo?.id?.toString() ?: "",
            onChange = autoSubmit
        )
        selectInput(
            translate(CommonI18nKey.COMPLETION),
            QueryCriteriaType.COMPLETION.key(),
            completionOptions(),
            value = criteria.completion.name,
            onChange = autoSubmit
        )
        if (isLoggedIn) {
            checkBox(
                translate(CommonI18nKey.ONLY_MY_TEAM),
                QueryCriteriaType.TEAM.key(),
                criteria.onlyTeam,
                autoSubmit
            )
        }
    }

    private fun areaOptions(): Map<String, String> =
        mapOf("" to translate(CommonI18nKey.ALL)) + Area.entries.associate {
            it.name to translate(AreaI18nKeyAdapter(it).i18nKey())
        }

    private fun typeOptions(): Map<String, String> =
        mapOf("" to translate(CommonI18nKey.ALL)) + Type.entries.associate {
            it.name to translate(TypeI18nKeyAdapter(it).i18nKey())
        }

    private fun toDoOptions(): Map<String, String> =
        mapOf("" to "") + todos.associate { it.id.toString() to ToDoTypeAdapter(it.description).text() }

    private fun completionOptions(): Map<String, String> = mapOf(
        CompletionFilter.ALL.name to translate(CommonI18nKey.ALL),
        CompletionFilter.UNCOMPLETED.name to translate(CommonI18nKey.UNCOMPLETED),
        CompletionFilter.COMPLETED.name to translate(CommonI18nKey.COMPLETE)
    )

    private fun DIV.pagination() {
        val firstResult = criteria.pagination.offset.toInt() + 1
        val lastResult = criteria.pagination.offset.toInt() + result.results.size
        val summary = "${translate(CommonI18nKey.SHOWING)} $firstResult-$lastResult " +
                "${translate(CommonI18nKey.OF)} ${result.totalResults} ${translate(CommonI18nKey.RESULTS)}"
        row(
            JustifyContent.SPACE_BETWEEN,
            AlignItems.CENTER,
            style =
                "background-color: white; border: 1px solid ${Colors.CREAM}; border-radius: 8px; " +
                        "padding: 0.75rem 1rem; flex-wrap: wrap;"
        ) {
            p {
                style = "margin: 0; color: ${Colors.DARK_BLUE}; font-weight: 700;"
                +summary
            }
            row(JustifyContent.CENTER, AlignItems.CENTER, gap = Gap.MIN) {
                if (criteria.pagination.page > 1) {
                    paginationButton(
                        translate(CommonI18nKey.PREVIOUS),
                        criteria.pagination.page - 1,
                        disableEmptyFieldsByFormIdScript,
                    )
                }
                p {
                    style =
                        "margin: 0; color: ${Colors.DARKEST_BLUE}; background-color: ${Colors.CREAM_LIGHEST}; " +
                                "border-radius: 999px; padding: 0.45rem 0.75rem; font-weight: 700;"
                    +"${translate(CommonI18nKey.PAGE)} ${criteria.pagination.page}"
                }
                if (result.hasNextPage) {
                    paginationButton(
                        translate(CommonI18nKey.NEXT),
                        criteria.pagination.page + 1,
                        disableEmptyFieldsByFormIdScript,
                    )
                }
            }
        }
    }
}

private fun DIV.paginationButton(
    text: String,
    toPage: Int,
    disableEmptyFieldsScript: String,
) {
    button(type = ButtonType.button) {
        style =
            "background-color: ${Colors.DARK_BLUE}; border: none; padding: 0.5rem 0.85rem; " +
                    "border-radius: 8px; cursor: pointer; font-weight: bold; color: ${Colors.ON_DARK_BLUE};"
        this.onClick =
            "document.getElementById('page-input').value = '${toPage}'; " +
                    "$disableEmptyFieldsScript; " +
                    "document.getElementById('filter-form').submit()"
        +text
    }
}

private const val TEAM_SIZE = 6
