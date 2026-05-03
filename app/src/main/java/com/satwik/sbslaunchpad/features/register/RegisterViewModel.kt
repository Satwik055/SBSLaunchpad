package com.satwik.sbslaunchpad.features.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.features.completeprofile.FormValidator
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

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result())
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(RegisterFormState())
    val formState: StateFlow<RegisterFormState> = _formState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.NameChanged ->
                _formState.update { it.copy(name = event.name, nameError = null) }

            is RegisterFormEvent.EmailChanged ->
                _formState.update { it.copy(email = event.email, emailError = null) }

            is RegisterFormEvent.PhoneChanged ->
                _formState.update { it.copy(phone = event.phone, phoneError = null) }

            is RegisterFormEvent.PasswordChanged ->
                _formState.update { it.copy(password = event.password, passwordError = null) }

            is RegisterFormEvent.ConfirmPasswordChanged ->
                _formState.update { it.copy(confirmPassword = event.confirmPassword, confirmPasswordError = null) }

            RegisterFormEvent.Submit ->
                submitData()
        }
    }

    private fun submitData() {
        val state = _formState.value

        val nameError = FormValidator.validateName(state.name)
        val emailError = FormValidator.validateEmail(state.email)
        val phoneError = FormValidator.validatePhone(state.phone)
        val passwordError = FormValidator.validatePassword(state.password)
        val confirmPasswordError = FormValidator.validateConfirmPassword(state.password, state.confirmPassword)

        if (nameError != null || emailError != null || phoneError != null || passwordError != null || confirmPasswordError != null) {
            _formState.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    phoneError = phoneError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
        } else {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    fun register(onSuccess: () -> Unit) {
        val state = _formState.value
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                authRepository.register(state.name, state.email, state.phone, state.password)
                _uiState.update { it.copy(isLoading = false, success = true) }
                onSuccess()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = when (e) {
                            is AuthRestException -> when (e.error) {
                                "user_already_exists" -> "Email already exists"
                                "weak_password" -> "Weak password"
                                else -> e.description ?: "Registration failed"
                            }
                            is HttpRequestException -> "Network error"
                            is RestException -> e.description ?: "Server error"
                            else -> "Registration failed"
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
