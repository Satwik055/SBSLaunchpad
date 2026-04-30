package com.satwik.sbslaunchpad.features.profilerejected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.profile.model.Profile
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.satwik.sbslaunchpad.data.profile.model.ProfileStatus
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ProfileRejectedViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result())
    val uiState = _uiState.asStateFlow()

    fun resubmit() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                val currentProfile = profileRepository.profile.firstOrNull()
                if (currentProfile != null) {
                    profileRepository.updateProfile(
                        uuid= currentProfile.id,
                        profile = Profile(status = ProfileStatus.PROFILE_COMPLETION_REQUIRED)
                    )
                    _uiState.update { it.copy(isLoading = false, success = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Profile not found") }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.localizedMessage ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = "") }
    }
}
