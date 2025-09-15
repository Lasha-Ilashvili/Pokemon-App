package com.task.pokemon_app.data.mapper

import com.task.pokemon_app.data.model.PokemonListDto
import com.task.pokemon_app.domain.model.PokemonList
import java.util.UUID

fun PokemonListDto.PokemonDto.toDomain(): PokemonList = PokemonList(
    id = UUID.randomUUID().toString(),
    name = name,
    url = url,
    imageUrl = "https://img.pokemondb.net/artwork/${name}.jpg",
)