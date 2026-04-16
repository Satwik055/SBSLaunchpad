package com.satwik.sbslaunchpad.data.auth

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryDummyImpl : AuthRepository {
    private val _sessionStatus = MutableStateFlow<SessionStatus>(SessionStatus.NotAuthenticated())
    override val sessionStatus: Flow<SessionStatus> = _sessionStatus.asStateFlow()

    private var loggedIn = false

    override suspend fun login(email: String, password: String): Result<Unit> {
        delay(1000)
        return if (email.isNotEmpty() && password.length >= 6) {
            loggedIn = true
            // Since this is a dummy and Session might be hard to construct, 
            // we might just keep it NotAuthenticated or find a way to mock Session if needed.
            // For now, let's just fix the compilation error by implementing the property.
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    override suspend fun register(email: String, phone: String, password: String): Result<Unit> {
        delay(1000)
        return if (email.isNotEmpty() && password.length >= 6 && phone.length >= 10) {
            loggedIn = true
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid registration details"))
        }
    }

    override suspend fun logout() {
        loggedIn = false
        _sessionStatus.value = SessionStatus.NotAuthenticated(isSignOut = true)
    }

    override fun isUserLoggedIn(): Boolean {
        return loggedIn
    }
}
