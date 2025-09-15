package com.task.pokemon_app.domain.exception

class ApiException(
    val errorType: ErrorType,
    cause: Throwable? = null
) : Exception(errorType.message, cause) {

    override val message: String get() = errorType.message
}