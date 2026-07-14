package dev.gemmabcr.views.pages.components.filters

data class AreaFilterConfig(
    val label: String,
    val name: String,
    val allLabel: String,
    val selectedValue: String?,
    val options: List<AreaFilterOption>,
    val onChange: String,
)
