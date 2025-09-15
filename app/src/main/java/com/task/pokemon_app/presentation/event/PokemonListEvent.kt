package com.task.pokemon_app.presentation.event

sealed class PokemonListEvent {
    data class OnImageClick(val imageUrl: String) : PokemonListEvent()
    data object OnOverlayDismiss : PokemonListEvent()
    data class OnDetailsClick(
        val name: String,
        val imageUrl: String,
        val detailsUrl: String
    ) : PokemonListEvent()

    data object OnNavigationHandled : PokemonListEvent()
}