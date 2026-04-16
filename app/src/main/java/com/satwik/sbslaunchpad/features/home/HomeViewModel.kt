package com.satwik.sbslaunchpad.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.PostType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PostRepository) : ViewModel() {

    private val _jobsState = MutableStateFlow(Result(isLoading = true))
    val jobsState: StateFlow<Result> = _jobsState.asStateFlow()

    private val _internshipsState = MutableStateFlow(Result(isLoading = true))
    val internshipsState: StateFlow<Result> = _internshipsState.asStateFlow()

    init {
        fetchHomeData()
    }

    private fun fetchHomeData() {
        viewModelScope.launch {
            _jobsState.value = Result(isLoading = true)
            try {
                val jobs = repository.getAllPostsByType(PostType.JOB)
                _jobsState.value = Result(success = true, successResult = jobs)
            } catch (e: Exception) {
                _jobsState.value = Result(error = e.message ?: "Unknown Error")
            }
        }
        viewModelScope.launch {
            _internshipsState.value = Result(isLoading = true)
            try {
                val internships = repository.getAllPostsByType(PostType.INTERNSHIP)
                _internshipsState.value = Result(success = true, successResult = internships)
            } catch (e: Exception) {
                _internshipsState.value = Result(error = e.message ?: "Unknown Error")
            }
        }
    }
}
