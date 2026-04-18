package com.satwik.sbslaunchpad.data.auth

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val sessionStatus: Flow<SessionStatus>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, phone: String, password: String): Result<Unit>
    suspend fun logout()
}
