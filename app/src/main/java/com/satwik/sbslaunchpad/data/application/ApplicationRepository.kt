package com.satwik.sbslaunchpad.data.application

import kotlinx.coroutines.flow.Flow

interface ApplicationRepository {
    suspend fun sendApplication(postId: String, studentId: String)
    fun getApplicationsForStudent(studentId: String): Flow<List<Application>>
    fun getAllApplicants(postId: String): Flow<Int>
}