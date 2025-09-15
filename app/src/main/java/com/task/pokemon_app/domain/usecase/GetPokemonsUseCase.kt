package com.task.pokemon_app.domain.usecase

import androidx.paging.PagingData
import com.task.pokemon_app.domain.model.PokemonList
import com.task.pokemon_app.domain.repository.PokemonListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonsUseCase @Inject constructor(
    private val pokemonListRepository: PokemonListRepository
) {

    operator fun invoke(): Flow<PagingData<PokemonList>> =
        pokemonListRepository.getPokemonList()
}