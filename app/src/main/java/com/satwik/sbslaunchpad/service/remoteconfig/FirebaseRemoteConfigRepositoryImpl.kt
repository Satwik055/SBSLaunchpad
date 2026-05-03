package com.satwik.sbslaunchpad.service.remoteconfig

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class FirebaseRemoteConfigRepositoryImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {

    private val tag = "Timber-${this::class.simpleName}"

    override fun getMaintenanceStatus(): Flow<Boolean> = callbackFlow {
        Timber.tag(tag).d("Starting maintenance status flow")

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Timber.tag(tag).d("Remote Config updated. Updated keys: %s", configUpdate.updatedKeys)
                if (configUpdate.updatedKeys.contains(IS_UNDER_MAINTENANCE)) {
                    firebaseRemoteConfig.activate().addOnCompleteListener { task ->
                        val isMaintenance = firebaseRemoteConfig.getBoolean(IS_UNDER_MAINTENANCE)
                        Timber.tag(tag).d("Maintenance status updated: %b (Activate successful: %b)", isMaintenance, task.isSuccessful)
                        trySend(isMaintenance)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Timber.tag(tag).e(error, "Remote Config update error: %s", error.message)
            }
        }

        val registration = firebaseRemoteConfig.addOnConfigUpdateListener(listener)

        // Send initial value
        val initialValue = firebaseRemoteConfig.getBoolean(IS_UNDER_MAINTENANCE)
        Timber.tag(tag).d("Emitting initial maintenance status: %b", initialValue)
        trySend(initialValue)

        awaitClose {
            Timber.tag(tag).d("Closing maintenance status flow, removing listener")
            registration.remove()
        }
    }.onStart {
        Timber.tag(tag).d("Fetching and activating Remote Config")
        firebaseRemoteConfig.fetchAndActivate()
    }

    companion object {
        private const val IS_UNDER_MAINTENANCE = "is_under_maintenance"
    }
}
