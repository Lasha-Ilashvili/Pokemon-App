package com.task.pokemon_app.presentation.navigation

import PokemonDetailsScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        modifier = modifier,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        composable(Screen.PokemonList.route) {
            PokemonListScreen(
                onNavigate = { event ->
                    when (event) {
                        is PokemonListViewModel.PokemonListUiEvent.NavigateToDetails -> {
                            navController.navigate(
                                route = Screen.PokemonDetails.createRoute(
                                    name = event.name,
                                    imageUrl = event.imageUrl,
                                    detailsUrl = event.detailsUrl
                                )
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
                navArgument("imageUrl") { type = NavType.StringType },
                navArgument("detailsUrl") { type = NavType.StringType }
            )
        ) { entry ->
            PokemonDetailsScreen(
                name = entry.arguments?.getString("name") ?: "",
                imageUrl = entry.arguments?.getString("imageUrl") ?: "",
                detailsUrl = entry.arguments?.getString("detailsUrl") ?: ""
            )
        }
    }
}