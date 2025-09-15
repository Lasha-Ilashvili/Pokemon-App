package com.task.pokemon_app.data.mapper

import com.task.pokemon_app.data.model.PokemonListDto
import com.task.pokemon_app.domain.model.Pokemon

fun PokemonListDto.PokemonDto.toDomain(): Pokemon = Pokemon(
    id = id,
    name = name,
    url = url
)