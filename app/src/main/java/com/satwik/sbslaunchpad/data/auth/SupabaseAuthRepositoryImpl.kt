package com.satwik.sbslaunchpad.data.auth

import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import timber.log.Timber

class SupabaseAuthRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val firebaseMessaging: FirebaseMessaging
) : AuthRepository {

    private val tag = "Timber-${this::class.simpleName}"


    override val sessionStatus: Flow<SessionStatus> = supabaseClient.auth.sessionStatus

    override val currentUserId: String?
        get() = supabaseClient.auth.currentUserOrNull()?.id

    override suspend fun login(email: String, password: String) {
        Timber.tag(tag).d("Attempting login for email: %s", email)
        try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Timber.tag(tag).d("Login successful for email: %s", email)
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Login failed for email: %s", email)
            throw e
        }
    }

    override suspend fun register(name: String, email: String, phone: String, password: String) {
        Timber.tag(tag).d("Attempting registration for email: %s", email)
        try {
            val fcmToken = firebaseMessaging.token.await()
            supabaseClient.auth.signUpWith(Email) {
                this.email = email.lowercase()
                this.password = password
                data = buildJsonObject {
                    put("full_name", name.lowercase())
                    put("fcm_token", fcmToken)
                    put("phone", phone)
                }
            }
            Timber.tag(tag).d("Registration successful for email: %s", email)
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Registration failed for email: %s", email)
            throw e
        }
    }

    override suspend fun logout() {
        Timber.tag(tag).d("Attempting logout")
        try {
            supabaseClient.auth.signOut()
            Timber.tag(tag).d("Logout successful")
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Logout failed")
            throw e
        }
    }

    override suspend fun updateFcmToken(token: String){
        val email = supabaseClient.auth.currentUserOrNull()?.email
        Timber.tag(tag).d("Updating FCM token for user: %s", email)
        try {
            supabaseClient.from("profile")
                .update(
                    mapOf("fcm_token" to token)
                ){
                    filter {
                        eq("email", email!!)
                    }
                }
            Timber.tag(tag).d("FCM token updated successfully!")
        }
        catch(e: Exception) {
            Timber.tag(tag).e(e, "Error updating FCM token")
            throw e
        }
    }

}
