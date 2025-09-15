package com.task.pokemon_app.presentation.screen.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.SubcomposeAsyncImage
import com.task.pokemon_app.domain.exception.ErrorType
import com.task.pokemon_app.presentation.event.PokemonListEvent
import com.task.pokemon_app.presentation.model.PokemonUiModel
import com.task.pokemon_app.presentation.state.PokemonListState

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel(),
    onNavigate: (PokemonListViewModel.PokemonListUiEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pokemonItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event -> onNavigate(event) }
    }

    Content(
        modifier = modifier,
        pokemonItems = pokemonItems,
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    pokemonItems: LazyPagingItems<PokemonUiModel>,
    uiState: PokemonListState,
    onEvent: (PokemonListEvent) -> Unit
) {
    val corner = 12.dp

    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                LoadStateItem(pokemonItems.loadState.prepend, pokemonItems::retry)
            }

            items(
                count = pokemonItems.itemCount,
                key = pokemonItems.itemKey { it.id }
            ) { index ->
                pokemonItems[index]?.let { model ->
                    PokemonItem(
                        pokemon = model,
                        corner = corner,
                        onImageClick = { onEvent(PokemonListEvent.OnImageClick(model.imageUrl)) },
                        onDetailsClick = {
                            onEvent(
                                PokemonListEvent.OnDetailsClick(
                                    name = model.name,
                                    imageUrl = model.imageUrl
                                )
                            )
                        }
                    )
                }
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                LoadStateItem(pokemonItems.loadState.append, pokemonItems::retry)
            }
        }

        LoadStateOverlay(
            loadState = pokemonItems.loadState.refresh,
            onRetry = pokemonItems::retry
        )

        uiState.enlargedImageUrl?.let { url ->
            ImageZoomOverlay(
                imageUrl = url,
                corner = corner,
                onDismiss = { onEvent(PokemonListEvent.OnOverlayDismiss) }
            )
        }
    }
}

@Composable
private fun PokemonItem(
    pokemon: PokemonUiModel,
    corner: Dp,
    onImageClick: () -> Unit,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(corner),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            SubcomposeAsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(corner))
                    .clickable(onClick = onImageClick),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Failed to load", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = pokemon.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onDetailsClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Details")
                }
            }
        }
    }
}

@Composable
private fun ImageZoomOverlay(
    imageUrl: String,
    corner: Dp,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize(0.9f)
                .clip(RoundedCornerShape(corner))
        )
    }
}

@Composable
private fun LoadStateItem(
    loadState: LoadState,
    onRetry: () -> Unit
) {
    if (loadState is LoadState.Loading) {
        LoadingItem()
    } else if (loadState is LoadState.Error) {
        ErrorItem(
            message = loadState.error.localizedMessage ?: ErrorType.UNKNOWN_ERROR.message,
            onRetry = onRetry
        )
    }
}

@Composable
private fun LoadStateOverlay(
    loadState: LoadState,
    onRetry: () -> Unit
) {
    if (loadState is LoadState.Loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (loadState is LoadState.Error) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ErrorItem(
                message = loadState.error.localizedMessage ?: ErrorType.UNKNOWN_ERROR.message,
                onRetry = onRetry
            )
        }
    }
}

@Composable
private fun LoadingItem() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorItem(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}