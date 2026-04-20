package com.satwik.sbslaunchpad.data.notice

import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNotices(): Flow<List<Notice>>
    
    suspend fun markNoticeAsRead(noticeId: Int)

}
