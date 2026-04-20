package com.satwik.sbslaunchpad.data.auth

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.satwik.sbslaunchpad.core.pushNotification.PushNotificationService
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseAuthRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val firebaseMessaging: FirebaseMessaging
) : AuthRepository {

    override val sessionStatus: Flow<SessionStatus> = supabaseClient.auth.sessionStatus

    override val currentUserId: String?
        get() = supabaseClient.auth.currentUserOrNull()?.id

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

    override suspend fun register(name: String, email: String, phone: String, password: String): Result<Unit> {
        return try {

            val fcmToken = firebaseMessaging.token.await()
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("full_name", name)
                    put("fcm_token", fcmToken)
                }
            }
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

    override suspend fun updateFcmToken(token: String){
        val email = supabaseClient.auth.currentUserOrNull()?.email
        try {
            supabaseClient.from("profile")
                .update(
                    mapOf("fcm_token" to token)
                ){
                    filter {
                        eq("email", email!!)
                    }
                }
            Log.d("AuthRepositoryImpl",  "FCM token updated successfully!")
        }
        catch(e: Exception) {
            Log.d("AuthRepositoryImpl",  "Error updating FCM token: ${e.message}")
        }
    }

}
