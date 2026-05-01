package com.satwik.sbslaunchpad.features.completeprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.service.storage.CloudFileUploader
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.auth.AuthRepository
import com.satwik.sbslaunchpad.data.new_profile_request.model.NewProfileRequest
import com.satwik.sbslaunchpad.data.new_profile_request.NewProfileRequestRepository
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


//TODO(Major cleanup required in this vm)
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
    private val newProfileRequestRepository: NewProfileRequestRepository,
    private val cloudFileUploader: CloudFileUploader
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result())
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(CompleteProfileFormState())
    val formState: StateFlow<CompleteProfileFormState> = _formState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

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

    fun onEvent(event: CompleteProfileFormEvent) {
        when (event) {
            is CompleteProfileFormEvent.SemesterChanged -> {
                if (event.semester.all { it.isDigit() }) {
                    _formState.update { it.copy(semester = event.semester, semesterError = null) }
                }
            }
            is CompleteProfileFormEvent.CourseChanged -> _formState.update { it.copy(course = event.course, courseError = null) }
            is CompleteProfileFormEvent.RollNumberChanged -> _formState.update { it.copy(rollNumber = event.rollNumber, rollNumberError = null) }
            is CompleteProfileFormEvent.BacklogsChanged -> {
                if (event.backlogs.all { it.isDigit() }) {
                    _formState.update { it.copy(backlogs = event.backlogs, backlogsError = null) }
                }
            }
            is CompleteProfileFormEvent.CurrentAddressChanged -> _formState.update { it.copy(currentAddress = event.currentAddress, currentAddressError = null) }
            is CompleteProfileFormEvent.PermanentAddressChanged -> _formState.update { it.copy(permanentAddress = event.permanentAddress, permanentAddressError = null) }
            is CompleteProfileFormEvent.CgpaChanged -> {
                if (event.cgpa.all { it.isDigit() }) {
                    _formState.update { it.copy(cgpa = event.cgpa, cgpaError = null) }
                }
            }
            is CompleteProfileFormEvent.YearChanged -> {
                if (event.year.all { it.isDigit() }) {
                    _formState.update { it.copy(year = event.year, yearError = null) }
                }
            }
            is CompleteProfileFormEvent.TenthMarksPercentageChanged -> {
                if (event.tenthMarksPercentage.all { it.isDigit() || it == '.' } && event.tenthMarksPercentage.count { it == '.' } <= 1) {
                    _formState.update { it.copy(tenthMarksPercentage = event.tenthMarksPercentage, tenthMarksPercentageError = null) }
                }
            }
            is CompleteProfileFormEvent.TwelfthMarksPercentageChanged -> {
                if (event.twelfthMarksPercentage.all { it.isDigit() || it == '.' } && event.twelfthMarksPercentage.count { it == '.' } <= 1) {
                    _formState.update { it.copy(twelfthMarksPercentage = event.twelfthMarksPercentage, twelfthMarksPercentageError = null) }
                }
            }
            CompleteProfileFormEvent.Submit -> submitData()
        }
    }

    private fun submitData() {
        val state = _formState.value
        val semesterError = if (state.semester.isBlank()) "Semester cannot be empty" else null
        val courseError = if (state.course.isBlank()) "Course cannot be empty" else null
        val rollNumberError = if (state.rollNumber.isBlank()) "Roll number cannot be empty" else null
        val backlogsError = if (state.backlogs.isBlank()) "Backlogs cannot be empty" else null
        val currentAddressError = if (state.currentAddress.isBlank()) "Current address cannot be empty" else null
        val permanentAddressError = if (state.permanentAddress.isBlank()) "Permanent address cannot be empty" else null
        val cgpaValue = state.cgpa.toDoubleOrNull()
        val cgpaError =
            if (state.cgpa.isBlank()){
                "CGPA cannot be empty"
            } else if (cgpaValue == null) {
                "Invalid CGPA format"
            } else if (cgpaValue !in 0.0..100.0){
                "Cgpa should be between 0 - 10"
            } else {
                null
            }

        val yearError = if (state.year.isBlank()) "Year cannot be empty" else null

        val tenthMarksValue = state.tenthMarksPercentage.toDoubleOrNull()
        val tenthMarksPercentageError =
            if (state.tenthMarksPercentage.isBlank()){
                "10th marks percentage cannot be empty"
            } else if (tenthMarksValue == null) {
                "Invalid percentage format"
            } else if (tenthMarksValue !in 0.0..100.0) {
                "Percentage should be between 0 - 100"
            } else {
                null
            }

        val twelfthMarksValue = state.twelfthMarksPercentage.toDoubleOrNull()
        val twelfthMarksPercentageError =
            if (state.twelfthMarksPercentage.isBlank()){
                "12th marks percentage cannot be empty"
            } else if (twelfthMarksValue == null) {
                "Invalid percentage format"
            } else if (twelfthMarksValue !in 0.0..100.0) {
                "Percentage should be between 0 - 100"
            } else {
                null
            }
        
        val resumeError = if (_resumeUploadState.value.fileByteArray == null) "Resume is required" else null
        val tenthMarksheetError = if (_tenthMarksheetUploadState.value.fileByteArray == null) "10th marksheet is required" else null
        val twelfthMarksheetError = if (_twelfthMarksheetUploadState.value.fileByteArray == null) "12th marksheet is required" else null

        if (semesterError != null || courseError != null || rollNumberError != null || backlogsError != null ||
            currentAddressError != null || permanentAddressError != null || cgpaError != null || yearError != null ||
            tenthMarksPercentageError != null || twelfthMarksPercentageError != null || 
            resumeError != null || tenthMarksheetError != null || twelfthMarksheetError != null
        ) {
            _formState.update {
                it.copy(
                    semesterError = semesterError,
                    courseError = courseError,
                    rollNumberError = rollNumberError,
                    backlogsError = backlogsError,
                    currentAddressError = currentAddressError,
                    permanentAddressError = permanentAddressError,
                    cgpaError = cgpaError,
                    yearError = yearError,
                    tenthMarksPercentageError = tenthMarksPercentageError,
                    twelfthMarksPercentageError = twelfthMarksPercentageError,
                    resumeError = resumeError,
                    tenthMarksheetError = tenthMarksheetError,
                    twelfthMarksheetError = twelfthMarksheetError
                )
            }
        } else {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    fun onFileSelected(
        folderName: String,
        fileByteArray: ByteArray,
        fileName: String
    ) {
        val stateFlow = when (folderName) {
            "resume" -> {
                _formState.update { it.copy(resumeError = null) }
                _resumeUploadState
            }
            "marksheet/tenth" -> {
                _formState.update { it.copy(tenthMarksheetError = null) }
                _tenthMarksheetUploadState
            }
            "marksheet/twelfth" -> {
                _formState.update { it.copy(twelfthMarksheetError = null) }
                _twelfthMarksheetUploadState
            }
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

    fun updateProfile() {
        val state = _formState.value
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }

                val user = client.auth.currentUserOrNull() ?: throw Exception("User not authenticated")

                // Upload files first
                uploadFileInternal("resume", _resumeUploadState)
                uploadFileInternal("marksheet/tenth", _tenthMarksheetUploadState)
                uploadFileInternal("marksheet/twelfth", _twelfthMarksheetUploadState)

                val newProfileRequest = NewProfileRequest(
                    requestedBy = user.id,
                    fullName = profileRepository.profile.first()?.fullName ?: "",
                    email = user.email ?: "",
                    semester = state.semester.toIntOrNull() ?: 0,
                    course = state.course,
                    rollNumber = state.rollNumber,
                    backlogs = state.backlogs.toIntOrNull() ?: 0,
                    permanentAddress = state.permanentAddress,
                    currentAddress = state.currentAddress,
                    cgpa = state.cgpa.toFloatOrNull() ?: 0f,
                    year = state.year.toIntOrNull() ?: 0,
                    tenthMarksPercentage = state.tenthMarksPercentage.toFloatOrNull() ?: 0f,
                    twelfthMarksPercentage = state.twelfthMarksPercentage.toFloatOrNull() ?: 0f,
                    tenthMarksheetUrl = _tenthMarksheetUploadState.value.url,
                    twelfthMarksheetUrl = _twelfthMarksheetUploadState.value.url,
                    profileImageUrl = "",
                    resumeUrl = _resumeUploadState.value.url,
                )

                newProfileRequestRepository.createNewProfileRequest(newProfileRequest)
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

    fun clearError() {
        _uiState.update { it.copy(error = "") }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
