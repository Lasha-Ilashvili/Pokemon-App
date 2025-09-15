package com.task.pokemon_app.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object PokemonList : Screen("pokemon_list")

    data object PokemonDetails : Screen("pokemon_details/{name}/{imageUrl}") {
        fun createRoute(name: String, imageUrl: String): String =
            "pokemon_details/${Uri.encode(name)}/${Uri.encode(imageUrl)}"
    }
}