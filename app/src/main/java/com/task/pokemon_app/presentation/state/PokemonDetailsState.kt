package com.task.pokemon_app.presentation.state

data class PokemonDetailsState(
    val isLoading: Boolean = false,
    val abilities: List<String>? = null,
    val error: String? = null
)