package com.satwik.sbslaunchpad.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadTabRow
import com.satwik.sbslaunchpad.core.designsystem.components.SearchBarTrigger
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.features.home.tabs.internships.InternshipsScreen
import com.satwik.sbslaunchpad.features.home.tabs.jobs.JobsScreen
import com.satwik.sbslaunchpad.features.home.local_component.HomeTopAppBar
import com.satwik.sbslaunchpad.data.profile.ProfileRepository
import org.koin.compose.koinInject
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    profileRepository: ProfileRepository = koinInject(),
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onJobClick: (String) -> Unit = {},
    onInternshipClick: (String) -> Unit = {}
) {
    val tabs = listOf("Jobs", "Internships")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    val profile by profileRepository.profile.collectAsState(null)


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        HomeTopAppBar(
            firstName = profile?.fullName ?: "Guest",
            userId = profile?.email ?: "",
            onNotificationClick = onNotificationClick,
            onProfileClick = onProfileClick
        )
        Spacer(Modifier.height(10.dp))

        SearchBarTrigger(
            onClick = onSearchClick,
            modifier = Modifier.padding(horizontal = LocalHorizontalAppPadding.current)
        )
        Spacer(Modifier.height(30.dp))
        LaunchpadTabRow(
            selectedTabIndex = pagerState.targetPage,
            tabs = tabs,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            beyondViewportPageCount = 1
        ) { page ->
            when (page) {
                0 -> JobsScreen(onJobClick = onJobClick)
                1 -> InternshipsScreen(onInternshipClick = onInternshipClick)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    SBSLaunchpadTheme {
        HomeScreen(modifier = Modifier.statusBarsPadding())
    }
}
