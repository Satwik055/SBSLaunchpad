package com.satwik.sbslaunchpad.features.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.profile.model.Profile
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import com.satwik.sbslaunchpad.data.profile.model.ProfileStatus
import com.satwik.sbslaunchpad.data.profile_update_request.model.ProfileUpdateRequest
import com.satwik.sbslaunchpad.data.profile_update_request.ProfileUpdateRequestRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class EditProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val profileUpdateRequestRepository: ProfileUpdateRequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(Result())
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(EditProfileFormState())
    val formState: StateFlow<EditProfileFormState> = _formState.asStateFlow()

    private val _isChanged = MutableStateFlow(false)
    val isChanged: StateFlow<Boolean> = _isChanged.asStateFlow()

    private var initialFormState: EditProfileFormState? = null

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            profileRepository.profile.collect { profile ->
                profile?.let { setProfileDataToInitialFormState(it) }
            }
        }
    }

    private fun setProfileDataToInitialFormState(p: Profile) {
        val newState = EditProfileFormState(
            fullName = p.fullName,
            email = p.email,
            course = p.course,
            semester = p.semester.toString(),
            phone = p.phone,
            backlogs = p.backlogs.toString(),
            rollNumber = p.rollNumber,
            cgpa = p.cgpa.toString(),
            tenthMarksPercentage = p.tenthMarksPercentage.toString(),
            twelfthMarksPercentage = p.twelfthMarksPercentage.toString(),
            year = p.year.toString(),
            permanentAddress = p.permanentAddress,
            currentAddress = p.currentAddress,
            profileImageUrl = p.profileImageUrl,
            status = p.status,
            rejectionReason = if (p.status == ProfileStatus.REJECTED) "Your request was rejected" else null
        )

        if (initialFormState == null) {
            initialFormState = newState
        }

        _formState.update { newState }
    }

    fun hasChanges(): Boolean {
        val current = _formState.value
        val initial = initialFormState ?: return false
        
        return current.fullName != initial.fullName ||
                current.email != initial.email ||
                current.course != initial.course ||
                current.semester != initial.semester ||
                current.phone != initial.phone ||
                current.backlogs != initial.backlogs ||
                current.rollNumber != initial.rollNumber ||
                current.cgpa != initial.cgpa ||
                current.tenthMarksPercentage != initial.tenthMarksPercentage ||
                current.twelfthMarksPercentage != initial.twelfthMarksPercentage ||
                current.year != initial.year ||
                current.permanentAddress != initial.permanentAddress ||
                current.currentAddress != initial.currentAddress
    }

    fun onEvent(event: EditProfileFormEvent) {
        when (event) {
            is EditProfileFormEvent.NameChanged -> _formState.update { it.copy(fullName = event.name, fullNameError = null) }
            is EditProfileFormEvent.EmailChanged -> _formState.update { it.copy(email = event.email, emailError = null) }
            is EditProfileFormEvent.CourseChanged -> _formState.update { it.copy(course = event.course, courseError = null) }
            is EditProfileFormEvent.SemesterChanged -> {
                if (event.semester.all { it.isDigit() }) {
                    _formState.update { it.copy(semester = event.semester, semesterError = null) }
                }
            }
            is EditProfileFormEvent.PhoneChanged -> _formState.update { it.copy(phone = event.phone, phoneError = null) }
            is EditProfileFormEvent.BacklogsChanged -> {
                if (event.backlogs.all { it.isDigit() }) {
                    _formState.update { it.copy(backlogs = event.backlogs, backlogsError = null) }
                }
            }
            is EditProfileFormEvent.RollNumberChanged -> _formState.update { it.copy(rollNumber = event.rollNumber, rollNumberError = null) }
            is EditProfileFormEvent.CgpaChanged -> _formState.update { it.copy(cgpa = event.cgpa, cgpaError = null) }
            is EditProfileFormEvent.TenthMarksPercentageChanged -> _formState.update { it.copy(tenthMarksPercentage = event.percentage, tenthMarksPercentageError = null) }
            is EditProfileFormEvent.TwelfthMarksPercentageChanged -> _formState.update { it.copy(twelfthMarksPercentage = event.percentage, twelfthMarksPercentageError = null) }
            is EditProfileFormEvent.YearChanged -> {
                if (event.year.all { it.isDigit() }) {
                    _formState.update { it.copy(year = event.year, yearError = null) }
                }
            }
            is EditProfileFormEvent.PermanentAddressChanged -> _formState.update { it.copy(permanentAddress = event.address, permanentAddressError = null) }
            is EditProfileFormEvent.CurrentAddressChanged -> _formState.update { it.copy(currentAddress = event.address, currentAddressError = null) }
            EditProfileFormEvent.Submit -> submitData()
        }
        _isChanged.value = hasChanges()
    }

    private fun submitData() {
        val state = _formState.value
        val nameError = if (state.fullName.isBlank()) "Name cannot be empty" else null
        val emailError = if (state.email.isBlank()) "Email cannot be empty" else null
        val courseError = if (state.course.isBlank()) "Course cannot be empty" else null
        val semesterError = if (state.semester.isBlank()) "Semester cannot be empty" else null
        val phoneError = if (state.phone.isBlank()) "Phone cannot be empty" else null
        val backlogsError = if (state.backlogs.isBlank()) "Backlogs cannot be empty" else null
        val rollNumberError = if (state.rollNumber.isBlank()) "Roll number cannot be empty" else null
        val cgpaError = if (state.cgpa.isBlank()) "CGPA cannot be empty" else null
        val tenthMarksError = if (state.tenthMarksPercentage.isBlank()) "10th marks cannot be empty" else null
        val twelfthMarksError = if (state.twelfthMarksPercentage.isBlank()) "12th marks cannot be empty" else null
        val yearError = if (state.year.isBlank()) "Year cannot be empty" else null
        val permanentAddressError = if (state.permanentAddress.isBlank()) "Permanent address cannot be empty" else null
        val currentAddressError = if (state.currentAddress.isBlank()) "Current address cannot be empty" else null

        if (nameError != null || emailError != null || courseError != null || 
            semesterError != null || phoneError != null || backlogsError != null || 
            rollNumberError != null || cgpaError != null || tenthMarksError != null || 
            twelfthMarksError != null || yearError != null || permanentAddressError != null || 
            currentAddressError != null) {
            _formState.update {
                it.copy(
                    fullNameError = nameError,
                    emailError = emailError,
                    courseError = courseError,
                    semesterError = semesterError,
                    phoneError = phoneError,
                    backlogsError = backlogsError,
                    rollNumberError = rollNumberError,
                    cgpaError = cgpaError,
                    tenthMarksPercentageError = tenthMarksError,
                    twelfthMarksPercentageError = twelfthMarksError,
                    yearError = yearError,
                    permanentAddressError = permanentAddressError,
                    currentAddressError = currentAddressError
                )
            }
        } else {
            viewModelScope.launch {
                createProfileUpdateRequest()
            }
        }
    }

    private suspend fun createProfileUpdateRequest() {
        val state = _formState.value
        try {
            _uiState.update { it.copy(isLoading = true, error = "") }
            val currentProfile = profileRepository.profile.first() ?: return
            
            val updated = currentProfile.copy(
                fullName = state.fullName,
                email = state.email,
                course = state.course,
                semester = state.semester.toIntOrNull() ?: 0,
                phone = state.phone,
                backlogs = state.backlogs.toIntOrNull() ?: 0,
                rollNumber = state.rollNumber,
                cgpa = state.cgpa.toDoubleOrNull() ?: 0.0,
                tenthMarksPercentage = state.tenthMarksPercentage.toDoubleOrNull() ?: 0.0,
                twelfthMarksPercentage = state.twelfthMarksPercentage.toDoubleOrNull() ?: 0.0,
                year = state.year.toIntOrNull() ?: 0,
                permanentAddress = state.permanentAddress,
                currentAddress = state.currentAddress
            )

            val requestedChanges = buildJsonObject {
                if (currentProfile.fullName != updated.fullName) put("full_name", updated.fullName)
                if (currentProfile.email != updated.email) put("email", updated.email)
                if (currentProfile.twelfthMarksheetUrl != updated.twelfthMarksheetUrl) put("twelfth_marksheet_url", updated.twelfthMarksheetUrl)
                if (currentProfile.tenthMarksheetUrl != updated.tenthMarksheetUrl) put("tenth_marksheet_url", updated.tenthMarksheetUrl)
                if (currentProfile.profileImageUrl != updated.profileImageUrl) put("profile_image_url", updated.profileImageUrl)
                if (currentProfile.course != updated.course) put("course", updated.course)
                if (currentProfile.semester != updated.semester) put("semester", updated.semester)
                if (currentProfile.phone != updated.phone) put("phone", updated.phone)
                if (currentProfile.backlogs != updated.backlogs) put("backlogs", updated.backlogs)
                if (currentProfile.cgpa != updated.cgpa) put("cgpa", updated.cgpa)
                if (currentProfile.tenthMarksPercentage != updated.tenthMarksPercentage) put("tenth_marks_percentage", updated.tenthMarksPercentage)
                if (currentProfile.twelfthMarksPercentage != updated.twelfthMarksPercentage) put("twelfth_marks_percentage", updated.twelfthMarksPercentage)
                if (currentProfile.postSelectedIn != updated.postSelectedIn) put("post_selected_in", updated.postSelectedIn)
                if (currentProfile.year != updated.year) put("year", updated.year)
                if (currentProfile.rollNumber != updated.rollNumber) put("roll_number", updated.rollNumber)
                if (currentProfile.permanentAddress != updated.permanentAddress) put("permanent_address", updated.permanentAddress)
                if (currentProfile.currentAddress != updated.currentAddress) put("current_address", updated.currentAddress)
                if (currentProfile.resumeUrl != updated.resumeUrl) put("resume_url", updated.resumeUrl)
            }

            if (requestedChanges.isNotEmpty()) {
                val updateRequest = ProfileUpdateRequest(
                    requestedBy = currentProfile.id,
                    requestedChanges = requestedChanges
                )
                profileUpdateRequestRepository.createProfileUpdateRequest(updateRequest)
            }

            _uiState.update { it.copy(isLoading = false, success = true) }
            validationEventChannel.send(ValidationEvent.Success)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.update { it.copy(isLoading = false, error = e.message ?: "Something went wrong") }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = "") }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
