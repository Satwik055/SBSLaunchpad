package com.satwik.sbslaunchpad.features.auth.completeprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTextFeild
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import com.satwik.sbslaunchpad.core.designsystem.components.UploadInputButton
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.util.getFileName
import com.satwik.sbslaunchpad.features.auth.local_component.ScrollShadows
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompleteProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: CompleteProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle(null)
    
    val resumeUploadState by viewModel.resumeUploadState.collectAsStateWithLifecycle()
    val tenthUploadState by viewModel.tenthMarksheetUploadState.collectAsStateWithLifecycle()
    val twelfthUploadState by viewModel.twelfthMarksheetUploadState.collectAsStateWithLifecycle()
    val isAnyFileUploading by viewModel.isAnyFileUploading.collectAsStateWithLifecycle()

    val semesterState = rememberTextFieldState()
    val courseState = rememberTextFieldState()
    val rollNumberState = rememberTextFieldState()
    val examRollNumberState = rememberTextFieldState()
    val backlogsState = rememberTextFieldState()
    val addressState = rememberTextFieldState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
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
                    LaunchpadTextFeild(
                        state = semesterState,
                        semantic = ContentType.Username,
                        placeholder = "Semester"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = courseState,
                        semantic = ContentType.Username,
                        placeholder = "Course"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = rollNumberState,
                        semantic = ContentType.Username,
                        placeholder = "Roll Number"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = examRollNumberState,
                        semantic = ContentType.Username,
                        placeholder = "Exam Roll Number"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = backlogsState,
                        semantic = ContentType.Username,
                        placeholder = "Backlogs"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = addressState,
                        semantic = ContentType.PostalAddress,
                        placeholder = "Permanent Address"
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Upload Section
                Text(
                    text = "Upload Documents",
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = TextPrimary
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    UploadInputButton(
                        text = "Attach 10th Marksheet",
                        selectedFileName = tenthUploadState.fileName,
                        loading = tenthUploadState.isUploading,
                        uploadProgress = tenthUploadState.progress,
                        onClick = { tenthLauncher.launch("application/pdf") },
                    )

                    UploadInputButton(
                        text = "Attach 12th Marksheet",
                        selectedFileName = twelfthUploadState.fileName,
                        loading = twelfthUploadState.isUploading,
                        uploadProgress = twelfthUploadState.progress,
                        onClick = { twelfthLauncher.launch("application/pdf") },
                    )

                    UploadInputButton(
                        text = "Attach Resume",
                        selectedFileName = resumeUploadState.fileName,
                        loading = resumeUploadState.isUploading,
                        uploadProgress = resumeUploadState.progress,
                        onClick = { resumeLauncher.launch("application/pdf") },
                    )
                }
            }
        }

        // Bottom Button (Sticky)
        LaunchpadButton(
            text = "Submit",
            loading = uiState.isLoading,
            enabled = !isAnyFileUploading,
            onClick = {
                viewModel.updateProfile(
                    semester = semesterState.text.toString(),
                    course = courseState.text.toString(),
                    rollNumber = rollNumberState.text.toString(),
                    examRollNumber = examRollNumberState.text.toString(),
                    backlogs = backlogsState.text.toString(),
                    address = addressState.text.toString()
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalHorizontalAppPadding.current)
                .padding(bottom = 10.dp, top = 16.dp)
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun CompleteProfileScreenPreview() {
    CompleteProfileScreen(modifier = Modifier.statusBarsPadding())
}
