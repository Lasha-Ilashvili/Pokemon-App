package com.task.pokemon_app.presentation.screen.pokemon_list

import app.cash.turbine.test
import com.task.pokemon_app.domain.usecase.GetPokemonsUseCase
import com.task.pokemon_app.presentation.common.BaseViewModelTest
import com.task.pokemon_app.presentation.event.PokemonListEvent
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Test

class PokemonListViewModelTest : BaseViewModelTest() {

    @MockK(relaxed = true)
    lateinit var getPokemonsUseCase: GetPokemonsUseCase

    private fun createViewModel() = PokemonListViewModel(getPokemonsUseCase)

    @Test
    fun `OnImageClick updates enlargedImageUrl`() = runTestBlock {
        val viewModel = createViewModel()

        viewModel.onEvent(PokemonListEvent.OnImageClick("fake_url"))

        assertEquals("fake_url", viewModel.uiState.value.enlargedImageUrl)
    }

    @Test
    fun `OnOverlayDismiss clears enlargedImageUrl`() = runTestBlock {
        val viewModel = createViewModel()

        viewModel.onEvent(PokemonListEvent.OnImageClick("fake_url"))
        viewModel.onEvent(PokemonListEvent.OnOverlayDismiss)

        assertNull(viewModel.uiState.value.enlargedImageUrl)
    }

    @Test
    fun `OnDetailsClick emits navigation event and sets isNavigating`() = runTestBlock {
        val viewModel = createViewModel()

        val name = "Bulbasaur"
        val imageUrl = "image_url"
        val detailsUrl = "details_url"

        viewModel.uiEvent.test {
            viewModel.onEvent(PokemonListEvent.OnDetailsClick(name, imageUrl, detailsUrl))

            // State should reflect navigation in progress
            assertTrue(viewModel.uiState.value.isNavigating)

            // Event should be emitted
            val event = awaitItem()
            assertEquals(
                PokemonListViewModel.PokemonListUiEvent.NavigateToDetails(
                    name = name,
                    imageUrl = imageUrl,
                    detailsUrl = detailsUrl
                ),
                event
            )
        }
    }

    @Test
    fun `OnNavigationHandled resets isNavigating`() = runTestBlock {
        val viewModel = createViewModel()

        viewModel.onEvent(
            PokemonListEvent.OnDetailsClick("Bulbasaur", "image_url", "details_url")
        )
        assertTrue(viewModel.uiState.value.isNavigating)

        viewModel.onEvent(PokemonListEvent.OnNavigationHandled)
        assertFalse(viewModel.uiState.value.isNavigating)
    }
}