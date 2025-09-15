package com.task.pokemon_app.di

import androidx.paging.PagingSource
import com.task.pokemon_app.data.data_source.PokemonListPagingSource
import com.task.pokemon_app.data.model.PokemonListDto.PokemonDto
import com.task.pokemon_app.data.repository.PokemonListRepositoryImpl
import com.task.pokemon_app.data.service.PokemonListService
import com.task.pokemon_app.domain.repository.PokemonListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bindPokemonRepository(impl: PokemonListRepositoryImpl): PokemonListRepository

    @Binds
    fun bindPokemonPagingSource(impl: PokemonListPagingSource): PagingSource<String, PokemonDto>

    companion object {

        @Provides
        fun providePokemonListService(retrofit: Retrofit): PokemonListService {
            return retrofit.create(PokemonListService::class.java)
        }
    }
}