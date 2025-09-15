package com.task.pokemon_app.domain.exception

enum class ErrorType(val message: String) {
    BAD_REQUEST("Bad request. Please check your input."),
    NOT_FOUND("Requested data not found."),
    CONNECTION_ERROR("Unable to connect. Please check your internet connection."),
    TIMEOUT_ERROR("Connection timeout. Please try again."),
    NO_INTERNET("No internet connection. Please check your network."),
    NETWORK_ERROR("Network error. Please check your connection."),
    UNKNOWN_ERROR("Something went wrong. Please try again.");

    companion object {
        fun fromCode(code: Int): ErrorType {
            return when (code) {
                400 -> BAD_REQUEST
                404 -> NOT_FOUND
                else -> UNKNOWN_ERROR
            }
        }
    }
}