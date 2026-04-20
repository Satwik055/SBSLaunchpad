package com.satwik.sbslaunchpad.features.account.presentation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.LogoutConfirmationDialog
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.data.profile.Profile
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
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout()
                onLogoutClick()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    AccountContent(
        modifier = modifier,
        accountState = accountState,
        onBackClick = onBackClick,
        onLogoutClick = { showLogoutDialog = true },
    )
}

@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    accountState: Result,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val profile = accountState.successResult as? Profile
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    fun viewDocument(url: String) {
        if (url.isEmpty()) return
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse(url), "application/pdf")
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback: Open in browser if no dedicated PDF viewer handles the URI
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(browserIntent)
            } catch (ex: Exception) {
                Toast.makeText(context, "No app found to open PDF", Toast.LENGTH_SHORT).show()
            }
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .customShadow(
                                    elevation = 3.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    alpha = ElevationStrength,
                                )
                            ,
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceDefault,
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                InfoItem(label = "Name", value = profile?.fullName + " " )
                                InfoItem(label = "Email", value = profile?.email ?: "N/A")
                                InfoItem(label = "Course", value = profile?.course ?: "N/A")
                                InfoItem(label = "Semester", value = profile?.semester ?: "N/A")
                                InfoItem(label = "Phone", value = profile?.phone ?: "N/A")
                                InfoItem(label = "Backlogs", value = profile?.backlogs ?: "N/A")
                                InfoItem(label = "Roll Number", value = profile?.rollNumber ?: "N/A")

                                Spacer(modifier = Modifier.height(16.dp))

                                DocumentItem(
                                    title = "Resume",
                                    onViewClick = { viewDocument(profile?.resumeUrl ?: "") }
                                )
                                DocumentItem(
                                    title = "10th Marksheet",
                                    onViewClick = { viewDocument(profile?.tenthMarksheetUrl ?: "") }
                                )
                                DocumentItem(
                                    title = "12th Marksheet",
                                    onViewClick = { viewDocument(profile?.twelfthMarksheetUrl ?: "") },
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


