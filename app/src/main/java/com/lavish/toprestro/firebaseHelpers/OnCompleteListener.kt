package com.lavish.toprestro.firebaseHelpers

interface OnCompleteListener<T> {
    fun onResult(t: T)
    fun onError(e: String)
}