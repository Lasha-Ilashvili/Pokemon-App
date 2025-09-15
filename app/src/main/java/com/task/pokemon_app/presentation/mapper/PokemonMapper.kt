package com.task.pokemon_app.presentation.mapper

import com.task.pokemon_app.domain.model.Pokemon
import com.task.pokemon_app.presentation.model.PokemonUiModel

fun Pokemon.toUiModel() = PokemonUiModel(
    id = id,
    name = name,
    url = url
)