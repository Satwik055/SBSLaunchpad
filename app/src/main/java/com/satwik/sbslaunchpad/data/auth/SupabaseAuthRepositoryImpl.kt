package com.satwik.sbslaunchpad.data.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import io.github.jan.supabase.auth.providers.builtin.Email

class SupabaseAuthRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override val sessionStatus: Flow<SessionStatus> = supabaseClient.auth.sessionStatus

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, phone: String, password: String): Result<Unit> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            // Note: Phone number storage might require additional database entry depending on your schema
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            supabaseClient.auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return supabaseClient.auth.currentSessionOrNull() != null
    }
}
