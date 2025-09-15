package com.task.pokemon_app.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailsDto(
    val abilities: List<AbilitiesDto>,
) {

    @JsonClass(generateAdapter = true)
    data class AbilitiesDto(
        val ability: AbilityDto
    ) {

        @JsonClass(generateAdapter = true)
        data class AbilityDto(
            val name: String
        )
    }
}