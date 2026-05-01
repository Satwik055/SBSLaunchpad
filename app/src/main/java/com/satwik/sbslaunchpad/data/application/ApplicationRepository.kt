package com.satwik.sbslaunchpad.data.application

import com.satwik.sbslaunchpad.data.application.model.Application
import kotlinx.coroutines.flow.Flow

interface ApplicationRepository {
    suspend fun createApplication(postId: String, userId: String)
    fun getUserApplicationsById(userId: String): Flow<List<Application>>
    fun getPostApplicationsById(postId: String): Flow<List<Application>>
}