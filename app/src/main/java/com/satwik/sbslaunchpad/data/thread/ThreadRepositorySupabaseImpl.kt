package com.satwik.sbslaunchpad.data.thread

import com.satwik.sbslaunchpad.data.admin.Admin
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThreadRepositorySupabaseImpl(
    private val client: SupabaseClient
) : ThreadRepository {

    private fun getUserId(): String {
        return client.auth.currentUserOrNull()?.id ?: "unknown"
    }

    @OptIn(SupabaseExperimental::class)
    override fun getThreads(): Flow<List<Thread>> {
        val userId = getUserId()
        return client.postgrest.from("thread").selectAsFlow(
            primaryKey = Thread::id,
            filter = FilterOperation("reciever_id", FilterOperator.EQ, userId)
        )
    }

    @OptIn(SupabaseExperimental::class)
    override fun getMessages(threadId: Int): Flow<List<Message>> {
        return client.postgrest
            .from("message")
            .selectAsFlow(
                primaryKey = Message::id,
                filter = FilterOperation("thread_id", FilterOperator.EQ, threadId)
            )
    }

    override suspend fun markThreadAsRead(threadId: Int) {
        client.postgrest.from("thread").update(
            {
                set("unread_count", 0)
            }
        ) {
            filter {
                eq("id", threadId)
            }
        }
    }

    override suspend fun markMessagesAsRead(threadId: Int) {
        client.postgrest.from("message").update(
            {
                set("is_read", true)
            }
        ) {
            filter {
                eq("thread_id", threadId)
            }
        }
    }
}
