package com.satwik.sbslaunchpad.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.core.util.ErrorMessages
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.profile_update_request.model.ProfileUpdateRequest
import com.satwik.sbslaunchpad.data.profile_update_request.ProfileUpdateRequestRepository
import timber.log.Timber
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AccountViewModel(
    private val profileRepository: ProfileRepository,
    private val profileUpdateRequestRepository: ProfileUpdateRequestRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val tag = "Timber-${this::class.simpleName}"
    private val _accountState = MutableStateFlow(Result(isLoading = true))
    val accountState: StateFlow<Result> = _accountState.asStateFlow()
    private val _latestUpdateRequest = MutableStateFlow(Result(isLoading = true))
    val latestUpdateRequest: StateFlow<Result> = _latestUpdateRequest.asStateFlow()

    init {
        loadAccount()
        loadLatestProfileUpdateRequest()
    }

    //TODO(Handle error state here)
    private fun loadAccount() {
        Timber.tag(tag).d("Loading account profile...")
        viewModelScope.launch {
            _accountState.update { it.copy(isLoading = true) }
            profileRepository.profile.collect { account ->
                Timber.tag(tag).d("Account profile loaded successfully: %s", account)
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

    private fun loadLatestProfileUpdateRequest() {
        Timber.tag(tag).d("Loading latest profile update request...")
        viewModelScope.launch {
            _latestUpdateRequest.update { it.copy(isLoading = true) }
            try {
                profileUpdateRequestRepository.getProfileUpdateRequest().collectLatest { request ->
                    Timber.tag(tag).d("Latest profile update request loaded: %s", request)
                    _latestUpdateRequest.update {
                        it.copy(
                            isLoading = false,
                            success = true,
                            successResult = request
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.tag(tag).e(e, "Failed to load latest profile update request | Error: ${e.message}")
                _latestUpdateRequest.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load update requests"
                    )
                }
            }
        }
    }

    //TODO(Handle all state here)
    fun logout() {
        Timber.tag(tag).d("Attempting logout...")
        viewModelScope.launch {
            try {
                authRepository.logout()
                Timber.tag(tag).d("Logout successful")
            } catch (e: Exception) {
                Timber.tag(tag).e(e, "Logout failed")
            }
        }
    }

    private val _markRequestAsReadState = MutableStateFlow(Result())
    val markRequestAsReadState: StateFlow<Result> = _markRequestAsReadState.asStateFlow()

    fun markLatestRequestAsRead() {
        val request = (_latestUpdateRequest.value.successResult as? ProfileUpdateRequest) ?: return
        Timber.tag(tag).d("Updating profile-update-request table is_read value to true | Id: %s", request.id)
        viewModelScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    profileUpdateRequestRepository.updateProfileUpdateRequest(id=request.id, ProfileUpdateRequest(isRead = true))
                }.onSuccess {
                    Timber.tag(tag).d("Successfully updated profile-update-request table is_read value to true | Id: ${request.id}")
                    _markRequestAsReadState.value = Result(success = true)
                }.onFailure { e ->
                    Timber.tag(tag).e(e, "Failed to update profile-update-request table is_read value to true | Id: ${request.id} | Error: ${e.message}")
                    _markRequestAsReadState.value = Result(error = ErrorMessages.MARK_AS_READ_ERROR)
                }
            }
        }
    }

    fun resetMarkAsReadState() {
        Timber.tag(tag).d("Resetting mark-as-read state")
        _markRequestAsReadState.value = Result()
    }
}
