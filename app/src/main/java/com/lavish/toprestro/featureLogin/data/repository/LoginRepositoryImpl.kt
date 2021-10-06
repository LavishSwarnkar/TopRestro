package com.lavish.toprestro.featureLogin.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.featureLogin.domain.repository.LoginRepository
import com.lavish.toprestro.featureLogin.domain.repository.Result
import com.lavish.toprestro.featureLogin.domain.repository.SimpleResult
import com.lavish.toprestro.featureOwner.domain.model.Profile
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl: LoginRepository {

    override suspend fun checkAccountExists(userType: String, email: String): Result<Profile> {
        try {
            val profile = FirebaseFirestore.getInstance()
                .document("${userType}/${email}")
                .get().await().toObject(Profile::class.java)!!

            return Result.Success(profile)
        } catch (e: Exception) {
            return Result.Failure(e.message.toString())
        }
    }

    override suspend fun createAccount(userType: String, profile: Profile): SimpleResult {
        try {
            FirebaseFirestore.getInstance()
                .document("${userType}/${profile.emailId}")
                .set(profile).await()

            return SimpleResult.Success
        } catch (e: Exception) {
            return SimpleResult.Failure(e.message.toString())
        }
    }

}