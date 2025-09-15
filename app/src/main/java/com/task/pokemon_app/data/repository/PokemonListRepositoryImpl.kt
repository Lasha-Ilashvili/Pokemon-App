package com.task.pokemon_app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.task.pokemon_app.data.model.PokemonListDto.PokemonDto
import com.task.pokemon_app.data.service.PokemonListService
import com.task.pokemon_app.data.mapper.toDomain
import com.task.pokemon_app.domain.model.PokemonList
import com.task.pokemon_app.domain.repository.PokemonListRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class PokemonListRepositoryImpl @Inject constructor(
    private val pokemonListPagingSource: PagingSource<String, PokemonDto>
) : PokemonListRepository {

    override fun getPokemonList(): Flow<PagingData<PokemonList>> = Pager(
        config = PagingConfig(pageSize = PokemonListService.LIMIT, enablePlaceholders = false)
    ) { pokemonListPagingSource }.flow.map {
        it.map { dto -> dto.toDomain() }
    }
}