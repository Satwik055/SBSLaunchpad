package com.satwik.sbslaunchpad.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.application.ApplicationRepository
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.post.Post
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.profile.Profile
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: PostRepository,
    private val applicationRepository: ApplicationRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result(isLoading = true))
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _applicationState = MutableStateFlow(Result(isLoading = false))
    val applicationState: StateFlow<Result> = _applicationState.asStateFlow()

    private val _isAppliedLoading = MutableStateFlow(true)
    val isAppliedLoading: StateFlow<Boolean> = _isAppliedLoading.asStateFlow()

    private val _isApplied = MutableStateFlow(false)
    val isApplied: StateFlow<Boolean> = _isApplied.asStateFlow()

    private val _isEligible = MutableStateFlow(true)
    val isEligible: StateFlow<Boolean> = _isEligible.asStateFlow()

    private val _eligibilityReasons = MutableStateFlow<List<String>>(emptyList())
    val eligibilityReasons: StateFlow<List<String>> = _eligibilityReasons.asStateFlow()

    private val _applicantsCount = MutableStateFlow(0)
    val applicantsCount: StateFlow<Int> = _applicantsCount.asStateFlow()

    fun loadDetail(id: String) {
        repository.getPostById(id)
            .onStart {
                _uiState.value = Result(isLoading = true)
                _isAppliedLoading.value = true
            }
            .onEach { detail ->
                if (detail != null) {
                    _uiState.value = Result(success = true, successResult = detail)
                    checkEligibilityAndApplication(detail)
                    loadApplicantsCount(id)
                } else {
                    _uiState.value = Result(error = "Detail not found")
                    _isAppliedLoading.value = false
                }
            }
            .catch { e ->
                _uiState.value = Result(error = e.message ?: "Unknown Error")
                _isAppliedLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    private fun checkEligibilityAndApplication(post: Post) {
        viewModelScope.launch {
            _isAppliedLoading.value = true
            val userId = authRepository.currentUserId
            if (userId == null) {
                _isAppliedLoading.value = false
                return@launch
            }

            combine(
                applicationRepository.getApplicationsForStudent(userId),
                profileRepository.profile
            ) { applications, profile ->
                _isApplied.value = applications.any { it.postId == post.id }
                if (profile != null) {
                    val (eligible, reasons) = checkEligibility(profile, post)
                    _isEligible.value = eligible
                    _eligibilityReasons.value = reasons
                } else {
                    _isEligible.value = false
                    _eligibilityReasons.value = listOf("Profile not found")
                }
                _isAppliedLoading.value = false
            }.launchIn(this)
        }
    }

    private fun checkEligibility(profile: Profile, post: Post): Pair<Boolean, List<String>> {
        val reasons = mutableListOf<String>()

        val courseMatch = post.reqCourses.isEmpty() || post.reqCourses.any { it.equals(profile.course, ignoreCase = true) }
        if (!courseMatch) reasons.add("Course does not match. Required: ${post.reqCourses.joinToString(", ")}")

        val cgpaMatch = profile.cgpa >= post.reqCgpa
        if (!cgpaMatch) reasons.add("CGPA is below requirement. Required: ${post.reqCgpa}")

        val yearMatch = post.reqYear.isEmpty() || post.reqYear.contains(profile.year)
        if (!yearMatch) reasons.add("Year of study does not match. Required: ${post.reqYear.joinToString(", ")}")

        val tenthMatch = profile.tenthMarksPercentage >= post.reqTenthMarksPercentage
        if (!tenthMatch) reasons.add("10th marks are below requirement. Required: ${post.reqTenthMarksPercentage}%")

        val twelfthMatch = profile.twelfthMarksPercentage >= post.reqTwelfthMarksPercentage
        if (!twelfthMatch) reasons.add("12th marks are below requirement. Required: ${post.reqTwelfthMarksPercentage}%")

        val backlogMatch = post.reqBacklog == null || (profile.backlogs.toIntOrNull() ?: 0) <= post.reqBacklog
        if (!backlogMatch) reasons.add("Backlogs exceed the limit. Maximum allowed: ${post.reqBacklog}")

        return (reasons.isEmpty()) to reasons
    }

    private fun loadApplicantsCount(postId: String) {
        applicationRepository.getAllApplicants(postId)
            .onEach { count ->
                _applicantsCount.value = count
            }
            .launchIn(viewModelScope)
    }

    fun applyForPost(postId: String) {
        viewModelScope.launch {
            _applicationState.value = Result(isLoading = true)
            val userId = authRepository.currentUserId
            if (userId == null) {
                _applicationState.value = Result(error = "User not logged in")
                return@launch
            }
            try {
                applicationRepository.sendApplication(postId, userId)
                _applicationState.value = Result(success = true)
                _isApplied.value = true
            } catch (e: Exception) {
                println(e.message)
                _applicationState.value = Result(error = e.message ?: "Failed to send application")
            }
        }
    }
}
