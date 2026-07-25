package dev.gemmabcr.views.ui

import dev.gemmabcr.models.CompletionFilter
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.QueryResult
import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.models.pokemons.Type
import dev.gemmabcr.models.pokemons.todo.ToDo
import dev.gemmabcr.views.QueryCriteriaType
import dev.gemmabcr.views.adapters.AreaI18nKeyAdapter
import dev.gemmabcr.views.adapters.ToDoTypeAdapter
import dev.gemmabcr.views.adapters.TypeI18nKeyAdapter
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.flexs.row
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.onChange
import kotlinx.html.onClick
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.style

class PokemonListContent(
    private val criteria: QueryCriteria,
    private val result: QueryResult<Pokemon>,
    private val todos: List<ToDo>,
    private val team: List<Pokemon>,
    private val redirectTo: String,
    private val isLoggedIn: Boolean,
) : UiComponent {
    private val disableEmptyFieldsScript = "Array.from(this.form.elements)" +
        ".forEach(e=>{if(e.name&&!e.value)e.disabled=true})"
    private val disableEmptyFieldsByFormIdScript =
        "Array.from(document.getElementById('filter-form').elements)" +
            ".forEach(e=>{if(e.name&&!e.value)e.disabled=true})"

    override fun create(content: FlowContent): FlowContent = content.apply {
        val autoSubmit =
            "document.getElementById('page-input').value='1'; " +
                "$disableEmptyFieldsScript; " +
                "this.form.submit()"
        div {
            classes = setOf("pokemon-list-layout")
            div {
                classes = setOf("pokemon-filter-sidebar", "ui-card")
                filtersForm(autoSubmit)
            }
            column(gap = Gap.MAX, style = "min-width: 0; width: 100%;") {
                if (result.results.isEmpty()) {
                    noResultsCard(translate(CommonI18nKey.NO_RESULTS))
                } else {
                    pagination()
                    PokemonCards(
                        pokemons = result.results,
                        teamSize = team.size,
                        redirectTo = redirectTo,
                        canManageTeam = isLoggedIn,
                    ).create(this)
                    pagination()
                }
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
                onSubmit = disableEmptyFieldsScript,
            ),
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
        areaFilter(
            AreaFilterConfig(
                label = translate(CommonI18nKey.AREA),
                name = QueryCriteriaType.AREA.key(),
                allLabel = translate(CommonI18nKey.ALL),
                selectedValue = criteria.area?.name,
                options = Area.entries.map { area ->
                    AreaFilterOption(
                        value = area.name,
                        label = translate(AreaI18nKeyAdapter(area).i18nKey()),
                        iconPath = area.iconPath(),
                    )
                },
                onChange = autoSubmit,
            ),
        )
        typeFilter(
            TypeFilterConfig(
                label = translate(CommonI18nKey.TYPE),
                name = QueryCriteriaType.TYPE.key(),
                allLabel = translate(CommonI18nKey.ALL),
                selectedValue = criteria.type?.name,
                options = Type.entries.map { type ->
                    TypeFilterOption(
                        value = type.name,
                        label = translate(TypeI18nKeyAdapter(type).i18nKey()),
                        iconPath = type.iconPath(),
                    )
                },
                onChange = autoSubmit,
            ),
        )
        standardFilters(autoSubmit)
    }

    private fun DIV.standardFilters(autoSubmit: String) {
        textInput(
            translate(CommonI18nKey.NAME),
            QueryCriteriaType.NAME.key(),
            value = criteria.name,
            onChange = autoSubmit,
        )
        numberInput(
            translate(CommonI18nKey.NUMBER),
            QueryCriteriaType.NUMBER.key(),
            criteria.number?.toString(),
            onChange = autoSubmit,
        )
        selectInput(
            translate(CommonI18nKey.TODOS),
            QueryCriteriaType.TO_DO.key(),
            toDoOptions(),
            value = criteria.toDo?.id?.toString() ?: "",
            onChange = autoSubmit,
        )
        selectInput(
            translate(CommonI18nKey.COMPLETION),
            QueryCriteriaType.COMPLETION.key(),
            completionOptions(),
            value = criteria.completion.name,
            onChange = autoSubmit,
        )
        if (isLoggedIn) {
            checkBox(
                translate(CommonI18nKey.ONLY_MY_TEAM),
                QueryCriteriaType.TEAM.key(),
                criteria.onlyTeam,
                autoSubmit,
            )
        }
    }

    private fun toDoOptions(): Map<String, String> =
        mapOf("" to "") + todos.associate { it.id.toString() to ToDoTypeAdapter(it.description).text() }

    private fun completionOptions(): Map<String, String> = mapOf(
        CompletionFilter.ALL.name to translate(CommonI18nKey.ALL),
        CompletionFilter.UNCOMPLETED.name to translate(CommonI18nKey.UNCOMPLETED),
        CompletionFilter.COMPLETED.name to translate(CommonI18nKey.COMPLETE),
    )

    private fun DIV.pagination() {
        val firstResult = criteria.pagination.offset.toInt() + 1
        val lastResult = criteria.pagination.offset.toInt() + result.results.size
        val summary = "${translate(CommonI18nKey.SHOWING)} $firstResult-$lastResult " +
            "${translate(CommonI18nKey.OF)} ${result.totalResults} ${translate(CommonI18nKey.RESULTS)}"
        row(
            JustifyContent.SPACE_BETWEEN,
            AlignItems.CENTER,
            gap = Gap.MIN,
            style =
                "width: 100%; box-sizing: border-box; border-top: 1px solid ${Colors.BLUE_GREY}; " +
                    "padding: 0.45rem 0.25rem 0; flex-wrap: wrap;",
        ) {
            p {
                style = "margin: 0; color: ${Colors.BLUE_GREY}; font-size: 0.75rem; font-weight: 500;"
                +summary
            }
            row(JustifyContent.CENTER, AlignItems.CENTER, gap = Gap.MIN) {
                if (criteria.pagination.page > 1) {
                    paginationButton(
                        "← ${translate(CommonI18nKey.PREVIOUS)}",
                        criteria.pagination.page - 1,
                    )
                }
                p {
                    style =
                        "margin: 0; color: ${Colors.DARK_BLUE}; background-color: ${Colors.CREAM_LIGHEST}; " +
                            "border-radius: 999px; padding: 0.28rem 0.55rem; font-size: 0.72rem; " +
                            "font-weight: 600;"
                    +"${translate(CommonI18nKey.PAGE)} ${criteria.pagination.page}"
                }
                if (result.hasNextPage) {
                    paginationButton(
                        "${translate(CommonI18nKey.NEXT)} →",
                        criteria.pagination.page + 1,
                    )
                }
            }
        }
    }

    private fun DIV.paginationButton(text: String, toPage: Int) {
        button(type = ButtonType.button) {
            classes = setOf("ui-secondary-button")
            style = "min-height: 30px; padding: 0.3rem 0.55rem; font-size: 0.75rem;"
            onClick =
                "document.getElementById('page-input').value = '$toPage'; " +
                    "$disableEmptyFieldsByFormIdScript; " +
                    "document.getElementById('filter-form').submit()"
            +text
        }
    }
}

private fun DIV.areaFilter(config: AreaFilterConfig) {
    column(style = "width: 100%;") {
        label {
            classes = setOf("ui-label")
            +config.label
        }
        div {
            classes = setOf("area-filter-options")
            attributes["role"] = "radiogroup"
            areaFilterOption(config, "", config.allLabel)
            config.options.forEach { areaFilterOption(config, it.value, it.label, it.iconPath) }
        }
    }
}

private fun DIV.areaFilterOption(
    config: AreaFilterConfig,
    value: String,
    label: String,
    iconPath: String? = null,
) {
    val inputId = "area-filter-${value.ifEmpty { "all" }.lowercase()}"
    input(type = InputType.radio, name = config.name) {
        id = inputId
        this.value = value
        checked = config.selectedValue == value || config.selectedValue == null && value.isEmpty()
        onChange = config.onChange
        classes = setOf("area-filter-option-input")
    }
    label {
        attributes["for"] = inputId
        classes = if (iconPath == null) {
            setOf("area-filter-option", "area-filter-option-all")
        } else {
            setOf("area-filter-option")
        }
        iconPath?.let { img(src = it, alt = label) }
        span { +label }
    }
}

private fun DIV.typeFilter(config: TypeFilterConfig) {
    column(style = "width: 100%;") {
        label {
            classes = setOf("ui-label")
            +config.label
        }
        div {
            classes = setOf("type-filter-options")
            attributes["role"] = "radiogroup"
            typeFilterOption(config, "", config.allLabel)
            config.options.forEach { typeFilterOption(config, it.value, it.label, it.iconPath) }
        }
    }
}

private fun DIV.typeFilterOption(
    config: TypeFilterConfig,
    value: String,
    label: String,
    iconPath: String? = null,
) {
    val inputId = "type-filter-${value.ifEmpty { "all" }.lowercase()}"
    input(type = InputType.radio, name = config.name) {
        id = inputId
        this.value = value
        checked = config.selectedValue == value || config.selectedValue == null && value.isEmpty()
        onChange = config.onChange
        classes = setOf("type-filter-option-input")
    }
    label {
        attributes["for"] = inputId
        classes = if (iconPath == null) {
            setOf("type-filter-option", "type-filter-option-all")
        } else {
            setOf("type-filter-option")
        }
        iconPath?.let { img(src = it, alt = "") }
        span { +label }
    }
}

private fun DIV.noResultsCard(message: String) {
    column(
        JustifyContent.CENTER,
        AlignItems.CENTER,
        gap = Gap.MIN,
        style = "width: 100%; padding: 2rem;",
    ) {
        classes = setOf("ui-card")
        img(src = ImageSource.NO_RESULT.url) {
            height = "120"
            width = "120"
        }
        p {
            style = "margin: 0; color: ${Colors.DARKEST_BLUE}; font-weight: 700; text-align: center;"
            +message
        }
    }
}

private fun Area.iconPath(): String = when (this) {
    Area.COASTLANDS -> "/icons/areas/cobalt_coastlands.webp"
    Area.DISTORTION -> "/icons/areas/distorsion.webp"
    Area.FIELDLANDS -> "/icons/areas/obsidian_fieldlands.webp"
    Area.HIGHLANDS -> "/icons/areas/coronet_highlands.webp"
    Area.ICELANDS -> "/icons/areas/alabaster_icelands.webp"
    Area.MIRELANDS -> "/icons/areas/crimson_mirelands.webp"
}

private fun Type.iconPath(): String = "/icons/types/${name.lowercase()}.svg"
