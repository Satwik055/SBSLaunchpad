package com.satwik.sbslaunchpad.features.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.notice.NoticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NoticeScreenViewModel(
    private val noticeRepository: NoticeRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(Result(isLoading = true))
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    init {
        getNotices()
    }

    fun getNotices() {
        noticeRepository.getNotices()
            .onStart { _uiState.value = Result(isLoading = true) }
            .onEach { notices ->
                _uiState.value = Result(success = true, successResult = notices)
            }
            .catch { e ->
                _uiState.value = Result(error = e.message ?: "Unknown Error")
            }
            .launchIn(viewModelScope)
    }

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
