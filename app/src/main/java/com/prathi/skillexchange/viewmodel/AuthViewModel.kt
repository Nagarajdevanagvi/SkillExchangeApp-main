package com.prathi.skillexchange.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathi.skillexchange.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.Idle)

    val authState: StateFlow<AuthState> = _authState

    val currentUser =
        authRepository.getCurrentUserId()

    // SET ERROR
    fun setError(message: String) {

        _authState.value =
            AuthState.Error(message)
    }

    // EMAIL LOGIN
    fun login(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _authState.value =
                AuthState.Loading

            val result =
                authRepository.login(
                    email,
                    password
                )

            result.onSuccess {

                _authState.value =
                    AuthState.Success(it)

            }.onFailure {

                _authState.value =
                    AuthState.Error(

                        it.message
                            ?: "Login Failed"
                    )
            }
        }
    }

    // EMAIL REGISTER
    fun register(
        name: String,
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _authState.value =
                AuthState.Loading

            val result =
                authRepository.register(
                    name,
                    email,
                    password
                )

            result.onSuccess {

                _authState.value =
                    AuthState.Success(it)

            }.onFailure {

                _authState.value =
                    AuthState.Error(

                        it.message
                            ?: "Registration Failed"
                    )
            }
        }
    }

    // SEND OTP
    fun sendOtp(
        phone: String,
        activity: Activity
    ) {

        _authState.value =
            AuthState.Loading

        authRepository.sendOtp(

            phone = phone,

            activity = activity,

            onCodeSent = {

                _authState.value =
                    AuthState.CodeSent
            },

            onError = { error ->

                _authState.value =
                    AuthState.Error(error)
            }
        )
    }

    // VERIFY OTP
    fun verifyOtp(
        otp: String
    ) {

        viewModelScope.launch {

            _authState.value =
                AuthState.Loading

            val result =
                authRepository.verifyOtp(otp)

            result.onSuccess {

                _authState.value =
                    AuthState.Success(it)

            }.onFailure {

                _authState.value =
                    AuthState.Error(

                        it.message
                            ?: "OTP Verification Failed"
                    )
            }
        }
    }

    // LOGOUT
    fun logout() {

        authRepository.logout()

        _authState.value =
            AuthState.Idle
    }

    // RESET
    fun resetState() {

        _authState.value =
            AuthState.Idle
    }
}

sealed class AuthState {

    object Idle : AuthState()

    object Loading : AuthState()

    object CodeSent : AuthState()

    data class Success(
        val message: String
    ) : AuthState()

    data class Error(
        val message: String
    ) : AuthState()
}