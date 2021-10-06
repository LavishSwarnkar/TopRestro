package com.lavish.toprestro.featureLogin.domain.repository

import com.lavish.toprestro.featureOwner.domain.model.Profile

interface LoginRepository {

    suspend fun checkAccountExists(userType: String, email: String): Result<Profile>

    suspend fun createAccount(userType: String, profile: Profile): SimpleResult

}

sealed class SimpleResult {
    object Success: SimpleResult()
    data class Failure(val e: String): SimpleResult()
}

sealed class Result<T> {
    data class Success<T>(val t: T): Result<T>()
    data class Failure<T>(val e: String): Result<T>()
}