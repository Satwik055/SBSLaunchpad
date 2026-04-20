package com.satwik.sbslaunchpad.features.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadJobPostCard
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadSearchBar
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.features.auth.local_component.LazyScrollShadows
import kotlin.time.ExperimentalTime

import org.koin.compose.viewmodel.koinViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary

@OptIn(ExperimentalTime::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onJobClick: (String) -> Unit = {}
) {
    val searchState = viewModel.searchState
    val filteredPosts = viewModel.searchResults
    val isSearching = viewModel.isSearching


    val scrollState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        LaunchpadSearchBar(
            state = searchState,
            placeholder = "Search jobs...",
            leadingButtonOnClick = onBackClick,
            trailButtonOnClick = { searchState.edit { replace(0, length, "") } },
            leadingButtonIcon = R.drawable.ic_arrow,
            trailButtonIcon = R.drawable.ic_cross,
            autoFocus = true
        )

        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)

        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BrandPrimary)
            }
        }

        LazyScrollShadows(scrollState = scrollState) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                contentPadding = PaddingValues(
                    horizontal = LocalHorizontalAppPadding.current,
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredPosts, key = { it.id }) { post ->
                    LaunchpadJobPostCard(
                        jobProfile = post.jobProfile,
                        companyName = post.companyName,
                        deadline = post.deadline,
                        group = post.group,
                        city = post.city,
                        applicants = post.applicants,
                        salary = post.amount,
                        isApplied = false,
                        postType = post.type,
                        onClick = { onJobClick(post.id) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SBSLaunchpadTheme {
        SearchScreen(onBackClick = {})
    }
}
