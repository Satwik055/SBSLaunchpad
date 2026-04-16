package com.satwik.sbslaunchpad.features.account.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.features.account.presentation.components.DocumentItem
import com.satwik.sbslaunchpad.features.account.presentation.components.InfoItem
import com.satwik.sbslaunchpad.features.account.presentation.components.LogoutButton
import com.satwik.sbslaunchpad.features.auth.local_component.ScrollShadows

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    profileImageUrl: String? = null,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
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
                            .data(profileImageUrl)
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
                        InfoItem(label = "Name", value = "Milke Michegan")
                        InfoItem(label = "Email", value = "mike123@gmail.com")
                        InfoItem(label = "Course", value = "Bcom(Hons)")
                        InfoItem(label = "Semester", value = "6th")
                        InfoItem(label = "Phone", value = "+91 2434453342")
                        InfoItem(label = "Backlogs", value = "3")
                        InfoItem(label = "Roll Number", value = "2023/0399")

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

@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview() {
    AccountScreen()
}
