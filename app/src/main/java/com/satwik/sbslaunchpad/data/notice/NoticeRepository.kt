package com.satwik.sbslaunchpad.data.notice

import com.satwik.sbslaunchpad.data.notice.model.Notice
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNotices(): Flow<List<Notice>>
    
    suspend fun markNoticeAsRead(noticeId: Int)

}
