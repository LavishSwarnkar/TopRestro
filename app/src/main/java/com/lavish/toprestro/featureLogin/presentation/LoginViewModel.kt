package com.lavish.toprestro.featureLogin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavish.toprestro.featureLogin.domain.usecases.LoginResult
import com.lavish.toprestro.featureLogin.domain.usecases.LoginUseCases
import com.lavish.toprestro.featureOwner.domain.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@DelicateCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private var _loginEvents = MutableStateFlow<LoginEvent>(LoginEvent.Empty)
    var loginEvent: StateFlow<LoginEvent> = _loginEvents

    lateinit var emailId: String
    lateinit var type: String

    fun postLogin(emailId: String, userType: String){
        this.emailId = emailId
        this.type = userType

        viewModelScope.launch {
            try {
                val loginResult = loginUseCases.login(userType, emailId)

                _loginEvents.emit(
                    when(loginResult) {
                        is LoginResult.LoginSuccessful ->
                            LoginEvent.WelcomeAndNavigateUser(loginResult.userName)

                        is LoginResult.AccountDoesNotExist ->
                            LoginEvent.InputName

                        is LoginResult.AdminAccessDenied ->
                            LoginEvent.AdminAccessDenied
                    }
                )

            } catch (e: Exception) {
                _loginEvents.emit(LoginEvent.Error(e.message.toString()))
            }
        }
    }

    fun createAccount(name: String) {
        val profile = Profile(name, emailId)

        viewModelScope.launch {
            try {
                loginUseCases.createAccount(type, profile)
            } catch (e: Exception) {
                _loginEvents.emit(LoginEvent.Error(e.message.toString()))
            }
        }
    }

}

sealed class LoginEvent {
    data class WelcomeAndNavigateUser(val name: String): LoginEvent()
    object InputName : LoginEvent()
    object AdminAccessDenied : LoginEvent()
    data class Error(val e: String): LoginEvent()
    object Empty: LoginEvent()
}