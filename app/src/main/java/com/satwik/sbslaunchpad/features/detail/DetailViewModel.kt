package com.satwik.sbslaunchpad.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result(isLoading = true))
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    fun loadDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = Result(isLoading = true)
            try {
                val detail = repository.getPostDetail(id)
                if (detail != null) {
                    _uiState.value = Result(success = true, successResult = detail)
                } else {
                    _uiState.value = Result(error = "Detail not found")
                }
            } catch (e: Exception) {
                _uiState.value = Result(error = e.message ?: "Unknown Error")
            }
        }
    }
}
