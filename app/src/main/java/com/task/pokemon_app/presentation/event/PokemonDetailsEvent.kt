package com.task.pokemon_app.presentation.event

sealed class PokemonDetailsEvent {
    data class LoadDetails(val url: String) : PokemonDetailsEvent()
    data class Retry(val url: String) : PokemonDetailsEvent()
}