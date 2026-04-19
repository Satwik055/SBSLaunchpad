@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.satwik.sbslaunchpad.data.application_system

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApplicationSystemSupabaseImpl(
    private val client: SupabaseClient,
): ApplicationSystem {
    override suspend fun sendApplication(postId: String, studentId: String) {
        val application = Application(
            postId = postId,
            studentId = studentId
        )
        client.postgrest.from("application").insert(application)
    }

    @OptIn(SupabaseExperimental::class)
    override fun getApplicationsForStudent(studentId: String): Flow<List<Application>> {
        return client.postgrest.from("application").selectAsFlow(
            primaryKey = Application::id,
            filter = FilterOperation("student_id", FilterOperator.EQ, studentId)
        )
    }

    @OptIn(SupabaseExperimental::class)
    override fun getAllApplicants(postId: String): Flow<Int> {
        return client.postgrest.from("application").selectAsFlow(
            primaryKey = Application::id,
            filter = FilterOperation("post_id", FilterOperator.EQ, postId)
        ).map { it.size }
    }
}