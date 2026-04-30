package com.satwik.sbslaunchpad.features.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.ConfirmationDialog
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadSnackbarHost
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandOnPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.designsystem.components.ScrollShadows
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import kotlinx.coroutines.flow.collectLatest
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.util.Result
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val isChanged by viewModel.isChanged.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.validationEvents.collectLatest { event ->
            when (event) {
                EditProfileViewModel.ValidationEvent.Success -> {
                    onSuccess()
                }
            }
        }
    }

    EditProfileContent(
        uiState = uiState,
        formState = formState,
        isChanged = isChanged,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onClearError = viewModel::clearError,
        modifier = modifier
    )
}

@Composable
fun EditProfileContent(
    uiState: Result,
    formState: EditProfileFormState,
    isChanged: Boolean,
    onEvent: (EditProfileFormEvent) -> Unit,
    onBackClick: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val nameState = rememberTextFieldState()
    val emailState = rememberTextFieldState()
    val courseState = rememberTextFieldState()
    val semesterState = rememberTextFieldState()
    val phoneState = rememberTextFieldState()
    val backlogsState = rememberTextFieldState()
    val rollNumberState = rememberTextFieldState()
    val cgpaState = rememberTextFieldState()
    val tenthMarksState = rememberTextFieldState()
    val twelfthMarksState = rememberTextFieldState()
    val yearState = rememberTextFieldState()
    val permanentAddressState = rememberTextFieldState()
    val currentAddressState = rememberTextFieldState()

    // Sync TextFieldState with ViewModel's formState
    LaunchedEffect(formState.fullName) {
        if (nameState.text.toString() != formState.fullName) {
            nameState.edit { replace(0, length, formState.fullName) }
        }
    }
    LaunchedEffect(formState.email) {
        if (emailState.text.toString() != formState.email) {
            emailState.edit { replace(0, length, formState.email) }
        }
    }
    LaunchedEffect(formState.course) {
        if (courseState.text.toString() != formState.course) {
            courseState.edit { replace(0, length, formState.course) }
        }
    }
    LaunchedEffect(formState.semester) {
        if (semesterState.text.toString() != formState.semester) {
            semesterState.edit { replace(0, length, formState.semester) }
        }
    }
    LaunchedEffect(formState.phone) {
        if (phoneState.text.toString() != formState.phone) {
            phoneState.edit { replace(0, length, formState.phone) }
        }
    }
    LaunchedEffect(formState.backlogs) {
        if (backlogsState.text.toString() != formState.backlogs) {
            backlogsState.edit { replace(0, length, formState.backlogs) }
        }
    }
    LaunchedEffect(formState.rollNumber) {
        if (rollNumberState.text.toString() != formState.rollNumber) {
            rollNumberState.edit { replace(0, length, formState.rollNumber) }
        }
    }
    LaunchedEffect(formState.cgpa) {
        if (cgpaState.text.toString() != formState.cgpa) {
            cgpaState.edit { replace(0, length, formState.cgpa) }
        }
    }
    LaunchedEffect(formState.tenthMarksPercentage) {
        if (tenthMarksState.text.toString() != formState.tenthMarksPercentage) {
            tenthMarksState.edit { replace(0, length, formState.tenthMarksPercentage) }
        }
    }
    LaunchedEffect(formState.twelfthMarksPercentage) {
        if (twelfthMarksState.text.toString() != formState.twelfthMarksPercentage) {
            twelfthMarksState.edit { replace(0, length, formState.twelfthMarksPercentage) }
        }
    }
    LaunchedEffect(formState.year) {
        if (yearState.text.toString() != formState.year) {
            yearState.edit { replace(0, length, formState.year) }
        }
    }
    LaunchedEffect(formState.permanentAddress) {
        if (permanentAddressState.text.toString() != formState.permanentAddress) {
            permanentAddressState.edit { replace(0, length, formState.permanentAddress) }
        }
    }
    LaunchedEffect(formState.currentAddress) {
        if (currentAddressState.text.toString() != formState.currentAddress) {
            currentAddressState.edit { replace(0, length, formState.currentAddress) }
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(uiState.error)
            onClearError()
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDefault,
        snackbarHost = { LaunchpadSnackbarHost(hostState = snackbarHostState) },
        topBar = {
            LaunchpadTopAppBar(
                title = "Edit Profile",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ScrollShadows(
                scrollState = scrollState,
                modifier = Modifier.weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (uiState.isLoading && formState.fullName.isEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = BrandPrimary
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                                .padding(horizontal = LocalHorizontalAppPadding.current),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF5A575E)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(formState.profileImageUrl)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.profile_big),
                            error = painterResource(id = R.drawable.profile_big),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Personal Info",
                        modifier = Modifier.align(Alignment.Start),
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextFeildCard {
                        Column {
                            EditProfileTextFeild(
                                label = "Name",
                                state = nameState,
                                placeholder = "Enter name",
                                onValueChange = { onEvent(EditProfileFormEvent.NameChanged(it)) },
                                isError = formState.fullNameError != null,
                                errorMessage = formState.fullNameError,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Email",
                                state = emailState,
                                placeholder = "Enter email",
                                onValueChange = { onEvent(EditProfileFormEvent.EmailChanged(it)) },
                                isError = formState.emailError != null,
                                errorMessage = formState.emailError,
                                keyboardType = KeyboardType.Email,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Phone",
                                state = phoneState,
                                placeholder = "Enter phone",
                                onValueChange = { onEvent(EditProfileFormEvent.PhoneChanged(it)) },
                                isError = formState.phoneError != null,
                                errorMessage = formState.phoneError,
                                keyboardType = KeyboardType.Phone,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Course",
                                state = courseState,
                                placeholder = "Enter course",
                                onValueChange = { onEvent(EditProfileFormEvent.CourseChanged(it)) },
                                isError = formState.courseError != null,
                                errorMessage = formState.courseError,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Semester",
                                state = semesterState,
                                placeholder = "Enter semester",
                                onValueChange = { onEvent(EditProfileFormEvent.SemesterChanged(it)) },
                                isError = formState.semesterError != null,
                                errorMessage = formState.semesterError,
                                keyboardType = KeyboardType.Number,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Year",
                                state = yearState,
                                placeholder = "Enter year",
                                onValueChange = { onEvent(EditProfileFormEvent.YearChanged(it)) },
                                isError = formState.yearError != null,
                                errorMessage = formState.yearError,
                                keyboardType = KeyboardType.Number,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Roll Number",
                                state = rollNumberState,
                                placeholder = "Enter roll number",
                                onValueChange = { onEvent(EditProfileFormEvent.RollNumberChanged(it)) },
                                isError = formState.rollNumberError != null,
                                errorMessage = formState.rollNumberError,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "CGPA",
                                state = cgpaState,
                                placeholder = "Enter CGPA",
                                onValueChange = { onEvent(EditProfileFormEvent.CgpaChanged(it)) },
                                isError = formState.cgpaError != null,
                                errorMessage = formState.cgpaError,
                                keyboardType = KeyboardType.Number,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "10th Percentage",
                                state = tenthMarksState,
                                placeholder = "Enter 10th %",
                                onValueChange = { onEvent(EditProfileFormEvent.TenthMarksPercentageChanged(it)) },
                                isError = formState.tenthMarksPercentageError != null,
                                errorMessage = formState.tenthMarksPercentageError,
                                keyboardType = KeyboardType.Number,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "12th Percentage",
                                state = twelfthMarksState,
                                placeholder = "Enter 12th %",
                                onValueChange = { onEvent(EditProfileFormEvent.TwelfthMarksPercentageChanged(it)) },
                                isError = formState.twelfthMarksPercentageError != null,
                                errorMessage = formState.twelfthMarksPercentageError,
                                keyboardType = KeyboardType.Number,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Backlogs",
                                state = backlogsState,
                                placeholder = "Enter backlogs",
                                onValueChange = { onEvent(EditProfileFormEvent.BacklogsChanged(it)) },
                                isError = formState.backlogsError != null,
                                errorMessage = formState.backlogsError,
                                keyboardType = KeyboardType.Number,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Current Address",
                                state = currentAddressState,
                                placeholder = "Enter current address",
                                onValueChange = { onEvent(EditProfileFormEvent.CurrentAddressChanged(it)) },
                                isError = formState.currentAddressError != null,
                                errorMessage = formState.currentAddressError,
                                showDivider = true
                            )
                            EditProfileTextFeild(
                                label = "Permanent Address",
                                state = permanentAddressState,
                                placeholder = "Enter permanent address",
                                onValueChange = { onEvent(EditProfileFormEvent.PermanentAddressChanged(it)) },
                                isError = formState.permanentAddressError != null,
                                errorMessage = formState.permanentAddressError,
                                showDivider = false
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }

            if (showConfirmationDialog) {
                ConfirmationDialog(
                    title = "Request for edit",
                    iconTint = BrandPrimary,
                    icon = painterResource(R.drawable.ic_info_filled),
                    message = "An admin needs to approve your profile edit request to apply the changes in your profile.",
                    confirmButtonText = "Request",
                    confirmButtonTextColor = BrandOnPrimary,
                    confirmButtonColor = BrandPrimary,
                    onConfirm = {
                        onEvent(EditProfileFormEvent.Submit)
                        showConfirmationDialog = false
                    },
                    onDismiss = { showConfirmationDialog = false }
                )
            }

            LaunchpadButton(
                text = "Confirm",
                loading = uiState.isLoading,
                enabled = isChanged,
                onClick = { showConfirmationDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalHorizontalAppPadding.current)
                    .padding(bottom = 16.dp, top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    SBSLaunchpadTheme {
        EditProfileContent(
            uiState = Result(),
            formState = EditProfileFormState(
                fullName = "Satwik",
                email = "satwik@example.com",
                course = "Computer Science",
                semester = "6",
                phone = "1234567890",
                backlogs = "0",
                rollNumber = "12345"
            ),
            isChanged = true,
            onEvent = {},
            onBackClick = {},
            onClearError = {}
        )
    }
}
