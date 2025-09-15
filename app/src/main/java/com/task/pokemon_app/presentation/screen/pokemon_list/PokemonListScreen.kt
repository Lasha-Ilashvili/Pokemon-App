package com.task.pokemon_app.presentation.screen.pokemon_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.task.pokemon_app.domain.exception.ErrorType
import com.task.pokemon_app.presentation.model.PokemonUiModel

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Content(
        modifier = modifier,
        pokemonItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    pokemonItems: LazyPagingItems<PokemonUiModel>
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            loadStateItem(pokemonItems.loadState.prepend, pokemonItems::retry, modifier = modifier)

            items(
                count = pokemonItems.itemCount,
                key = pokemonItems.itemKey { it.id }
            ) { index ->
                pokemonItems[index]?.let { PokemonItem(pokemon = it) }
            }

            loadStateItem(pokemonItems.loadState.append, pokemonItems::retry, modifier = modifier)
        }

        LoadStateOverlay(
            loadState = pokemonItems.loadState.refresh,
            onRetry = pokemonItems::retry,
            modifier = modifier
        )
    }
}

private fun LazyListScope.loadStateItem(
    loadState: LoadState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (loadState is LoadState.Loading) {
        item { LoadingItem(modifier = modifier) }
    } else if (loadState is LoadState.Error) {
        item {
            ErrorItem(
                message = loadState.error.localizedMessage ?: ErrorType.UNKNOWN_ERROR.message,
                onRetry = onRetry,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun BoxScope.LoadStateOverlay(
    loadState: LoadState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (loadState is LoadState.Loading) {
        CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
    } else if (loadState is LoadState.Error) {
        ErrorItem(
            message = loadState.error.localizedMessage ?: ErrorType.UNKNOWN_ERROR.message,
            onRetry = onRetry,
            modifier = modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun PokemonItem(pokemon: PokemonUiModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = pokemon.name,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun LoadingItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorItem(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}