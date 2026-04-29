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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadJobPostCard
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.data.post.Post
import com.satwik.sbslaunchpad.data.post.PostType
import com.satwik.sbslaunchpad.data.profile.Profile
import com.satwik.sbslaunchpad.features.auth.local_component.LazyScrollShadows
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun JobsScreen(
    modifier: Modifier = Modifier,
    viewModel: JobsViewModel = koinViewModel(),
    onJobClick: (String) -> Unit = {}
) {
    val result by viewModel.uiState.collectAsState()
    val isAppliedLoading by viewModel.isAppliedLoading.collectAsState()
    val appliedPostIds by viewModel.appliedPostIds.collectAsState()
    val applicantsCounts by viewModel.applicantsCounts.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val scrollState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        if (result.isLoading || isAppliedLoading) {
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
                    items(jobs, key = { it.id }, contentType = { "job" }) { job ->
                        val isEligible = remember(profile, job) {
                            profile?.let { checkEligibility(it, job) } ?: true
                        }
                        val liveApplicants = remember(applicantsCounts, job) {
                            applicantsCounts[job.id] ?: job.applicants.toIntOrNull() ?: 0
                        }
                        val applicantsText = remember(liveApplicants) {
                            "$liveApplicants Applicants"
                        }
                        val isApplied = remember(appliedPostIds, job.id) {
                            appliedPostIds.contains(job.id)
                        }
                        val onCardClick = remember(job.id) {
                            { onJobClick(job.id) }
                        }

                        LaunchpadJobPostCard(
                            jobProfile = job.jobProfile,
                            companyName = job.companyName,
                            deadline = job.deadline,
                            group = "Group ${job.group}",
                            city = job.city,
                            applicants = applicantsText,
                            salary = job.amount,
                            isApplied = isApplied,
                            isEligible = isEligible,
                            postType = PostType.JOB,
                            onClick = onCardClick
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

private fun checkEligibility(profile: Profile, post: Post): Boolean {
    val courseMatch = post.reqCourses.isEmpty() || post.reqCourses.any { it.equals(profile.course, ignoreCase = true) }
    val cgpaMatch = profile.cgpa >= post.reqCgpa
    val yearMatch = post.reqYear.isEmpty() || post.reqYear.contains(profile.year)
    val tenthMatch = profile.tenthMarksPercentage >= post.reqTenthMarksPercentage
    val twelfthMatch = profile.twelfthMarksPercentage >= post.reqTwelfthMarksPercentage
    val backlogMatch = post.reqBacklog == null || profile.backlogs <= post.reqBacklog

    return courseMatch && cgpaMatch && yearMatch && tenthMatch && twelfthMatch && backlogMatch
}
