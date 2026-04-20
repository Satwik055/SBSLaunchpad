package com.satwik.sbslaunchpad.core.pushNotification


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class PushNotificationService: FirebaseMessagingService() {

    private val authRepository: AuthRepository by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        serviceScope.launch{
            try {
                authRepository.updateFcmToken(token)
            }
            catch (e: Exception){
                Log.d("PushNotificationService", "Error updating FCM token: ${e.message}")
            }
        }
    }

}

