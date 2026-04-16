package com.satwik.sbslaunchpad.features.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.account.Account
import com.satwik.sbslaunchpad.data.account.AccountRepository
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AccountRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _accountState = MutableStateFlow(Result(isLoading = true))
    val accountState: StateFlow<Result> = _accountState.asStateFlow()

    init {
        loadAccount()
    }

    private fun loadAccount() {
        viewModelScope.launch {
            _accountState.update { it.copy(isLoading = true) }
            try {
                val account = repository.getAccount()
                _accountState.update {
                    it.copy(
                        isLoading = false,
                        success = true,
                        successResult = account
                    )
                }
            } catch (e: Exception) {
                _accountState.update {
                    it.copy(
                        isLoading = false,
                        success = false,
                        error = e.message ?: "Unknown Error"
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

    fun updateProfile(account: Account) {
        viewModelScope.launch {
            repository.updateAccount(account)
            _accountState.update {
                it.copy(successResult = account)
            }
        }
    }
}
