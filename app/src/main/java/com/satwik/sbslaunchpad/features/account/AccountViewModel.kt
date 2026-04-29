package com.satwik.sbslaunchpad.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.profile.RequestStatus
import com.satwik.sbslaunchpad.data.profile.ProfileUpdateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AccountViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _accountState = MutableStateFlow(Result(isLoading = true))
    val accountState: StateFlow<Result> = _accountState.asStateFlow()

    private val _latestUpdateRequest = MutableStateFlow(Result(isLoading = true))
    val latestUpdateRequest: StateFlow<Result> = _latestUpdateRequest.asStateFlow()

    init {
        loadAccount()
        loadLatestUpdateRequest()
    }

    private fun loadAccount() {
        viewModelScope.launch {
            _accountState.update { it.copy(isLoading = true) }
            profileRepository.profile.collect { account ->
                _accountState.update {
                    it.copy(
                        isLoading = false,
                        success = true,
                        successResult = account
                    )
                }
            }
        }
    }

    private fun loadLatestUpdateRequest() {
        viewModelScope.launch {
            _latestUpdateRequest.update { it.copy(isLoading = true) }
            try {
                profileRepository.getProfileUpdateRequest().collectLatest { request ->
                    _latestUpdateRequest.update {
                        it.copy(
                            isLoading = false,
                            success = true,
                            successResult = request
                        )
                    }
                }
            } catch (e: Exception) {
                _latestUpdateRequest.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load update requests"
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

    fun automaticallyMarkLatestRequestAsRead() {
        val request = _latestUpdateRequest.value.successResult as? ProfileUpdateRequest
        if (request != null && !request.isRead && request.status != RequestStatus.IN_REVIEW) {
            markRequestAsRead(request.id)
        }
    }

    fun markRequestAsRead(requestId: Int) {
        viewModelScope.launch {
            profileRepository.markProfileUpdateRequestAsRead(requestId)
        }
    }
}
