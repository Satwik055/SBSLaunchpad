package com.satwik.sbslaunchpad.features.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AccountViewModel(
    private val repository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _accountState = MutableStateFlow(Result(isLoading = true))
    val accountState: StateFlow<Result> = _accountState.asStateFlow()

    init {
        loadAccount()
    }


    private fun loadAccount() {
        viewModelScope.launch {
            _accountState.update { it.copy(isLoading = true) }
            repository.profile.collect { account ->
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

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
