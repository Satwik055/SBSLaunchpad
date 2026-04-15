package com.satwik.sbslaunchpad.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.core.designsystem.components.HomeTopAppBar
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTabRow
import com.satwik.sbslaunchpad.core.designsystem.components.SearchBarTrigger
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
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
