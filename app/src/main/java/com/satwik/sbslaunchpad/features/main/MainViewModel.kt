package com.satwik.sbslaunchpad.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed class AppState {
    data object Loading : AppState()
    data object LoginRequired : AppState()
    data object ProfileCompletionRequired : AppState()
    data object VerificationPending : AppState()
    data object Blacklisted : AppState()
    data object Authorized : AppState()
}

class MainViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    init {
        observeState()
    }

    private fun observeState() {
        val startTime = System.currentTimeMillis()

        viewModelScope.launch {
            combine(
                authRepository.sessionStatus,
                profileRepository.profile
            ) { status, profile ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        // Check if session is just starting or refreshing
                        if (profile == null) {
                            // During the transition, if we have a session but no profile yet, 
                            // keep the Loading state instead of flashing ProfileCompletionRequired
                            AppState.Loading
                        } else {
                            when {
                                profile.isBlacklisted -> AppState.Blacklisted
                                !profile.isProfileCompleted -> AppState.ProfileCompletionRequired
                                !profile.isVerified -> AppState.VerificationPending
                                else -> AppState.Authorized
                            }
                        }
                    }

                    is SessionStatus.NotAuthenticated -> AppState.LoginRequired
                    is SessionStatus.Initializing -> AppState.Loading
                    is SessionStatus.RefreshFailure -> AppState.LoginRequired
                }
            }.collectLatest { newState ->
                //Splash Screen logic
                if (newState != AppState.Loading) {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    if (elapsedTime < 2000) {
                        delay(2000 - elapsedTime)
                    }
                }
                _appState.value = newState
            }
        }
    }
}
