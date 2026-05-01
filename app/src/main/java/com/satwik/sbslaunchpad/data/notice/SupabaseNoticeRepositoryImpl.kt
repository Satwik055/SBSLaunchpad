package com.satwik.sbslaunchpad.data.notice

import com.satwik.sbslaunchpad.data.notice.model.Notice
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

class SupabaseNoticeRepositoryImpl(
    private val client: SupabaseClient
) : NoticeRepository {

    private val tag = "Timber-${this::class.simpleName}"


    private fun getUserId(): String {
        return client.auth.currentUserOrNull()?.id ?: "unknown"
    }

    @OptIn(SupabaseExperimental::class)
    override fun getNotices(): Flow<List<Notice>> {
        val userId = getUserId()
        Timber.tag(tag).d("Fetching notices for user: %s", userId)
        return client.postgrest.from("notice").selectAsFlow(
            primaryKey = Notice::id,
            filter = FilterOperation("receiver_id", FilterOperator.EQ, userId)
        ).catch { e ->
            Timber.tag(tag).e(e, "Error fetching notices for user: %s", userId)
            throw e
        }
    }

    //TODO (Fix this function interface and impl)
    override suspend fun markNoticeAsRead(noticeId: Int) {
        val userId = getUserId()
        Timber.tag(tag).d("Marking notice %d as read for user: %s", noticeId, userId)

        try {
            client.postgrest.from("notice").update(
                {
                    set("is_read", true)
                }
            ) {
                filter {
                    eq("id", noticeId)
                    eq("receiver_id", userId)
                }
            }
            Timber.tag(tag).d("Successfully marked notice %d as read", noticeId)
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Error marking notice %d as read", noticeId)
            throw e
        }
    }
}
