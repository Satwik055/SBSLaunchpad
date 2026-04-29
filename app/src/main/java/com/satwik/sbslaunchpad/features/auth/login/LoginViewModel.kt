package com.satwik.sbslaunchpad.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result())
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged ->
                _formState.update { it.copy(email = event.email, emailError = null) }

            is LoginFormEvent.PasswordChanged ->
                _formState.update { it.copy(password = event.password, passwordError = null) }

            LoginFormEvent.Submit ->
                submitData()
        }
    }

    private fun submitData() {
        val email = _formState.value.email
        val password = _formState.value.password

        var emailError: String? = null
        var passwordError: String? = null

        if (email.isBlank()) {
            emailError = "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
        }

        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
        }

        if (emailError != null || passwordError != null) {
            _formState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
        } else {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    fun login(onSuccess: () -> Unit) {
        val email = _formState.value.email
        val password = _formState.value.password
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                authRepository.login(email, password).getOrThrow()
                _uiState.update { it.copy(isLoading = false, success = true) }
                onSuccess()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = when (e) {
                            is AuthRestException -> when (e.error) {
                                "invalid_credentials" -> "Invalid email or password"
                                else -> e.description ?: "Login failed"
                            }

                            is HttpRequestException -> "Network error"
                            is RestException -> e.description ?: "Server error"
                            else -> "Login failed"
                        }
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = "") }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
