package com.task.pokemon_app.presentation.screen.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.task.pokemon_app.domain.usecase.GetPokemonsUseCase
import com.task.pokemon_app.presentation.event.PokemonListEvent
import com.task.pokemon_app.presentation.mapper.toUiModel
import com.task.pokemon_app.presentation.model.PokemonUiModel
import com.task.pokemon_app.presentation.state.PokemonListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    getPokemonsUseCase: GetPokemonsUseCase
) : ViewModel() {

    val pokemonPagingData: Flow<PagingData<PokemonUiModel>> = getPokemonsUseCase().map {
        it.map { pokemon -> pokemon.toUiModel() }
    }.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(PokemonListState())
    val uiState: StateFlow<PokemonListState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<PokemonListUiEvent>()
    val uiEvent: SharedFlow<PokemonListUiEvent> get() = _uiEvent.asSharedFlow()

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.OnImageClick -> handleImageClick(event.imageUrl)
            PokemonListEvent.OnOverlayDismiss -> handleOverlayDismiss()
            is PokemonListEvent.OnDetailsClick -> handleDetails(
                name = event.name,
                imageUrl = event.imageUrl,
                detailsUrl = event.detailsUrl
            )

            PokemonListEvent.OnNavigationHandled -> _uiState.update { it.copy(isNavigating = false) }
        }
    }

    private fun handleImageClick(url: String) {
        _uiState.update { it.copy(enlargedImageUrl = url) }
    }

    private fun handleOverlayDismiss() {
        _uiState.update { it.copy(enlargedImageUrl = null) }
    }

    private fun handleDetails(name: String, imageUrl: String, detailsUrl: String) {
        _uiState.update { it.copy(isNavigating = true) }
        viewModelScope.launch {
            _uiEvent.emit(PokemonListUiEvent.NavigateToDetails(name, imageUrl, detailsUrl))
        }
    }

    sealed interface PokemonListUiEvent {
        data class NavigateToDetails(
            val name: String,
            val imageUrl: String,
            val detailsUrl: String
        ) : PokemonListUiEvent
    }
}