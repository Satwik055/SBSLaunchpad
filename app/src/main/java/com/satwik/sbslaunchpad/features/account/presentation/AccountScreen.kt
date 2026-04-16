package com.satwik.sbslaunchpad.features.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.theme.*

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    profileImageUrl: String? = null,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceDefault,
                shadowElevation = 2.dp,
                onClick = onLogoutClick
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "Logout",
                        modifier = Modifier.size(24.dp),
                        tint = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Logout",
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_carret_right),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = TextSecondary
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = TextPrimary
            )
        )
    }
}

@Composable
private fun DocumentItem(
    title: String,
    showEdit: Boolean = false,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit = {},
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = TextPrimary
            )
        )

        IconButton(onClick = onViewClick, modifier = Modifier.size(48.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_eye),
                contentDescription = "View",
                modifier = Modifier.size(20.dp),
                tint = TextSecondary
            )
        }

        if (showEdit) {
            IconButton(onClick = onEditClick, modifier = Modifier.size(48.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pencil),
                    contentDescription = "Edit",
                    modifier = Modifier.size(20.dp),
                    tint = TextSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview() {
    AccountScreen()
}
