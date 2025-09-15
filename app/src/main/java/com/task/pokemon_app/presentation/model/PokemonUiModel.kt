package com.task.pokemon_app.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class PokemonUiModel(
    val id: String,
    val name: String,
    val url: String,
    val imageUrl: String,
)