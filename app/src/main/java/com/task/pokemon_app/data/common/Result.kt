package com.task.pokemon_app.data.common

import com.task.pokemon_app.domain.exception.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: ApiException) : Result<Nothing>()
    data class Loading(val isLoading: Boolean) : Result<Nothing>()
}

inline fun <Dto : Any, Domain : Any> Flow<Result<Dto>>.onSuccess(
    crossinline onSuccess: (Dto) -> Domain
): Flow<Result<Domain>> = map {
    when (it) {
        is Result.Success -> Result.Success(data = onSuccess(it.data))
        is Result.Error -> Result.Error(exception = it.exception)
        is Result.Loading -> Result.Loading(isLoading = it.isLoading)
    }
}