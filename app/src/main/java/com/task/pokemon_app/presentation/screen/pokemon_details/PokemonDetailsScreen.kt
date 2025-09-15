import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.task.pokemon_app.presentation.event.PokemonDetailsEvent
import com.task.pokemon_app.presentation.screen.pokemon_details.PokemonDetailsViewModel
import com.task.pokemon_app.presentation.state.PokemonDetailsState

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PokemonDetailsScreenPreview() {
    Content(
        name = "Pikachu",
        imageUrl = "https://img.pokemondb.net/artwork/bulbasaur.jpg",
        detailsUrl = "https://pokeapi.co/api/v2/pokemon/25/",
        state = PokemonDetailsState(
            abilities = listOf("Static", "Lightning Rod")
        ),
        onEvent = {}
    )
}

@Composable
fun PokemonDetailsScreen(
    name: String,
    imageUrl: String,
    detailsUrl: String,
    viewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(detailsUrl) {
        viewModel.onEvent(PokemonDetailsEvent.LoadDetails(detailsUrl))
    }

    Content(
        name = name,
        imageUrl = imageUrl,
        detailsUrl = detailsUrl,
        state = uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun Content(
    name: String,
    imageUrl: String,
    detailsUrl: String,
    state: PokemonDetailsState,
    onEvent: (PokemonDetailsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(Modifier.height(16.dp))

        Text(text = name, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (state.isLoading) CircularProgressIndicator()

        state.error?.let {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { onEvent(PokemonDetailsEvent.Retry(detailsUrl)) }) {
                    Text("Try Again")
                }
            }
        }

        state.abilities?.let {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                it.forEach { ability ->
                    AssistChip(
                        onClick = {},
                        label = { Text(ability) }
                    )
                }
            }
        }
    }
}