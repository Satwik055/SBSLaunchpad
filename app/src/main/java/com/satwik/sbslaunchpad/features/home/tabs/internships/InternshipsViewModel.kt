package com.satwik.sbslaunchpad.features.home.tabs.internships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.PostType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class InternshipsViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result(isLoading = true))
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    init {
        fetchInternships()
    }

    fun fetchInternships() {
        repository.getAllPostsByType(PostType.INTERNSHIP)
            .onStart { _uiState.value = Result(isLoading = true) }
            .onEach { internships -> _uiState.value = Result(success = true, successResult = internships) }
            .catch { e -> _uiState.value = Result(error = e.message ?: "Unknown Error") }
            .launchIn(viewModelScope)
    }
}
