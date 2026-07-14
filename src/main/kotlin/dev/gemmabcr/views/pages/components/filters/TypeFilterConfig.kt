package dev.gemmabcr.views.pages.components.filters

data class TypeFilterConfig(
    val label: String,
    val name: String,
    val allLabel: String,
    val selectedValue: String?,
    val options: List<TypeFilterOption>,
    val onChange: String,
)
