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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadJobPostCard
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadSearchBar
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.features.auth.local_component.LazyScrollShadows
import com.satwik.sbslaunchpad.data.post.Post
import com.satwik.sbslaunchpad.data.post.PostType

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onJobClick: (String) -> Unit = {}
) {
    val searchState = rememberTextFieldState()
    val posts = remember {
        listOf(
            Post(
                id = "1",
                profile = "Audit Assistant",
                companyName = "Deloitte",
                deadline = "3 Days",
                group = "Group A",
                city = "Banglore",
                applicants = "40 Applicants",
                amount = "₹ 6,00,000 - ₹ 7,00,000",
                type = PostType.JOB
            ),
            Post(
                id = "2",
                profile = "Key Accounts Manag...",
                companyName = "District",
                deadline = "4 Days",
                group = "Group B",
                city = "New Delhi",
                applicants = "20 Applicants",
                amount = "₹6,50,000",
                type = PostType.JOB
            ),
            Post(
                id = "3",
                profile = "Associate",
                companyName = "Daloopa",
                deadline = "5 Days",
                group = "Group B",
                city = "Mumbai",
                applicants = "10 Applicants",
                amount = "₹5,00,000",
                type = PostType.JOB
            ),
            Post(
                id = "4",
                profile = "Reservation Associate",
                companyName = "Oberoi Group",
                deadline = "6 Days",
                group = "Group C",
                city = "Mumbai",
                applicants = "8 Applicants",
                amount = "₹3,30,000",
                type = PostType.JOB
            ),
            Post(
                id = "7",
                profile = "Data Analyst",
                companyName = "Amazon",
                deadline = "2 Days",
                group = "Group A",
                city = "Chennai",
                applicants = "200 Applicants",
                amount = "₹ 12,00,000 - ₹ 15,00,000",
                type = PostType.JOB
            ),
            Post(
                id = "9",
                profile = "Full Stack Developer",
                companyName = "Zomato",
                deadline = "1 Day",
                group = "Group B",
                city = "Gurgaon",
                applicants = "150 Applicants",
                amount = "₹ 10,00,000 - ₹ 12,00,000",
                type = PostType.JOB
            )
        )
    }

    val filteredPosts by remember {
        derivedStateOf {
            val query = searchState.text.toString()
            if (query.isEmpty()) {
                emptyList<Post>()
            } else {
                posts.filter {
                    it.profile.contains(query, ignoreCase = true) ||
                            it.companyName.contains(query, ignoreCase = true)
                }
            }
        }
    }

    val scrollState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        LaunchpadSearchBar(
            state = searchState,
            placeholder = "Search jobs...",
            leadingButtonOnClick = { /* Handle back click */ },
            trailButtonOnClick = { searchState.edit { replace(0, length, "") } },
            leadingButtonIcon = R.drawable.ic_arrow,
            trailButtonIcon = R.drawable.ic_cross,
            autoFocus = true
        )
        
        HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)

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
                        jobProfile = post.profile,
                        companyName = post.companyName,
                        deadline = post.deadline,
                        group = post.group,
                        city = post.city,
                        applicants = post.applicants,
                        salary = post.amount,
                        isApplied = false,
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
        SearchScreen()
    }
}
