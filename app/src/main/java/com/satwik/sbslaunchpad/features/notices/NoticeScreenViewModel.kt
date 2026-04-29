package com.satwik.sbslaunchpad.features.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.notice.NoticeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoticeScreenViewModel(
    private val noticeRepository: NoticeRepository
): ViewModel() {

    val uiState: StateFlow<Result> = noticeRepository.getNotices()
        .map { Result(success = true, successResult = it) }
        .onStart { emit(Result(isLoading = true)) }
        .catch { emit(Result(error = it.message ?: "Unknown Error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result(isLoading = true)
        )

    fun markNoticeAsRead(noticeId: Int) {
        viewModelScope.launch {
            try {
                noticeRepository.markNoticeAsRead(noticeId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
