package com.satwik.sbslaunchpad.features.account.presentation

import org.koin.compose.viewmodel.koinViewModel
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.account.Account
import com.satwik.sbslaunchpad.features.account.presentation.components.DocumentItem
import com.satwik.sbslaunchpad.features.account.presentation.components.InfoItem
import com.satwik.sbslaunchpad.features.account.presentation.components.LogoutButton
import com.satwik.sbslaunchpad.features.auth.local_component.ScrollShadows

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val accountState by viewModel.accountState.collectAsStateWithLifecycle()

    AccountContent(
        modifier = modifier,
        accountState = accountState,
        onBackClick = onBackClick,
        onLogoutClick = {
            viewModel.logout()
            onLogoutClick()
        }
    )
}

@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    accountState: Result,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val account = accountState.successResult as? Account
    val scrollState = rememberScrollState()

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
                if (accountState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = TextPrimary
                    )
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
                                    .data(account?.profileImageUrl)
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

                        // Info Card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceDefault,
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                InfoItem(label = "Name", value = account?.name ?: "N/A")
                                InfoItem(label = "Email", value = account?.email ?: "N/A")
                                InfoItem(label = "University", value = account?.university ?: "N/A")
                                InfoItem(label = "Course", value = account?.course ?: "N/A")
                                InfoItem(label = "Semester", value = account?.semester ?: "N/A")
                                InfoItem(label = "Phone", value = account?.phone ?: "N/A")
                                InfoItem(label = "Backlogs", value = account?.backlogs ?: "N/A")
                                InfoItem(label = "Roll Number", value = account?.rollNumber ?: "N/A")
                                InfoItem(label = "Bio", value = account?.bio ?: "N/A")

                                Spacer(modifier = Modifier.height(16.dp))

                                DocumentItem(
                                    title = "Resume",
                                    showEdit = true,
                                    onViewClick = {},
                                    onEditClick = {}
                                )
                                DocumentItem(
                                    title = "10th Marksheet",
                                    onViewClick = {}
                                )
                                DocumentItem(
                                    title = "12th Marksheet",
                                    onViewClick = {},
                                    isLast = true
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

@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview() {
    AccountContent(
        accountState = Result(
            success = true,
            successResult = Account(
                id = "1",
                name = "Test User",
                email = "test@example.com",
                university = "Test University"
            )
        )
    )
}
