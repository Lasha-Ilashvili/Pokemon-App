package com.task.pokemon_app.presentation.screen.pokemon_details

import app.cash.turbine.test
import com.task.pokemon_app.data.common.Result
import com.task.pokemon_app.domain.exception.ApiException
import com.task.pokemon_app.domain.exception.ErrorType
import com.task.pokemon_app.domain.usecase.GetPokemonDetailsUseCase
import com.task.pokemon_app.presentation.common.BaseViewModelTest
import com.task.pokemon_app.presentation.event.PokemonDetailsEvent
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Test

class PokemonDetailsViewModelTest : BaseViewModelTest() {

    @MockK
    lateinit var getPokemonDetailsUseCase: GetPokemonDetailsUseCase

    @Test
    fun `LoadDetails updates state with abilities on success`() = runTestBlock {
        val fakeAbilities = listOf("Overgrow", "Chlorophyll")
        coEvery { getPokemonDetailsUseCase(any()) } returns flowOf(Result.Success(fakeAbilities))

        val viewModel = PokemonDetailsViewModel(getPokemonDetailsUseCase)

        viewModel.uiState.test {
            viewModel.onEvent(PokemonDetailsEvent.LoadDetails("url"))

            awaitItem()

            val successState = awaitItem()
            assertEquals(fakeAbilities, successState.abilities)
            assertEquals(null, successState.error)
            assertEquals(false, successState.isLoading)
        }
    }

    @Test
    fun `LoadDetails updates state with error`() = runTestBlock {
        val fakeError = ApiException(ErrorType.NO_INTERNET)
        coEvery { getPokemonDetailsUseCase(any()) } returns flowOf(Result.Error(fakeError))

        val viewModel = PokemonDetailsViewModel(getPokemonDetailsUseCase)

        viewModel.uiState.test {
            viewModel.onEvent(PokemonDetailsEvent.LoadDetails("url"))

            awaitItem()

            val errorState = awaitItem()
            assertTrue(errorState.error!!.contains("No internet"))
            assertEquals(null, errorState.abilities)
        }
    }

    @Test
    fun `LoadDetails updates state with loading`() = runTestBlock {
        coEvery { getPokemonDetailsUseCase(any()) } returns flowOf(Result.Loading(true))

        val viewModel = PokemonDetailsViewModel(getPokemonDetailsUseCase)

        viewModel.uiState.test {
            viewModel.onEvent(PokemonDetailsEvent.LoadDetails("url"))

            awaitItem()

            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)
        }
    }
}