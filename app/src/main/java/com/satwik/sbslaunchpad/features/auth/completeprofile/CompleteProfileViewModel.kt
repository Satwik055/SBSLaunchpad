package com.satwik.sbslaunchpad.features.auth.completeprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.fileUploader.CloudFileUploader
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.profile.Profile
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UploadState(
    val isUploading: Boolean = false,
    val progress: Float = 0f,
    val url: String = "",
    val fileName: String = "",
    val fileByteArray: ByteArray? = null
)

class CompleteProfileViewModel(
    private val client: SupabaseClient,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val cloudFileUploader: CloudFileUploader
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result())
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _resumeUploadState = MutableStateFlow(UploadState())
    val resumeUploadState = _resumeUploadState.asStateFlow()

    private val _tenthMarksheetUploadState = MutableStateFlow(UploadState())
    val tenthMarksheetUploadState = _tenthMarksheetUploadState.asStateFlow()

    private val _twelfthMarksheetUploadState = MutableStateFlow(UploadState())
    val twelfthMarksheetUploadState = _twelfthMarksheetUploadState.asStateFlow()

    val isAnyFileUploading = combine(
        _resumeUploadState,
        _tenthMarksheetUploadState,
        _twelfthMarksheetUploadState
    ) { r, t10, t12 ->
        r.isUploading || t10.isUploading || t12.isUploading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val profile = profileRepository.profile

    fun onFileSelected(
        folderName: String,
        fileByteArray: ByteArray,
        fileName: String
    ) {
        val stateFlow = when (folderName) {
            "resume" -> _resumeUploadState
            "marksheet/tenth" -> _tenthMarksheetUploadState
            "marksheet/twelfth" -> _twelfthMarksheetUploadState
            else -> return
        }

        stateFlow.update { it.copy(fileName = fileName, fileByteArray = fileByteArray, progress = 0f) }
    }

    private suspend fun uploadFileInternal(
        folderName: String,
        stateFlow: MutableStateFlow<UploadState>
    ) {
        val bytes = stateFlow.value.fileByteArray ?: return
        val fileName = stateFlow.value.fileName
        val userId = client.auth.currentUserOrNull()?.id ?: "unknown"
        val path = "$userId/$folderName/$fileName"

        stateFlow.update { it.copy(isUploading = true) }
        cloudFileUploader.uploadFileProgress(folderName, fileName, bytes).collect { progress ->
            stateFlow.update { it.copy(progress = progress) }
        }
        stateFlow.update { it.copy(isUploading = false, url = path) }
    }

    fun updateProfile(
        semester: String,
        course: String,
        rollNumber: String,
        examRollNumber: String,
        backlogs: String,
        address: String
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }

                val user = client.auth.currentUserOrNull() ?: throw Exception("User not authenticated")

                // Upload files first
                uploadFileInternal("resume", _resumeUploadState)
                uploadFileInternal("marksheet/tenth", _tenthMarksheetUploadState)
                uploadFileInternal("marksheet/twelfth", _twelfthMarksheetUploadState)

                val updatedProfile = Profile(
                    id = user.id,
                    fullName = profileRepository.profile.first()?.fullName ?: "",
                    email = user.email ?: "",
                    semester = semester,
                    course = course,
                    rollNumber = rollNumber,
                    examRollNumber = examRollNumber,
                    backlogs = backlogs,
                    address = address,
                    tenthMarksheetUrl = _tenthMarksheetUploadState.value.url,
                    twelfthMarksheetUrl = _twelfthMarksheetUploadState.value.url,
                    profileImageUrl = "",
                    resumeUrl = _resumeUploadState.value.url,
                    isProfileCompleted = true
                )

                profileRepository.updateProfile(updatedProfile).getOrThrow()
                _uiState.update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
