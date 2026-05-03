package com.satwik.sbslaunchpad.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import com.satwik.sbslaunchpad.data.profile.model.ProfileStatus
import com.satwik.sbslaunchpad.core.util.NetworkMonitor
import com.satwik.sbslaunchpad.service.remoteconfig.RemoteConfigRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class AppState {
    data object Loading : AppState()
    data object LoginRequired : AppState()
    data object ProfileCompletionRequired : AppState()

    data object ProfileRejected : AppState()
    data object ProfileInReview : AppState()
    data object Blacklisted : AppState()
    data object UnderMaintenance : AppState()


    data object Authorized : AppState()
}

class MainViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val networkMonitor: NetworkMonitor,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    private val tag = "Timber-${this::class.simpleName}"

    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    private val _isRetrying = MutableStateFlow(false)
    val isRetrying = _isRetrying.asStateFlow()

    val isOnline = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    init {
        observeState()
    }

    private var observeJob: Job? = null

    fun retry() {
        Timber.tag(tag).d("Retry triggered")
        viewModelScope.launch {
            _isRetrying.value = true
            observeState()
            delay(1000) // Show progress for at least 1 second
            _isRetrying.value = false
        }
    }

    private fun observeState() {
        Timber.tag(tag).d("Observing app state")
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            combine(
                authRepository.sessionStatus,
                profileRepository.profile,
                remoteConfigRepository.getMaintenanceStatus()
            ) { status, profile, isUnderMaintenance ->
                Timber.tag(tag).d("State update - Maintenance: %b, Session: %s, Profile loaded: %b", 
                    isUnderMaintenance, status::class.simpleName, profile != null)
                
                if (isUnderMaintenance) {
                    AppState.UnderMaintenance
                } else {
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
                                    profile.status == ProfileStatus.IN_REVIEW -> AppState.ProfileInReview
                                    profile.status == ProfileStatus.REJECTED -> AppState.ProfileRejected
                                    profile.status == ProfileStatus.PROFILE_COMPLETION_REQUIRED -> AppState.ProfileCompletionRequired
                                    else -> AppState.Authorized
                                }
                            }
                        }

                        is SessionStatus.NotAuthenticated -> AppState.LoginRequired
                        is SessionStatus.Initializing -> AppState.Loading
                        is SessionStatus.RefreshFailure -> AppState.LoginRequired
                    }
                }
            }.catch { e ->
                Timber.tag(tag).e(e, "Error in observeState: %s", e.message)
                // When an error occurs (like no internet), we might want to stay in Loading
                // or move to Authorized if we want the screen-level offline check to take over.
                // If it's a network error, the flow might terminate, so we should handle it.
            }.collectLatest { newState ->
                Timber.tag(tag).d("Setting new AppState: %s", newState::class.simpleName)
                _appState.value = newState
            }
        }
    }
}
