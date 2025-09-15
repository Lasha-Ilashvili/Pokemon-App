package com.task.pokemon_app.data.common

import com.task.pokemon_app.domain.exception.ApiException
import com.task.pokemon_app.domain.exception.ErrorType
import com.task.pokemon_app.domain.exception.ErrorType.Companion.fromCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HandleResponse {

    fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<Result<T>> = flow {
        emit(Result.Loading(isLoading = true))

        val response = apiCall()
        if (!response.isSuccessful) throw HttpException(response)

        emit(Result.Success(data = response.body() ?: throw Exception("Response body is null")))
    }.catch { e ->
        emit(Result.Error(exception = e.toApiException()))
    }.onCompletion {
        emit(Result.Loading(isLoading = false))
    }

    private fun Throwable.toApiException(): ApiException {
        return when (this) {
            is HttpException -> ApiException(fromCode(code()), this)
            is ConnectException -> ApiException(ErrorType.CONNECTION_ERROR, this)
            is SocketTimeoutException -> ApiException(ErrorType.TIMEOUT_ERROR, this)
            is UnknownHostException -> ApiException(ErrorType.NO_INTERNET, this)
            is IOException -> ApiException(ErrorType.NETWORK_ERROR, this)
            else -> ApiException(ErrorType.UNKNOWN_ERROR, this)
        }
    }
}