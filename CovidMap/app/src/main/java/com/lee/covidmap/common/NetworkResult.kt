package com.lee.covidmap.common

sealed class NetworkResult<T> {
    data class Loading<T>(val isLoading: Boolean) : NetworkResult<T>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Exception<T>(val errorMessage: String) : NetworkResult<T>()
    data class Failure<T>(val code: Int) : NetworkResult<T>()
}