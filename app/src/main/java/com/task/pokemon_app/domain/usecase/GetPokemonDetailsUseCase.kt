package com.task.pokemon_app.domain.usecase

import com.task.pokemon_app.data.common.Result
import com.task.pokemon_app.domain.repository.PokemonDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(
    private val pokemonDetailsRepository: PokemonDetailsRepository
) {

    operator fun invoke(url: String): Flow<Result<List<String>>> =
        pokemonDetailsRepository.getPokemonDetails(url)
}