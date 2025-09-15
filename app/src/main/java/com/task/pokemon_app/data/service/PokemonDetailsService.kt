package com.task.pokemon_app.data.service

import com.task.pokemon_app.data.model.PokemonDetailsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonDetailsService {

    @GET
    suspend fun getPokemonDetails(@Url url: String): Response<PokemonDetailsDto>
}