package com.task.pokemon_app.presentation.mapper

import com.task.pokemon_app.domain.model.PokemonList
import com.task.pokemon_app.presentation.model.PokemonUiModel

fun PokemonList.toUiModel() = PokemonUiModel(
    id = id,
    name = name,
    url = url,
    imageUrl = imageUrl,
)