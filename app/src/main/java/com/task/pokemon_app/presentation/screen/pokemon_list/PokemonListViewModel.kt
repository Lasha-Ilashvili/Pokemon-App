package com.task.pokemon_app.presentation.screen.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.task.pokemon_app.domain.usecase.GetJobsUseCase
import com.task.pokemon_app.presentation.mapper.toUiModel
import com.task.pokemon_app.presentation.model.PokemonUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(getJobsUseCase: GetJobsUseCase) : ViewModel() {

    val pokemonPagingData: Flow<PagingData<PokemonUiModel>> = getJobsUseCase().map {
        it.map { pokemon -> pokemon.toUiModel() }
    }.cachedIn(viewModelScope)
}