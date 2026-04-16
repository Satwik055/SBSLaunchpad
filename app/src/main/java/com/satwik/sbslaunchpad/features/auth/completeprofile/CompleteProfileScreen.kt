package com.satwik.sbslaunchpad.features.auth.completeprofile

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTextFeild
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import com.satwik.sbslaunchpad.core.designsystem.components.UploadInputButton
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.features.auth.AuthViewModel
import com.satwik.sbslaunchpad.features.auth.local_component.ScrollShadows
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompleteProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    val firstNameState = rememberTextFieldState()
    val lastNameState = rememberTextFieldState()
    val semesterState = rememberTextFieldState()
    val courseState = rememberTextFieldState()
    val rollNumberState = rememberTextFieldState()
    val backlogsState = rememberTextFieldState()
    val examRollNumberState = rememberTextFieldState()
    val permanentAddressState = rememberTextFieldState()

    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        LaunchpadTopAppBar(
            title = "Complete Profile",
            onBackClick = onBackClick
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
                Spacer(modifier = Modifier.height(32.dp))

                // Form Fields
                TextFeildCard {
                    LaunchpadTextFeild(
                        state = firstNameState,
                        semantic = ContentType.PersonFirstName,
                        placeholder = "First Name"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = lastNameState,
                        semantic = ContentType.PersonLastName,
                        placeholder = "Last Name"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
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
                        state = backlogsState,
                        semantic = ContentType.Username,
                        placeholder = "Backlogs"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = examRollNumberState,
                        semantic = ContentType.Username,
                        placeholder = "Exam Roll Number"
                    )
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    LaunchpadTextFeild(
                        state = permanentAddressState,
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
                        onClick = { /* TODO: Handle upload */ },
                    )

                    UploadInputButton(
                        text = "Attach 12th Marksheet",
                        onClick = { /* TODO: Handle upload */ },
                    )

                    UploadInputButton(
                        text = "Attach Resume",
                        onClick = { /* TODO: Handle upload */ },
                    )
                }
            }
        }

        // Bottom Button (Sticky)
        LaunchpadButton(
            text = "Submit",
            loading = uiState.isLoading,
            onClick = {
                viewModel.completeProfile(onSuccess = onSubmitSuccess)
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
