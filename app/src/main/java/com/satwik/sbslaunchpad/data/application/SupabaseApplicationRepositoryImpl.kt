@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.satwik.sbslaunchpad.data.application

import com.satwik.sbslaunchpad.data.application.model.Application
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class SupabaseApplicationRepositoryImpl(
    private val client: SupabaseClient,
): ApplicationRepository {

    private val tag = "Timber-${this::class.simpleName}"


    override suspend fun sendApplication(postId: String, userId: String) {
        Timber.tag(tag).d("Sending application for postId: %s, studentId: %s", postId, userId)
        val application = Application(
            postId = postId,
            studentId = userId
        )
        try {
            client.postgrest.from("application").insert(application)
            Timber.tag(tag).d("Application sent successfully")
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Error sending application")
            throw e
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getUserApplicationsById(userId: String): Flow<List<Application>> {
        Timber.tag(tag).d("Getting applications for student: %s", userId)
        return client.postgrest.from("application").selectAsFlow(
            primaryKey = Application::id,
            filter = FilterOperation("student_id", FilterOperator.EQ, userId)
        ).catch { e ->
            Timber.tag(tag).e(e, "Error getting applications for student: %s", userId)
            throw e
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getPostApplicationsById(postId: String): Flow<List<Application>> {
        Timber.tag(tag).d("Getting applications for post: %s", postId)
        return client.postgrest.from("application").selectAsFlow(
            primaryKey = Application::id,
            filter = FilterOperation("post_id", FilterOperator.EQ, postId)
        ).catch { e ->
            Timber.tag(tag).e(e, "Error getting applications for post: %s", postId)
            throw e
        }
    }
}