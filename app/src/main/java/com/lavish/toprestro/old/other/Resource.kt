package com.lavish.toprestro.old.other

sealed class Resource<T> {
    class Success<T>(result: T): Resource<T>()
    class Failure<T>(msg: String, result: T? = null): Resource<T>()
}
