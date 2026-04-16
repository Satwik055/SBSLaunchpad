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
import com.satwik.sbslaunchpad.features.home.jobs.JobPost

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val searchState = rememberTextFieldState()
    val jobs = remember {
        listOf(
            JobPost(
                id = "1",
                jobProfile = "Audit Assistant",
                companyName = "Deloitte",
                deadline = "3 Days",
                group = "Group A",
                city = "Banglore",
                applicants = "40 Applicants",
                salary = "₹ 6,00,000 - ₹ 7,00,000"
            ),
            JobPost(
                id = "2",
                jobProfile = "Key Accounts Manag...",
                companyName = "District",
                deadline = "4 Days",
                group = "Group B",
                city = "New Delhi",
                applicants = "20 Applicants",
                salary = "₹6,50,000"
            ),
            JobPost(
                id = "3",
                jobProfile = "Associate",
                companyName = "Daloopa",
                deadline = "5 Days",
                group = "Group B",
                city = "Mumbai",
                applicants = "10 Applicants",
                salary = "₹5,00,000"
            ),
            JobPost(
                id = "4",
                jobProfile = "Reservation Associate",
                companyName = "Oberoi Group",
                deadline = "6 Days",
                group = "Group C",
                city = "Mumbai",
                applicants = "8 Applicants",
                salary = "₹3,30,000"
            ),
            JobPost(
                id = "5",
                jobProfile = "Software Engineer Intern",
                companyName = "Google",
                deadline = "10 Days",
                group = "Group A",
                city = "Bangalore",
                applicants = "100 Applicants",
                salary = "₹ 50,000 / Month"
            ),
            JobPost(
                id = "6",
                jobProfile = "Product Management Intern",
                companyName = "Microsoft",
                deadline = "15 Days",
                group = "Group B",
                city = "Hyderabad",
                applicants = "50 Applicants",
                salary = "₹ 40,000 / Month"
            ),
            JobPost(
                id = "7",
                jobProfile = "Data Analyst",
                companyName = "Amazon",
                deadline = "2 Days",
                group = "Group A",
                city = "Chennai",
                applicants = "200 Applicants",
                salary = "₹ 12,00,000 - ₹ 15,00,000"
            ),
            JobPost(
                id = "8",
                jobProfile = "UX Design Intern",
                companyName = "Adobe",
                deadline = "7 Days",
                group = "Group C",
                city = "Noida",
                applicants = "30 Applicants",
                salary = "₹ 35,000 / Month"
            ),
            JobPost(
                id = "9",
                jobProfile = "Full Stack Developer",
                companyName = "Zomato",
                deadline = "1 Day",
                group = "Group B",
                city = "Gurgaon",
                applicants = "150 Applicants",
                salary = "₹ 10,00,000 - ₹ 12,00,000"
            )
        )
    }

    val filteredJobs by remember {
        derivedStateOf {
            val query = searchState.text.toString()
            if (query.isEmpty()) {
                emptyList<JobPost>()
            } else {
                jobs.filter {
                    it.jobProfile.contains(query, ignoreCase = true) ||
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
                items(filteredJobs, key = { it.id }) { job ->
                    LaunchpadJobPostCard(
                        jobProfile = job.jobProfile,
                        companyName = job.companyName,
                        deadline = job.deadline,
                        group = job.group,
                        city = job.city,
                        applicants = job.applicants,
                        salary = job.salary,
                        isApplied = false,
                        onClick = { /* Handle job click */ }
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
