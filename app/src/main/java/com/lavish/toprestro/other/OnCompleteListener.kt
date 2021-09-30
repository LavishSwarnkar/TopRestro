package com.lavish.toprestro.other

interface OnCompleteListener<T> {
    fun onResult(t: T)
    fun onError(e: String)
}