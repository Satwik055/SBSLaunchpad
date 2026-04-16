package com.satwik.sbslaunchpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isInitializing = MutableStateFlow(true)
    val isInitializing: StateFlow<Boolean> = _isInitializing.asStateFlow()

    init {
        observeSessionStatus()
    }

    private fun observeSessionStatus() {
        viewModelScope.launch {
            authRepository.sessionStatus.collectLatest { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        _isLoggedIn.value = true
                        _isInitializing.value = false
                    }
                    is SessionStatus.NotAuthenticated -> {
                        _isLoggedIn.value = false
                        _isInitializing.value = false
                    }
                    is SessionStatus.Initializing -> {
                        _isInitializing.value = true
                    }
                    is SessionStatus.RefreshFailure -> _isInitializing.value = true
                }
            }
        }
    }
}
