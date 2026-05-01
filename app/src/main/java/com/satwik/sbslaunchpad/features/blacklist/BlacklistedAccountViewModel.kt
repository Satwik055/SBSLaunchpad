package com.satwik.sbslaunchpad.features.blacklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import kotlinx.coroutines.launch

class BlacklistedAccountViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    //TODO(Handle all states)
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
