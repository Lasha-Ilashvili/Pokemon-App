package com.task.pokemon_app.data.data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.task.pokemon_app.data.common.HandleResponse.safeApiCall
import com.task.pokemon_app.data.common.Result
import com.task.pokemon_app.domain.exception.ApiException
import com.task.pokemon_app.domain.exception.ErrorType
import com.task.pokemon_app.data.model.PokemonListDto.PokemonDto
import com.task.pokemon_app.data.service.PokemonListService
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class PokemonListPagingSource @Inject constructor(
    private val service: PokemonListService
) : PagingSource<String, PokemonDto>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PokemonDto> {
        val url = params.key

        val result = safeApiCall {
            url?.let { service.getPokemonList(it) } ?: service.getPokemonList()
        }.filterNot { it is Result.Loading }.firstOrNull()

        return when (result) {
            is Result.Success -> with(result.data) {
                LoadResult.Page(data = pokemons, prevKey = previous, nextKey = next)
            }

            is Result.Error -> LoadResult.Error(result.exception)
            else -> LoadResult.Error(ApiException(ErrorType.UNKNOWN_ERROR))
        }
    }

    override fun getRefreshKey(state: PagingState<String, PokemonDto>): String? = null
}