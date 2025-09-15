package com.task.pokemon_app.presentation.screen.pokemon_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.pokemon_app.data.common.Result
import com.task.pokemon_app.domain.usecase.GetPokemonDetailsUseCase
import com.task.pokemon_app.presentation.event.PokemonDetailsEvent
import com.task.pokemon_app.presentation.state.PokemonDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailsState())
    val uiState: StateFlow<PokemonDetailsState> = _uiState.asStateFlow()

    fun onEvent(event: PokemonDetailsEvent) {
        when (event) {
            is PokemonDetailsEvent.LoadDetails -> loadDetails(event.url)
            is PokemonDetailsEvent.Retry -> loadDetails(event.url)
        }
    }

    private fun loadDetails(url: String) {
        viewModelScope.launch {
            getPokemonDetailsUseCase(url).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = result.isLoading) }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(abilities = result.data, error = null)
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(error = result.exception.message)
                        }
                    }
                }
            }
        }
    }
}