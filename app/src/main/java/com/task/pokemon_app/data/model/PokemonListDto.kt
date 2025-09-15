package com.task.pokemon_app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class PokemonListDto(
    val count: Long,
    val next: String?,
    val previous: String?,
    @param:Json(name = "results")
    val pokemons: List<PokemonDto>
) {

    @JsonClass(generateAdapter = true)
    data class PokemonDto(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val url: String
    )
}