package com.satwik.sbslaunchpad.data.application_system

import com.satwik.sbslaunchpad.data.application_system.Application
import kotlinx.coroutines.flow.Flow

interface ApplicationSystem {
    suspend fun sendApplication(postId: String, studentId: String)
    fun getApplicationsForStudent(studentId: String): Flow<List<Application>>
    fun getAllApplicants(postId: String): Flow<Int>
}