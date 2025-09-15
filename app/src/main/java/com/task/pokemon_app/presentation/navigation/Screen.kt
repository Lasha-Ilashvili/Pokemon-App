package com.task.pokemon_app.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object PokemonList : Screen("pokemon_list")

    data object PokemonDetails : Screen("pokemon_details/{name}/{imageUrl}/{detailsUrl}") {
        fun createRoute(name: String, imageUrl: String, detailsUrl: String): String =
            "pokemon_details/${Uri.encode(name)}/${Uri.encode(imageUrl)}/${Uri.encode(detailsUrl)}"
    }
}