package com.satwik.sbslaunchpad.features.home.local_component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.IconSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    firstName: String,
    userId: String,
    profilePictureUrl: String? = null,
    notificationCount: Int = 2,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(90.dp)
            .fillMaxWidth()
            .padding(horizontal = LocalHorizontalAppPadding.current),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Profile Image using Coil
        Box(
            modifier = Modifier.Companion
                .size(45.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = BrandPrimary, shape = CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Companion.Center
        ) {
            AsyncImage(
                model = profilePictureUrl ?: R.drawable.ic_profile,
                contentDescription = "Profile",
                modifier = Modifier.size(45.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_profile),
                error = painterResource(id = R.drawable.ic_profile)
            )
        }

        Spacer(modifier = Modifier.Companion.width(12.dp))

        // User Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = firstName,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Companion.Normal,
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    color = TextPrimary
                )
            )
            Text(
                text = userId,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Companion.Normal,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            )
        }

        // Notification Icon with Badge anchored to the IconButton to prevent clipping
        BadgedBox(
            badge = {
                if (notificationCount > 0) {
                    Badge(
                        containerColor = BrandPrimary,
                        contentColor = TextPrimary,
                        // Offset the badge so it sits on top of the IconButton without being cut
                        modifier = Modifier.size(20.dp).offset(x = (-8).dp, y = 8.dp)
                    ) {
                        Text(
                            text = notificationCount.toString(),
                            style = TextStyle(
                                fontFamily = fontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        ) {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = "Notifications",
                    tint = IconSecondary,
                    modifier = Modifier.size(23.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeTopAppBarPreview() {
    SBSLaunchpadTheme {
        HomeTopAppBar(
            firstName = "Satwik",
            userId = "satwik@example.com"
        )
    }
}