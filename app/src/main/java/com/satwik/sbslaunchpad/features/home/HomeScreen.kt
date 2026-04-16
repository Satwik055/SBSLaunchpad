package com.satwik.sbslaunchpad.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.features.home.HomeTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTabRow
import com.satwik.sbslaunchpad.core.designsystem.components.SearchBarTrigger
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.IconSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.features.home.internships.InternshipsScreen
import com.satwik.sbslaunchpad.features.home.jobs.JobsScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onJobClick: (String) -> Unit = {},
    onInternshipClick: (String) -> Unit = {}
) {
    val tabs = listOf("Jobs", "Internships")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.background(BackgroundDefault)) {
                HomeTopAppBar(
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick
                )
                SearchBarTrigger(
                    onClick = onSearchClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                LaunchpadTabRow(
                    selectedTabIndex = pagerState.targetPage,
                    tabs = tabs,
                    onTabSelected = { index ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        },
        containerColor = BackgroundDefault
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            beyondViewportPageCount = 1
        ) { page ->
            when (page) {
                0 -> JobsScreen(onJobClick = onJobClick)
                1 -> InternshipsScreen(onInternshipClick = onInternshipClick)
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    SBSLaunchpadTheme {
        HomeScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    userName: String = "Satwik Kumar",
    userId: String = "20324947349",
    notificationCount: Int = 2,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Profile Image using drawable
        Box(
            modifier = Modifier.Companion
                .size(42.dp)
                .clip(CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Companion.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Profile",
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.Companion.width(12.dp))

        // User Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = userName,
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
                        modifier = Modifier.Companion.offset(x = (-8).dp, y = 8.dp)
                    ) {
                        Text(
                            text = notificationCount.toString(),
                            style = TextStyle(
                                fontFamily = fontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Companion.Normal
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
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeTopAppBarPreview() {
    SBSLaunchpadTheme {
        HomeTopAppBar()
    }
}