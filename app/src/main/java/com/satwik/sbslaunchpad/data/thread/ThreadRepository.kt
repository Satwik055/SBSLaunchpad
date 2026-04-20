package com.satwik.sbslaunchpad.data.thread

import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    fun getThreads(): Flow<List<Thread>>
    fun getMessages(threadId: Int): Flow<List<Message>>
    suspend fun markThreadAsRead(threadId: Int)
    suspend fun markMessagesAsRead(threadId: Int)
}