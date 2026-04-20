package com.satwik.sbslaunchpad.features.home.tabs.internships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.application.ApplicationRepository
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.PostType
import com.satwik.sbslaunchpad.data.profile.Profile
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class InternshipsViewModel(
    private val repository: PostRepository,
    private val applicationRepository: ApplicationRepository,
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

    private val _applicationState = MutableStateFlow(Result(isLoading = false))
    val applicationState: StateFlow<Result> = _applicationState.asStateFlow()

    private val _applicantsCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val applicantsCounts: StateFlow<Map<String, Int>> = _applicantsCounts.asStateFlow()

    init {
        fetchInternships()
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
            applicationRepository.getApplicationsForStudent(userId).collect { applications ->
                _appliedPostIds.value = applications.map { it.postId }.toSet()
                _isAppliedLoading.value = false
            }
        }
    }

    fun fetchInternships() {
        repository.getAllPostsByType(PostType.INTERNSHIP)
            .onStart { _uiState.value = Result(isLoading = true) }
            .onEach { internships ->
                _uiState.value = Result(success = true, successResult = internships)
                internships.forEach { internship ->
                    observeApplicantsCount(internship.id)
                }
            }
            .catch { e -> _uiState.value = Result(error = e.message ?: "Unknown Error") }
            .launchIn(viewModelScope)
    }

    private fun observeApplicantsCount(postId: String) {
        applicationRepository.getAllApplicants(postId)
            .onEach { count ->
                _applicantsCounts.value = _applicantsCounts.value.toMutableMap().apply {
                    put(postId, count)
                }
            }
            .launchIn(viewModelScope)
    }

    fun applyForPost(postId: String) {
        val userId = authRepository.currentUserId
        if (userId == null) {
            _applicationState.value = Result(error = "User not logged in")
            return
        }

        viewModelScope.launch {
            _applicationState.value = Result(isLoading = true)
            try {
                applicationRepository.sendApplication(postId, userId)
                _applicationState.value = Result(success = true)
            } catch (e: Exception) {
                _applicationState.value = Result(error = e.message ?: "Failed to send application")
            }
        }
    }
}
