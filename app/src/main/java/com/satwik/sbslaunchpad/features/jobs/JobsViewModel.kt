package com.satwik.sbslaunchpad.features.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.ErrorMessages
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.application.ApplicationRepository
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.post.PostRepository
import com.satwik.sbslaunchpad.data.post.model.PostType
import com.satwik.sbslaunchpad.data.profile.model.Profile
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class JobsViewModel(
    private val postRepository: PostRepository,
    private val applicationRepository: ApplicationRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _jobState = MutableStateFlow(Result())
    val jobState: StateFlow<Result> = _jobState.asStateFlow()

    private val _userApplicationState = MutableStateFlow(Result())
    val userApplicationState: StateFlow<Result> = _userApplicationState.asStateFlow()

    private val _applicantsCountState = MutableStateFlow(Result())
    val applicantsCountState: StateFlow<Result> = _applicantsCountState.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    init {
        getProfile()
        fetchJobs()
        getApplicationsOfUser()
    }

    private fun getProfile() {
        profileRepository.profile.onEach { profile ->
            _profile.value = profile
        }.launchIn(viewModelScope)
    }

    fun fetchJobs() {
        postRepository.getAllPostsByType(PostType.JOB)
            .onStart {
                _jobState.update { it.copy(isLoading = true, error = "") }
            }
            .onEach { jobs ->
                _jobState.update {
                    it.copy(
                        isLoading = false,
                        success = true,
                        successResult = jobs,
                        error = ""
                    )
                }
                jobs.forEach { job ->
                    observeApplicantsCount(job.id)
                }
            }
            .catch { e ->
                _jobState.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorMessages.JOB_SCREEN_ERROR
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getApplicationsOfUser() {
        val userId = authRepository.currentUserId
        if (userId == null) {
            _userApplicationState.update { it.copy(isLoading = false) }
            return
        }

        applicationRepository.getUserApplicationsById(userId)
            .onStart {
                _userApplicationState.update { it.copy(isLoading = true, error = "") }
            }
            .onEach { applications ->
                _userApplicationState.update {
                    it.copy(
                        isLoading = false,
                        success = true,
                        successResult = applications.map { it.postId }.toSet(),
                        error = ""
                    )
                }
            }
            .catch { e ->
                _userApplicationState.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorMessages.JOB_SCREEN_ERROR
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeApplicantsCount(postId: String) {
        applicationRepository.getPostApplicationsById(postId)
            .onStart { _applicantsCountState.update { it.copy(isLoading = true) } }
            .onEach { applications ->
                _applicantsCountState.update { state ->
                    @Suppress("UNCHECKED_CAST")
                    val currentMap = (state.successResult as? Map<String, Int>) ?: emptyMap()
                    state.copy(
                        isLoading = false,
                        success = true,
                        successResult = currentMap + (postId to applications.size),
                        error = ""
                    )
                }
            }
            .catch { e ->
                _applicantsCountState.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorMessages.JOB_SCREEN_ERROR
                    )
                }
            }
            .launchIn(viewModelScope)
    }

}
