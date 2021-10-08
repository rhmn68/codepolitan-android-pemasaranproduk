package com.codepolitan.pemasaranproduk.data.model

sealed class Resource<out R> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val errorMessage: String) : Resource<Nothing>()
    object Empty: Resource<Nothing>()
    object Loading: Resource<Nothing>()
}