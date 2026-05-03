package com.satwik.sbslaunchpad.service.remoteconfig

import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {
    fun getMaintenanceStatus(): Flow<Boolean>
}