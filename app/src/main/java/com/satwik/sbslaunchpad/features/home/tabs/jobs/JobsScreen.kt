package com.satwik.sbslaunchpad.features.home.tabs.jobs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadJobPostCard
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.data.post.Post
import com.satwik.sbslaunchpad.features.auth.local_component.LazyScrollShadows
import com.satwik.sbslaunchpad.features.home.HomeViewModel

@Composable
fun JobsScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onJobClick: (String) -> Unit = {}
) {
    val result by viewModel.jobsState.collectAsState()
    val scrollState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        if (result.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrandPrimary)
        } else if (result.success) {
            val jobs = result.successResult as? List<Post> ?: emptyList()

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
                    items(jobs, key = { it.id }) { job ->
                        LaunchpadJobPostCard(
                            jobProfile = job.profile,
                            companyName = job.companyName,
                            deadline = job.deadline,
                            group = job.group,
                            city = job.city,
                            applicants = job.applicants,
                            salary = job.amount,
                            isApplied = false,
                            onClick = { onJobClick(job.id) }
                        )
                    }
                }
            }
        } else if (result.error.isNotEmpty()) {
            Text(
                text = result.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
