package com.lavish.toprestro.old.firebaseHelpers

interface OnCompleteListener<T> {
    fun onResult(t: T)
    fun onError(e: String)
}