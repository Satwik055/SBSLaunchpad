package com.satwik.sbslaunchpad.features.account

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.CalloutCard
import com.satwik.sbslaunchpad.core.designsystem.components.CalloutType
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadSnackbarHost
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.LogoutConfirmationDialog
import com.satwik.sbslaunchpad.core.designsystem.components.TextFeildCard
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.profile.model.Profile
import com.satwik.sbslaunchpad.data.profile.model.RequestStatus
import com.satwik.sbslaunchpad.data.profile_update_request.model.ProfileUpdateRequest
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.ui.Alignment
import com.satwik.sbslaunchpad.features.editprofile.EditProfileTextFeild
import com.satwik.sbslaunchpad.features.account.components.DocumentItem
import com.satwik.sbslaunchpad.features.account.components.LogoutButton
import com.satwik.sbslaunchpad.core.designsystem.components.ScrollShadows
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.data.profile.model.ProfileStatus
import kotlinx.serialization.json.buildJsonObject

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    val accountState by viewModel.accountState.collectAsStateWithLifecycle()
    val latestUpdateRequest by viewModel.latestUpdateRequest.collectAsStateWithLifecycle()
    val markRequestAsReadState by viewModel.markRequestAsReadState.collectAsStateWithLifecycle()

    AccountScreenContent(
        accountState = accountState,
        latestUpdateRequest = latestUpdateRequest,
        markRequestAsReadState = markRequestAsReadState,
        onResetMarkAsReadState = { viewModel.resetMarkAsReadState() },
        onBackClick = onBackClick,
        onLogoutClick = onLogoutClick,
        onEditClick = {
            viewModel.markLatestRequestAsRead()
            if(markRequestAsReadState.success || latestUpdateRequest.successResult == null ){
                onEditClick()
            }
        },
        onLogoutConfirmed = {
            viewModel.logout()
            onLogoutClick()
        },
        onDismissRequest = { viewModel.markLatestRequestAsRead() },
        modifier = modifier
    )
}

@Composable
fun AccountScreenContent(
    accountState: Result,
    latestUpdateRequest: Result,
    markRequestAsReadState: Result,
    onResetMarkAsReadState: () -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onEditClick: () -> Unit,
    onLogoutConfirmed: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(markRequestAsReadState.error) {
        if (markRequestAsReadState.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(markRequestAsReadState.error)
            onResetMarkAsReadState()
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogoutConfirmed()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Scaffold(
        containerColor = BackgroundDefault,
        snackbarHost = { LaunchpadSnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        AccountContent(
            modifier = modifier.padding(padding),
            accountState = accountState,
            latestUpdateRequest = latestUpdateRequest,
            markRequestAsReadState = markRequestAsReadState,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onLogoutClick = { showLogoutDialog = true },
            onEditClick = onEditClick,
            onDismissRequest = onDismissRequest
        )
    }
}


@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    accountState: Result,
    latestUpdateRequest: Result,
    markRequestAsReadState: Result,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val profile = accountState.successResult as? Profile
    val request = latestUpdateRequest.successResult as? ProfileUpdateRequest
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val isUpdatePending = request?.status == RequestStatus.IN_REVIEW
    val isMarkingAsRead = markRequestAsReadState.isLoading

    fun viewDocument(url: String) {
        if (url.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "URL not available", Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        // Top Bar
        LaunchpadTopAppBar(
            title = "Account",
            onBackClick = onBackClick
        )

        ScrollShadows(scrollState = scrollState) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (accountState.isLoading || latestUpdateRequest.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrandPrimary)
                } else if (accountState.error.isNotEmpty()) {
                    Text(
                        text = accountState.error,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = LocalHorizontalAppPadding.current),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        request?.let { request ->
                            if (!request.isRead) {
                                val calloutType = when (request.status) {
                                    RequestStatus.IN_REVIEW -> CalloutType.Warning
                                    RequestStatus.ACCEPTED -> CalloutType.Success
                                    RequestStatus.REJECTED -> CalloutType.Error
                                }

                                val title = when (request.status) {
                                    RequestStatus.IN_REVIEW -> "Edit request in review"
                                    RequestStatus.ACCEPTED -> "Edit request approved"
                                    RequestStatus.REJECTED -> "Edit request rejected"
                                }

                                val description = when (request.status) {
                                    RequestStatus.IN_REVIEW -> "Your profile edit request is currently under review by the admin."
                                    RequestStatus.ACCEPTED -> "Your recent profile edit request has been approved."
                                    RequestStatus.REJECTED -> "Your recent profile edit request was rejected."
                                }

                                CalloutCard(
                                    title = title,
                                    description = description,
                                    type = calloutType,
                                    reason = if (request.status == RequestStatus.REJECTED) request.note else null,
                                    onDismiss = if (calloutType == CalloutType.Success || calloutType == CalloutType.Error) {
                                        { onDismissRequest() }
                                    } else null,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )
                            }
                        }

                        // Profile Picture with Coil
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFF5A575E)),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(profile?.profileImageUrl)
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

                        // Personal Info Section
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

                            Text(
                                text = "Personal Info",
                                style = TextStyle(
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = TextPrimary
                                )
                            )
                            LaunchpadButton(
                                text = "Edit",
                                onClick = {
                                    if (isUpdatePending) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Cannot edit profile while an edit request is in review")
                                        }
                                    } else {
                                        onEditClick()
                                    }
                                },
                                enabled = !isMarkingAsRead,
                                containerColor = if (isUpdatePending) Color.White.copy(alpha = 0.6f) else Color.White,
                                contentColor = if (isUpdatePending) Color.Black.copy(alpha = 0.5f) else Color.Black,
                                leadingIcon = R.drawable.ic_pencil,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(44.dp)
                                    .customShadow(
                                        elevation = if (isUpdatePending || isMarkingAsRead) 0.dp else 4.dp,
                                        shape = CircleShape,
                                        alpha = 0.3f
                                    )
                            )

                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        // Info Card
                        TextFeildCard {
                            EditProfileTextFeild(
                                label = "Name",
                                state = rememberTextFieldState(profile?.fullName ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Email",
                                state = rememberTextFieldState(profile?.email ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Phone",
                                state = rememberTextFieldState(profile?.phone ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Course",
                                state = rememberTextFieldState(profile?.course ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Semester",
                                state = rememberTextFieldState(profile?.semester?.toString() ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Year",
                                state = rememberTextFieldState(profile?.year?.toString() ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Roll Number",
                                state = rememberTextFieldState(profile?.rollNumber ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "CGPA",
                                state = rememberTextFieldState(profile?.cgpa?.toString() ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "10th Percentage",
                                state = rememberTextFieldState(profile?.tenthMarksPercentage?.toString() ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "12th Percentage",
                                state = rememberTextFieldState(profile?.twelfthMarksPercentage?.toString() ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Backlogs",
                                state = rememberTextFieldState(profile?.backlogs?.toString() ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Current Address",
                                state = rememberTextFieldState(profile?.currentAddress ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )
                            EditProfileTextFeild(
                                label = "Permanent Address",
                                state = rememberTextFieldState(profile?.permanentAddress ?: "N/A"),
                                placeholder = "",
                                onValueChange = {},
                                enabled = false
                            )

                            Column(modifier = Modifier.padding(16.dp)) {
                                DocumentItem(
                                    title = "Resume",
                                    onViewClick = { viewDocument(profile?.resumeUrl ?: "") },
                                    showDivider = false
                                )
                                DocumentItem(
                                    title = "10th Marksheet",
                                    onViewClick = { viewDocument(profile?.tenthMarksheetUrl ?: "") },
                                    showDivider = false
                                )
                                DocumentItem(
                                    title = "12th Marksheet",
                                    onViewClick = { viewDocument(profile?.twelfthMarksheetUrl ?: "") },
                                    showDivider = false
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Logout Button
                        LogoutButton(onClick = onLogoutClick)

                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
fun AccountScreenPreview() {
    val sampleProfile = Profile(
        id = "1",
        fullName = "John Doe",
        email = "john.doe@example.com",
        profileImageUrl = "",
        twelfthMarksheetUrl = "",
        tenthMarksheetUrl = "",
        resumeUrl = "",
        course = "Computer Science",
        semester = 6,
        phone = "1234567890",
        backlogs = 0,
        cgpa = 8.5,
        tenthMarksPercentage = 90.0,
        twelfthMarksPercentage = 85.0,
        year = 3,
        rollNumber = "CS123",
        currentAddress = "123 Main St",
        permanentAddress = "456 Oak St",
        status = ProfileStatus.ACCEPTED,
        fcmToken = "",
        postSelectedIn = "",
        isBlacklisted = false,
    )

    SBSLaunchpadTheme {
        Surface(color = BackgroundDefault) {
            AccountContent(
                accountState = Result(success = true, successResult = sampleProfile),
                latestUpdateRequest = Result(
                    success = true,
                    successResult = ProfileUpdateRequest(
                        status = RequestStatus.IN_REVIEW,
                        requestedChanges = buildJsonObject { }
                    )
                ),
                markRequestAsReadState = Result(),
                snackbarHostState = remember { SnackbarHostState() },
                onBackClick = {},
                onLogoutClick = {},
                onEditClick = {},
                onDismissRequest = {}
            )
        }
    }
}
