package com.task.pokemon_app.data.repository

import com.task.pokemon_app.data.common.HandleResponse
import com.task.pokemon_app.data.common.Result
import com.task.pokemon_app.data.common.onSuccess
import com.task.pokemon_app.data.service.PokemonDetailsService
import com.task.pokemon_app.domain.repository.PokemonDetailsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class PokemonDetailsRepositoryImpl @Inject constructor(
    private val pokemonDetailsService: PokemonDetailsService,
    private val handleResponse: HandleResponse
) : PokemonDetailsRepository {

    override fun getPokemonDetails(url: String): Flow<Result<List<String>>> =
        handleResponse.safeApiCall {
            pokemonDetailsService.getPokemonDetails(url)
        }.onSuccess {
            it.abilities.map { ability -> ability.ability.name }
        }
}