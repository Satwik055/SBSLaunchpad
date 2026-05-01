package com.satwik.sbslaunchpad.data.thread

import com.satwik.sbslaunchpad.data.thread.model.Thread
import com.satwik.sbslaunchpad.data.thread.model.ThreadMessage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber

class SupabaseThreadRepositoryImpl(
    private val client: SupabaseClient
) : ThreadRepository {

    private val tag = "Timber-${this::class.simpleName}"

    private fun getUserId(): String {
        return client.auth.currentUserOrNull()?.id ?: "unknown"
    }

    @OptIn(SupabaseExperimental::class)
    override fun getThreads(): Flow<List<Thread>> {
        val userId = getUserId()
        Timber.tag(tag).d("Fetching threads for user: %s", userId)
        return client.postgrest.from("thread").selectAsFlow(
            primaryKey = Thread::id,
            filter = FilterOperation("reciever_id", FilterOperator.EQ, userId)
        ).catch { e ->
            Timber.tag(tag).e(e, "Error fetching threads for user: %s", userId)
            throw e
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getMessages(threadId: Int): Flow<List<ThreadMessage>> {
        Timber.tag(tag).d("Fetching messages for thread: %d", threadId)
        return client.postgrest
            .from("message")
            .selectAsFlow(
                primaryKey = ThreadMessage::id,
                filter = FilterOperation("thread_id", FilterOperator.EQ, threadId)
            ).catch { e ->
                Timber.tag(tag).e(e, "Error fetching messages for thread: %d", threadId)
                throw e
            }
    }

    //TODO (Fix this function interface and impl)
    override suspend fun markThreadAsRead(threadId: Int) {
        Timber.tag(tag).d("Marking thread %d as read", threadId)
        try {
            client.postgrest.from("thread").update(
                {
                    set("unread_count", 0)
                }
            ) {
                filter {
                    eq("id", threadId)
                }
            }
            Timber.tag(tag).d("Successfully marked thread %d as read", threadId)
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Error marking thread %d as read", threadId)
            throw e
        }
    }

    //TODO (Fix this function interface and impl)
    override suspend fun markMessagesAsRead(threadId: Int) {
        Timber.tag(tag).d("Marking messages in thread %d as read", threadId)
        try {
            client.postgrest.from("message").update(
                {
                    set("is_read", true)
                }
            ) {
                filter {
                    eq("thread_id", threadId)
                }
            }
            Timber.tag(tag).d("Successfully marked messages in thread %d as read", threadId)
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Error marking messages in thread %d as read", threadId)
            throw e
        }
    }
}
