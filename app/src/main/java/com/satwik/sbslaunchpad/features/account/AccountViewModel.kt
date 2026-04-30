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

    private fun loadLatestProfileUpdateRequest() {
        viewModelScope.launch {
            _latestUpdateRequest.update { it.copy(isLoading = true) }
            try {
                profileUpdateRequestRepository.getProfileUpdateRequest().collectLatest { request ->
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
        _markRequestAsReadState.value = Result()
    }
}
