package com.task.pokemon_app.domain.usecase

import androidx.paging.PagingData
import com.task.pokemon_app.domain.model.Pokemon
import com.task.pokemon_app.domain.repository.PokemonListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetJobsUseCase @Inject constructor(
    private val pokemonListRepository: PokemonListRepository
) {

    operator fun invoke(): Flow<PagingData<Pokemon>> =
        pokemonListRepository.getPokemonList()
}