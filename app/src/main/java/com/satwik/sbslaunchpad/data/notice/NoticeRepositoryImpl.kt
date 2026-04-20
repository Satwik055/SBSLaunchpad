package com.satwik.sbslaunchpad.data.notice

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow

class NoticeRepositoryImpl(
    private val client: SupabaseClient
) : NoticeRepository {

    private fun getUserId(): String {
        return client.auth.currentUserOrNull()?.id ?: "unknown"
    }

    @OptIn(SupabaseExperimental::class)
    override fun getNotices(): Flow<List<Notice>> {
        val userId = getUserId()
        return client.postgrest.from("notice").selectAsFlow(
            primaryKey = Notice::id,
            filter = FilterOperation("reciever_id", FilterOperator.EQ, userId)
        )
    }

    override suspend fun markNoticeAsRead(noticeId: Int) {
        client.postgrest.from("notice").update(
            {
                set("is_read", true)
            }
        ) {
            filter {
                eq("id", noticeId)
            }
        }
    }
}
