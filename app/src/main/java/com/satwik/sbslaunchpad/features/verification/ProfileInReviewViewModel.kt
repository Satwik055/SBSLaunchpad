package com.satwik.sbslaunchpad.features.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import kotlinx.coroutines.launch

class ProfileInReviewViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
