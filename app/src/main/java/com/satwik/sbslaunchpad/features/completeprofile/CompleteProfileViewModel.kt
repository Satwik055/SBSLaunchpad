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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import timber.log.Timber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UploadState

        if (isUploading != other.isUploading) return false
        if (progress != other.progress) return false
        if (url != other.url) return false
        if (fileName != other.fileName) return false
        if (fileByteArray != null) {
            if (other.fileByteArray == null) return false
            if (!fileByteArray.contentEquals(other.fileByteArray)) return false
        } else if (other.fileByteArray != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isUploading.hashCode()
        result = 31 * result + progress.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + (fileByteArray?.contentHashCode() ?: 0)
        return result
    }
}


/**
 * ViewModel for the Complete Profile screen, implemented using Unidirectional Data Flow (UDF).
 *
 * Architecture Flow:
 * 1. **UI Triggers Events**: The UI layer does not modify state directly. Instead, it
 * dispatches [CompleteProfileFormEvent]s to the [onEvent] method.
 * 2. **State Processing**: The ViewModel processes these events, updating the
 * [_formState] or [_filesState] accordingly.
 * 3. **State Observation**: The UI observes the immutable [formState] and [uiState]
 * to reflect changes, ensuring a single source of truth.
 * 4. **Side Effects**: One-time events (like successful validation) are sent via
 * the [validationEventChannel] to be handled as UI effects (e.g., navigation).
 */
class CompleteProfileViewModel(
    private val client: SupabaseClient,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val newProfileRequestRepository: NewProfileRequestRepository,
    private val cloudFileUploader: CloudFileUploader
) : ViewModel() {

    private val tag = "Timber-${this::class.simpleName}"

    //Complete profile feature uses this bucket only
    private val bucketName = "new_profile_request"

    private val _uiState = MutableStateFlow(Result())
    val uiState: StateFlow<Result> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(CompleteProfileFormState())
    val formState: StateFlow<CompleteProfileFormState> = _formState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val _filesState = MutableStateFlow(
        mapOf(
            DocumentType.Resume to UploadState(),
            DocumentType.TenthMarksheet to UploadState(),
            DocumentType.TwelfthMarksheet to UploadState(),
            DocumentType.ProfilePic to UploadState()
        )
    )
    private val uploadJobs = mutableMapOf<DocumentType, Job?>()

    val isAnyFileUploading = _filesState.map { stateMap: Map<DocumentType, UploadState> ->
        stateMap.values.any { it.isUploading }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val resumeUploadState = _filesState.map { it[DocumentType.Resume] ?: UploadState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UploadState())

    val tenthMarksheetUploadState = _filesState.map { it[DocumentType.TenthMarksheet] ?: UploadState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UploadState())

    val twelfthMarksheetUploadState = _filesState.map { it[DocumentType.TwelfthMarksheet] ?: UploadState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UploadState())

    val profileImageUploadState = _filesState.map { it[DocumentType.ProfilePic] ?: UploadState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UploadState())

    val profile = profileRepository.profile

    init {
        loadExistingTempFiles()
    }

    /**
     * Attempts to recover any previously uploaded temporary files from Supabase storage.
     *
     * This method is executed on ViewModel initialization. It scans the storage folders
     * associated with each [DocumentType] for the current user. If a file is found, it
     * updates the [_filesState] to reflect that the file is already available, preventing
     * the user from having to re-upload documents if they leave and return to the screen.
     */
    private fun loadExistingTempFiles() {
        viewModelScope.launch {
            val userId = client.auth.currentUserOrNull()?.id ?: return@launch
            DocumentType::class.sealedSubclasses.forEach { kClass ->
                val documentType = kClass.objectInstance ?: return@forEach
                try {
                    val folderPath = "temp/$userId/${documentType.folderName}"
                    val files = cloudFileUploader.listFiles(bucketName, folderPath)
                    if (files.isNotEmpty()) {
                        val fileName = files.first()
                        val path = "$folderPath/$fileName"
                        _filesState.update { it + (documentType to it.getValue(documentType).copy(
                                fileName = fileName,
                                url = path,
                                progress = 1f
                            )) }
                    }
                } catch (e: Exception) {
                    Timber.tag(tag).e(e, "Failed to load existing temp files for ${documentType.folderName}")
                }
            }
        }
    }

    fun onEvent(event: CompleteProfileFormEvent) {
        when (event) {
            is CompleteProfileFormEvent.SemesterChanged -> {
                _formState.update { it.copy(semester = event.semester, semesterError = null) }
            }
            is CompleteProfileFormEvent.CourseChanged -> {
                _formState.update { it.copy(course = event.course, courseError = null) }
            }
            is CompleteProfileFormEvent.RollNumberChanged -> {
                _formState.update { it.copy(rollNumber = event.rollNumber, rollNumberError = null) }
            }
            is CompleteProfileFormEvent.BacklogsChanged -> {
                _formState.update { it.copy(backlogs = event.backlogs, backlogsError = null) }
            }
            is CompleteProfileFormEvent.CurrentAddressChanged -> {
                _formState.update { it.copy(currentAddress = event.currentAddress, currentAddressError = null) }
            }
            is CompleteProfileFormEvent.PermanentAddressChanged -> {
                _formState.update { it.copy(permanentAddress = event.permanentAddress, permanentAddressError = null) }
            }
            is CompleteProfileFormEvent.CgpaChanged -> {
                _formState.update { it.copy(cgpa = event.cgpa, cgpaError = null) }
            }
            is CompleteProfileFormEvent.YearChanged -> {
                _formState.update { it.copy(year = event.year, yearError = null) }
            }
            is CompleteProfileFormEvent.TenthMarksPercentageChanged -> {
                _formState.update { it.copy(tenthMarksPercentage = event.tenthMarksPercentage, tenthMarksPercentageError = null) }
            }
            is CompleteProfileFormEvent.TwelfthMarksPercentageChanged -> {
                _formState.update { it.copy(twelfthMarksPercentage = event.twelfthMarksPercentage, twelfthMarksPercentageError = null) }
            }
            is CompleteProfileFormEvent.DeleteProfileImage -> deleteFileFromTemp(DocumentType.ProfilePic)
            is CompleteProfileFormEvent.DeleteResume -> deleteFileFromTemp(DocumentType.Resume)
            is CompleteProfileFormEvent.DeleteTenthMarksheet -> deleteFileFromTemp(DocumentType.TenthMarksheet)
            is CompleteProfileFormEvent.DeleteTwelfthMarksheet -> deleteFileFromTemp(DocumentType.TwelfthMarksheet)
            CompleteProfileFormEvent.Submit -> checkSubmissionReadiness()
        }
    }

    /**
     * Evaluates the form state and active processes to determine if the profile
     * can be safely submitted.
     *
     * This function performs a two-step validation:
     * 1. **Process Check**: Ensures no files are currently being uploaded to Supabase
     * to prevent partial data submission.
     * 2. **Content Validation**: Runs all form fields and file attachments through
     * [FormValidator] logic.
     *
     * If validation fails, it updates [_formState] with specific error messages for the UI.
     * If validation passes, it emits [ValidationEvent.Success] through the
     * [validationEventChannel] to notify the UI to proceed with the final network request.
     *
     * @see CompleteProfileFormEvent.Submit
     * @see FormValidator
     */
    private fun checkSubmissionReadiness() {
        val state = _formState.value
        val semesterError = FormValidator.validateSemester(state.semester)
        val courseError = FormValidator.validateCourse(state.course)
        val rollNumberError = FormValidator.validateRollNumber(state.rollNumber)
        val backlogsError = FormValidator.validateBacklogs(state.backlogs)
        val currentAddressError = FormValidator.validateCurrentAddress(state.currentAddress)
        val permanentAddressError = FormValidator.validatePermanentAddress(state.permanentAddress)
        val cgpaError = FormValidator.validateCgpa(state.cgpa)
        val yearError = FormValidator.validateYear(state.year)
        val tenthMarksPercentageError = FormValidator.validateTenthMarks(state.tenthMarksPercentage)
        val twelfthMarksPercentageError = FormValidator.validateTwelfthMarks(state.twelfthMarksPercentage)
        val resumeError = FormValidator.validateFile(_filesState.value[DocumentType.Resume]?.url ?: "", "Resume")
        val tenthMarksheetError = FormValidator.validateFile(_filesState.value[DocumentType.TenthMarksheet]?.url ?: "", "10th marksheet")
        val twelfthMarksheetError = FormValidator.validateFile(_filesState.value[DocumentType.TwelfthMarksheet]?.url ?: "", "12th marksheet")
        val profileImageError = FormValidator.validateFile(_filesState.value[DocumentType.ProfilePic]?.url ?: "", "Profile image")

        if (isAnyFileUploading.value) {
            _uiState.update { it.copy(error = "Please wait for files to finish uploading.") }
            return
        }

        if (semesterError != null || courseError != null || rollNumberError != null || backlogsError != null ||
            currentAddressError != null || permanentAddressError != null || cgpaError != null || yearError != null ||
            tenthMarksPercentageError != null || twelfthMarksPercentageError != null || 
            resumeError != null || tenthMarksheetError != null || twelfthMarksheetError != null ||
            profileImageError != null
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
                    twelfthMarksheetError = twelfthMarksheetError,
                    profileImageError = profileImageError
                )
            }
        } else {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }



    /**
     * Handles the asynchronous upload process for a specific document to Supabase Storage.
     *
     * This method is called directly by the UI layer. It performs the following steps:
     * 1. **Job Management**: Cancels any existing upload job for the same [documentType] to avoid conflicts.
     * 2. **State Reset**: Clears validation errors in [formState] and initializes the upload progress in [_filesState].
     * 3. **Path Generation**: Generates a temporary storage path for the document.
     * 4. **Streaming**: Streams the [fileByteArray] to the "profile" bucket and updates the progress state in real-time.
     * 5. **Finalization**: On success, it saves the generated URL; on failure, it resets the state and notifies the user via [uiState].
     *
     * @param documentType The specific document category (e.g., Resume, Profile Pic) being uploaded.
     * @param fileByteArray The raw binary data of the file to be uploaded.
     * @param fileName The name of the file used for naming the object in storage.
     */
    fun uploadFileToTemp(documentType: DocumentType, fileByteArray: ByteArray, fileName: String) {
        val folderName = documentType.folderName
        Timber.tag(tag).d("uploadFile: folderName=$folderName, fileName=$fileName")

        uploadJobs[documentType]?.cancel()
        uploadJobs[documentType] = viewModelScope.launch {
            _filesState.update { it + (documentType to it.getValue(documentType).copy(isUploading = true, progress = 0f)) }

            // Clear corresponding error
            _formState.update {
                when (documentType) {
                    DocumentType.Resume -> it.copy(resumeError = null)
                    DocumentType.TenthMarksheet -> it.copy(tenthMarksheetError = null)
                    DocumentType.TwelfthMarksheet -> it.copy(twelfthMarksheetError = null)
                    DocumentType.ProfilePic -> it.copy(profileImageError = null)
                }
            }

            _filesState.update { it + (documentType to it.getValue(documentType).copy(
                fileName = fileName,
                fileByteArray = fileByteArray,
                progress = 0f,
                url = ""
            )) }

            val userId = client.auth.currentUserOrNull()?.id ?: "unknown"
            try {
                val path = "temp/$userId/$folderName/$fileName"
                cloudFileUploader.uploadFile(bucketName, path, fileByteArray).collect { progress ->
                    _filesState.update { it + (documentType to it.getValue(documentType).copy(progress = progress)) }
                }
                _filesState.update { it + (documentType to it.getValue(documentType).copy(isUploading = false, url = path)) }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Timber.tag(tag).e(e, "uploadFile: upload failed for $fileName")
                _filesState.update { it + (documentType to it.getValue(documentType).copy(isUploading = false, fileName = "", fileByteArray = null)) }
                _uiState.update { it.copy(error = "${documentType::class.simpleName} upload failed: ${e.message}") }
            }
        }
    }




    /**
     * Removes a previously uploaded document from the temporary storage in Supabase.
     *
     * This method performs the following steps:
     * 1. **Path Construction**: Constructs the full storage path using the user's ID, document folder, and file name.
     * 2. **State Update**: Sets `isUploading` to true for the specified [documentType] to indicate an active process.
     * 3. **Deletion**: Calls [CloudFileUploader.deleteFile] to remove the object from the "profile" bucket.
     * 4. **Cleanup**: On success, resets the document's state (URL, name, data, progress) in [_filesState].
     * 5. **Error Handling**: On failure, reverts the uploading state and notifies the user through [_uiState].
     *
     * @param documentType The specific document category to be deleted.
     */
    fun deleteFileFromTemp(documentType: DocumentType) {
        val state = _filesState.value[documentType] ?: return
        val fileName = state.fileName
        val folderName = documentType.folderName
        Timber.tag(tag).d("deleteDocumentFromTemp: folderName=$folderName, fileName=$fileName")
        if (fileName.isBlank()) return

        viewModelScope.launch {
            try {
                _filesState.update { it + (documentType to it.getValue(documentType).copy(isUploading = true)) }
                val userId = client.auth.currentUserOrNull()?.id ?: "unknown"
                val path = "temp/$userId/$folderName/$fileName"
                cloudFileUploader.deleteFile(bucketName, path)
                _filesState.update { it + (documentType to it.getValue(documentType).copy(url = "", fileName = "", fileByteArray = null, progress = 0f, isUploading = false)) }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Timber.tag(tag).e(e, "deleteDocumentFromTemp: delete failed for $fileName")
                _filesState.update { it + (documentType to it.getValue(documentType).copy(isUploading = false)) }
                _uiState.update { it.copy(error = "Failed to delete ${documentType::class.simpleName}: ${e.message}") }
            }
        }
    }

    /**
     * Transitions a document from temporary storage to the permanent unapproved requests folder.
     *
     * This method is called during profile submission to move files from the `temp/` directory
     * to `new_profile_request/`. It performs the following:
     * 1. **Path Mapping**: Replaces the "temp/" prefix in the file path with "new_profile_request/".
     * 2. **File Migration**: Uses [CloudFileUploader.moveFile] to physically move the file in the storage bucket.
     * 3. **State Sync**: Updates the local [_filesState] with the new permanent path.
     *
     * @param documentType The type of document to promote.
     * @return The updated permanent storage path, or the original path if no promotion was needed.
     */
    private suspend fun promoteToUnapproved(documentType: DocumentType): String {
        val state = _filesState.value[documentType] ?: return ""
        val url = state.url
        val folderName = documentType.folderName
        val tempPrefix = "temp/"
        val permanentPrefix = "new_profile_request/"

        if (url.startsWith(tempPrefix)) {
            val permanentPath = url.replaceFirst(tempPrefix, permanentPrefix)
            Timber.tag(tag).d("promoteToUnapproved: promoting $folderName from $url to $permanentPath")

            cloudFileUploader.moveFile(bucketName, url, permanentPath)
            
            _filesState.update { it + (documentType to it.getValue(documentType).copy(url = permanentPath)) }
            return permanentPath
        }
        return url
    }

    /**
     * Orchestrates the final submission of a new profile request.
     *
     * This method performs several critical operations in sequence:
     * 1. **State Initialization**: Sets the UI to a loading state and clears previous errors.
     * 2. **Authentication Check**: Ensures a valid user session exists.
     * 3. **File Promotion**: Moves all uploaded documents from temporary storage to the permanent
     *    `new_profile_request/` directory using [promoteToUnapproved].
     * 4. **URL Resolution**: Obtains public URLs for all promoted files.
     * 5. **Request Construction**: Assembles a [NewProfileRequest] object using the current form state
     *    and the resolved file URLs.
     * 6. **Data Persistence**: Saves the new profile request to the repository.
     * 7. **UI Notification**: Updates the state to indicate success or captures and displays any errors.
     */
    fun createNewProfileRequest() {
        val state = _formState.value
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }

                val user = client.auth.currentUserOrNull() ?: throw Exception("User not authenticated")

                val resumeUrl = getPublicUrl(promoteToUnapproved(DocumentType.Resume))
                val tenthMarksheetUrl = getPublicUrl(promoteToUnapproved(DocumentType.TenthMarksheet))
                val twelfthMarksheetUrl = getPublicUrl(promoteToUnapproved(DocumentType.TwelfthMarksheet))
                val profileImageUrl = getPublicUrl(promoteToUnapproved(DocumentType.ProfilePic))

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
                    tenthMarksheetUrl = tenthMarksheetUrl,
                    twelfthMarksheetUrl = twelfthMarksheetUrl,
                    profileImageUrl = profileImageUrl,
                    resumeUrl = resumeUrl,
                )

                newProfileRequestRepository.createNewProfileRequest(newProfileRequest)
                _uiState.update { it.copy(isLoading = false, success = true, successResult = "PROFILE_UPDATE_SUCCESS") }
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
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                authRepository.logout()
                _uiState.update { it.copy(isLoading = false, success = true, successResult = "LOGOUT_SUCCESS") }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Logout failed"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = "") }
    }

    fun getPublicUrl(path: String): String {
        return if (path.isNotBlank()) cloudFileUploader.getPublicUrl(bucketName, path) else ""
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
