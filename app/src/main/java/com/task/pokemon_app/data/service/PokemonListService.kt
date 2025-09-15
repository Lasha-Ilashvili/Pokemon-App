package com.task.pokemon_app.data.service

import com.task.pokemon_app.data.model.PokemonListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonListService {

    @GET
    suspend fun getPokemonList(@Url url: String = POKEMON_LIST): Response<PokemonListDto>

    companion object {
        const val LIMIT = 10
        private const val POKEMON_LIST = "pokemon/?offset=0&limit=$LIMIT"
    }
}