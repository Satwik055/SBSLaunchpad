package com.satwik.sbslaunchpad.data.thread

import com.satwik.sbslaunchpad.data.thread.model.Thread
import com.satwik.sbslaunchpad.data.thread.model.ThreadMessage
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    fun getThreads(): Flow<List<Thread>>
    fun getMessages(threadId: Int): Flow<List<ThreadMessage>>
    suspend fun markThreadAsRead(threadId: Int)
    suspend fun markMessagesAsRead(threadId: Int)
}