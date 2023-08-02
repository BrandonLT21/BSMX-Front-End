package com.example.bsmx

interface NetworkCallback {

    fun onSuccess()
    fun onError(errorMessage: String)
}