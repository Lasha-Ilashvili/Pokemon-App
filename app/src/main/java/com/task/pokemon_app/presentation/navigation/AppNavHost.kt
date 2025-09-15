package com.task.pokemon_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.task.pokemon_app.presentation.screen.pokemon_details.PokemonDetailsScreen
import com.task.pokemon_app.presentation.screen.pokemon_list.PokemonListScreen
import com.task.pokemon_app.presentation.screen.pokemon_list.PokemonListViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier
    ) {
        composable(Screen.PokemonList.route) {
            PokemonListScreen(
                onNavigate = { event ->
                    when (event) {
                        is PokemonListViewModel.PokemonListUiEvent.NavigateToDetails -> {
                            navController.navigate(
                                Screen.PokemonDetails.createRoute(event.name, event.imageUrl)
                            )
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.PokemonDetails.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { entry ->
            val name = entry.arguments?.getString("name") ?: ""
            val imageUrl = entry.arguments?.getString("imageUrl") ?: ""
            PokemonDetailsScreen(name, imageUrl)
        }
    }
}