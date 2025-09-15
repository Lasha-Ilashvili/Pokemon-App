package com.task.pokemon_app.domain.repository

import com.task.pokemon_app.data.common.Result
import kotlinx.coroutines.flow.Flow

interface PokemonDetailsRepository {

    fun getPokemonDetails(url: String): Flow<Result<List<String>>>
}