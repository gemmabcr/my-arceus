package dev.gemmabcr.views.ui

import kotlinx.html.FormMethod

data class FormConfig(
    val action: String,
    val method: FormMethod,
    val submitText: String,
    val id: String? = null,
    val onSubmit: String? = null,
)
