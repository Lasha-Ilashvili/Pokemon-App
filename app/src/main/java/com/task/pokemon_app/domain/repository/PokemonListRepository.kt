package com.task.pokemon_app.domain.repository

import androidx.paging.PagingData
import com.task.pokemon_app.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository {

    fun getPokemonList(): Flow<PagingData<Pokemon>>
}