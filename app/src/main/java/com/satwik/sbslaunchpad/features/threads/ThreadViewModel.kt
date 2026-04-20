package com.satwik.sbslaunchpad.features.threads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.admin.AdminRepository
import com.satwik.sbslaunchpad.data.post.Post
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.thread.Message
import com.satwik.sbslaunchpad.data.thread.Thread
import com.satwik.sbslaunchpad.data.thread.ThreadRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

data class ThreadWithPost(
    val thread: Thread,
    val post: Post?
)

class ThreadViewModel(
    private val threadRepository: ThreadRepository,
    private val postRepository: PostRepository,
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        fetchThreads()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchThreads() {
        threadRepository.getThreads()
            .onStart { _uiState.value = Result(isLoading = true) }
            .flatMapLatest { threads ->
                if (threads.isEmpty()) {
                    return@flatMapLatest flowOf(emptyList<ThreadWithPost>())
                }

                val postFlows = threads.map { thread ->
                    postRepository.getPostById(thread.postId).map { post ->
                        ThreadWithPost(thread, post)
                    }
                }

                combine(postFlows) { it.toList() }
            }
            .onEach { threadsWithPosts ->
                _uiState.value = Result(success = true, successResult = threadsWithPosts, isLoading = false)
            }
            .catch { e ->
                _uiState.value = Result(error = e.message ?: "Unknown Error", isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getMessages(threadId: Int): Flow<Result> {
        return threadRepository.getMessages(threadId)
            .flatMapLatest { messages ->
                if (messages.isEmpty()) return@flatMapLatest flowOf(emptyList<Message>())

                val messageFlows = messages.map { message ->
                    flow {
                        val admin = if (message.admin == null) {
                            adminRepository.getAdminById(message.senderId)
                        } else {
                            message.admin
                        }
                        emit(message.copy(admin = admin))
                    }
                }

                combine(messageFlows) { it.toList() }
            }
            .map { messages ->
                Result(success = true, successResult = messages, isLoading = false)
            }
            .onStart { emit(Result(isLoading = true)) }
            .catch { e ->
                emit(Result(error = e.message ?: "Unknown Error", isLoading = false))
            }
    }

    fun markThreadAsRead(threadId: Int) {
        viewModelScope.launch {
            try {
                // Optimistic update for immediate feedback
                val currentState = _uiState.value
                if (currentState.success) {
                    val threadsWithPosts = currentState.successResult as? List<ThreadWithPost> ?: emptyList()
                    val updatedThreads = threadsWithPosts.map { item ->
                        if (item.thread.id == threadId) {
                            item.copy(thread = item.thread.copy(unreadCount = 0))
                        } else item
                    }
                    _uiState.value = currentState.copy(successResult = updatedThreads)
                }

                threadRepository.markThreadAsRead(threadId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markMessagesAsRead(threadId: Int) {
        viewModelScope.launch {
            try {
                threadRepository.markMessagesAsRead(threadId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
