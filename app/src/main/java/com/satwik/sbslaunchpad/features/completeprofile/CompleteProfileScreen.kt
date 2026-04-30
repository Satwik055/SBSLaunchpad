package com.satwik.sbslaunchpad.features.completeprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadSnackbarHost
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTextFeild
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.FieldError
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import com.satwik.sbslaunchpad.core.designsystem.components.UploadInputButton
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.util.getFileName
import com.satwik.sbslaunchpad.core.designsystem.components.ScrollShadows
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompleteProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: CompleteProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle(null)
    
    val resumeUploadState by viewModel.resumeUploadState.collectAsStateWithLifecycle()
    val tenthUploadState by viewModel.tenthMarksheetUploadState.collectAsStateWithLifecycle()
    val twelfthUploadState by viewModel.twelfthMarksheetUploadState.collectAsStateWithLifecycle()
    val isAnyFileUploading by viewModel.isAnyFileUploading.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(uiState.error)
            viewModel.clearError()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.validationEvents.collectLatest { event ->
            when (event) {
                is CompleteProfileViewModel.ValidationEvent.Success -> {
                    viewModel.updateProfile()
                }
            }
        }
    }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val semesterState = rememberTextFieldState()
    val courseState = rememberTextFieldState()
    val rollNumberState = rememberTextFieldState()
    val backlogsState = rememberTextFieldState()
    val cgpaState = rememberTextFieldState()
    val yearState = rememberTextFieldState()
    val tenthMarksPercentageState = rememberTextFieldState()
    val twelfthMarksPercentageState = rememberTextFieldState()
    val currentAddressState = rememberTextFieldState()
    val permanentAddressState = rememberTextFieldState()

    // Sync TextFieldState with ViewModel's formState
    LaunchedEffect(formState.semester) {
        if (semesterState.text.toString() != formState.semester) {
            semesterState.edit { replace(0, length, formState.semester) }
        }
    }
    LaunchedEffect(formState.course) {
        if (courseState.text.toString() != formState.course) {
            courseState.edit { replace(0, length, formState.course) }
        }
    }
    LaunchedEffect(formState.rollNumber) {
        if (rollNumberState.text.toString() != formState.rollNumber) {
            rollNumberState.edit { replace(0, length, formState.rollNumber) }
        }
    }
    LaunchedEffect(formState.backlogs) {
        if (backlogsState.text.toString() != formState.backlogs) {
            backlogsState.edit { replace(0, length, formState.backlogs) }
        }
    }
    LaunchedEffect(formState.cgpa) {
        if (cgpaState.text.toString() != formState.cgpa) {
            cgpaState.edit { replace(0, length, formState.cgpa) }
        }
    }
    LaunchedEffect(formState.year) {
        if (yearState.text.toString() != formState.year) {
            yearState.edit { replace(0, length, formState.year) }
        }
    }
    LaunchedEffect(formState.tenthMarksPercentage) {
        if (tenthMarksPercentageState.text.toString() != formState.tenthMarksPercentage) {
            tenthMarksPercentageState.edit { replace(0, length, formState.tenthMarksPercentage) }
        }
    }
    LaunchedEffect(formState.twelfthMarksPercentage) {
        if (twelfthMarksPercentageState.text.toString() != formState.twelfthMarksPercentage) {
            twelfthMarksPercentageState.edit { replace(0, length, formState.twelfthMarksPercentage) }
        }
    }
    LaunchedEffect(formState.currentAddress) {
        if (currentAddressState.text.toString() != formState.currentAddress) {
            currentAddressState.edit { replace(0, length, formState.currentAddress) }
        }
    }
    LaunchedEffect(formState.permanentAddress) {
        if (permanentAddressState.text.toString() != formState.permanentAddress) {
            permanentAddressState.edit { replace(0, length, formState.permanentAddress) }
        }
    }

    // Helper for file selection
    val handleFileSelection: (Uri?, String) -> Unit = { uri, folderName ->
        uri?.let { selectedUri ->
            val fileName = getFileName(context, selectedUri)
            val inputStream = context.contentResolver.openInputStream(selectedUri)
            val fileBytes = inputStream?.use { it.readBytes() }
            if (fileBytes != null && fileName != null) {
                viewModel.onFileSelected(folderName, fileBytes, fileName)
            }
        }
    }

    val resumeLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { 
        handleFileSelection(it, "resume") 
    }
    val tenthLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { 
        handleFileSelection(it, "marksheet/tenth") 
    }
    val twelfthLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { 
        handleFileSelection(it, "marksheet/twelfth")
    }

    var lastFullName by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(profile?.fullName) {
        profile?.fullName?.let { 
            lastFullName = it 
        }
    }
    val displayName = lastFullName ?: "User"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDefault,
        snackbarHost = { LaunchpadSnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LaunchpadTopAppBar(
                title = "",
                onBackClick = { viewModel.logout() }
            )

            ScrollShadows(
                scrollState = scrollState,
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = LocalHorizontalAppPadding.current)
                ) {

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append("Hey ")
                            }
                            withStyle(style = SpanStyle(color = BrandPrimary, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic)) {
                                append("$displayName !")
                            }
                        },
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 32.sp,
                            color = TextPrimary
                        )
                    )

                    Text(
                        text = "Please complete your profile",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Form Fields
                    TextFeildCard {
                        Column {
                            LaunchpadTextFeild(
                                state = semesterState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.SemesterChanged(it)) },
                                placeholder = "Semester",
                                isError = formState.semesterError != null,
                                keyboardType = KeyboardType.Number
                            )
                            if (formState.semesterError != null) {
                                FieldError(text = formState.semesterError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = courseState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.CourseChanged(it)) },
                                placeholder = "Course",
                                isError = formState.courseError != null
                            )
                            if (formState.courseError != null) {
                                FieldError(text = formState.courseError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = rollNumberState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.RollNumberChanged(it)) },
                                placeholder = "Roll Number",
                                isError = formState.rollNumberError != null
                            )
                            if (formState.rollNumberError != null) {
                                FieldError(text = formState.rollNumberError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = yearState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.YearChanged(it)) },
                                placeholder = "Year",
                                isError = formState.yearError != null,
                                keyboardType = KeyboardType.Number
                            )
                            if (formState.yearError != null) {
                                FieldError(text = formState.yearError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = backlogsState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.BacklogsChanged(it)) },
                                placeholder = "Backlogs",
                                isError = formState.backlogsError != null,
                                keyboardType = KeyboardType.Number
                            )
                            if (formState.backlogsError != null) {
                                FieldError(text = formState.backlogsError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = cgpaState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.CgpaChanged(it)) },
                                placeholder = "Current CGPA",
                                isError = formState.cgpaError != null,
                                keyboardType = KeyboardType.Number
                            )
                            if (formState.cgpaError != null) {
                                FieldError(text = formState.cgpaError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = tenthMarksPercentageState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.TenthMarksPercentageChanged(it)) },
                                placeholder = "10th Mark Percentage",
                                isError = formState.tenthMarksPercentageError != null,
                                keyboardType = KeyboardType.Decimal
                            )
                            if (formState.tenthMarksPercentageError != null) {
                                FieldError(text = formState.tenthMarksPercentageError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = twelfthMarksPercentageState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.TwelfthMarksPercentageChanged(it)) },
                                placeholder = "12th Mark Percentage",
                                isError = formState.twelfthMarksPercentageError != null,
                                keyboardType = KeyboardType.Decimal
                            )
                            if (formState.twelfthMarksPercentageError != null) {
                                FieldError(text = formState.twelfthMarksPercentageError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = currentAddressState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.CurrentAddressChanged(it)) },
                                placeholder = "Current Address",
                                isError = formState.currentAddressError != null
                            )
                            if (formState.currentAddressError != null) {
                                FieldError(text = formState.currentAddressError!!)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                        Column {
                            LaunchpadTextFeild(
                                state = permanentAddressState,
                                onValueChange = { viewModel.onEvent(CompleteProfileFormEvent.PermanentAddressChanged(it)) },
                                placeholder = "Permanent Address",
                                isError = formState.permanentAddressError != null
                            )
                            if (formState.permanentAddressError != null) {
                                FieldError(text = formState.permanentAddressError!!)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Upload Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Upload Documents",
                            style = TextStyle(
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                        )
                        
                        if (formState.tenthMarksheetError != null || formState.twelfthMarksheetError != null || formState.resumeError != null) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        UploadInputButton(
                            text = "Attach 10th Marksheet",
                            selectedFileName = tenthUploadState.fileName,
                            loading = tenthUploadState.isUploading,
                            uploadProgress = tenthUploadState.progress,
                            onClick = { tenthLauncher.launch("application/pdf") },
                        )
                        if (formState.tenthMarksheetError != null) {
                            FieldError(text = formState.tenthMarksheetError!!, startPadding = 0.dp)
                        }

                        UploadInputButton(
                            text = "Attach 12th Marksheet",
                            selectedFileName = twelfthUploadState.fileName,
                            loading = twelfthUploadState.isUploading,
                            uploadProgress = twelfthUploadState.progress,
                            onClick = { twelfthLauncher.launch("application/pdf") },
                        )
                        if (formState.twelfthMarksheetError != null) {
                            FieldError(text = formState.twelfthMarksheetError!!, startPadding = 0.dp)
                        }

                        UploadInputButton(
                            text = "Attach Resume",
                            selectedFileName = resumeUploadState.fileName,
                            loading = resumeUploadState.isUploading,
                            uploadProgress = resumeUploadState.progress,
                            onClick = { resumeLauncher.launch("application/pdf") },
                        )
                        if (formState.resumeError != null) {
                            FieldError(text = formState.resumeError!!, startPadding = 0.dp)
                        }
                    }
                }
            }

            // Bottom Button (Sticky)
            LaunchpadButton(
                text = "Submit",
                loading = uiState.isLoading,
                enabled = !isAnyFileUploading,
                onClick = {
                    viewModel.onEvent(CompleteProfileFormEvent.Submit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalHorizontalAppPadding.current)
                    .padding(bottom = 10.dp, top = 16.dp)
            )
        }
    }
}


@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun CompleteProfileScreenPreview() {
    CompleteProfileScreen(modifier = Modifier.statusBarsPadding())
}
