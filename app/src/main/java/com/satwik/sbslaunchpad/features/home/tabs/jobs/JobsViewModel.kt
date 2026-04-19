package com.satwik.sbslaunchpad.features.home.tabs.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.application_system.ApplicationSystem
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.post.Post
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.PostType
import com.satwik.sbslaunchpad.data.profile.Profile
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class JobsViewModel(
    private val postRepository: PostRepository,
    private val applicationSystem: ApplicationSystem,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result(isLoading = true))
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _isAppliedLoading = MutableStateFlow(true)
    val isAppliedLoading: StateFlow<Boolean> = _isAppliedLoading.asStateFlow()

    private val _appliedPostIds = MutableStateFlow<Set<String>>(emptySet())
    val appliedPostIds: StateFlow<Set<String>> = _appliedPostIds.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    init {
        fetchJobs()
        fetchApplications()
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            profileRepository.profile.collectLatest {
                _profile.value = it
            }
        }
    }

    private fun fetchApplications() {
        viewModelScope.launch {
            _isAppliedLoading.value = true
            val userId = authRepository.currentUserId
            if (userId == null) {
                _isAppliedLoading.value = false
                return@launch
            }
            applicationSystem.getApplicationsForStudent(userId).collect { applications ->
                _appliedPostIds.value = applications.map { it.postId }.toSet()
                _isAppliedLoading.value = false
            }
        }
    }

    fun fetchJobs() {
        postRepository.getAllPostsByType(PostType.JOB)
            .onStart { _uiState.value = Result(isLoading = true) }
            .onEach { jobs -> _uiState.value = Result(success = true, successResult = jobs) }
            .catch { e -> _uiState.value = Result(error = e.message ?: "Unknown Error") }
            .launchIn(viewModelScope)
    }
}
