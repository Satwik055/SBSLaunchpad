package com.satwik.sbslaunchpad.features.jobs

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadJobPostCard
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.data.post.model.Post
import com.satwik.sbslaunchpad.data.post.model.PostType
import com.satwik.sbslaunchpad.data.profile.model.Profile
import com.satwik.sbslaunchpad.core.designsystem.components.LazyScrollShadows
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun JobsScreen(
    modifier: Modifier = Modifier,
    viewModel: JobsViewModel = koinViewModel(),
    onJobClick: (String) -> Unit = {}
) {
    val jobState by viewModel.jobState.collectAsState()
    val applicationState by viewModel.userApplicationState.collectAsState()
    val applicantsCountState by viewModel.applicantsCountState.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val scrollState = rememberLazyListState()

    val isLoading = jobState.isLoading || applicationState.isLoading || applicantsCountState.isLoading
    val error = jobState.error.ifEmpty { applicationState.error.ifEmpty { applicantsCountState.error } }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrandPrimary)
        } else if (error.isNotEmpty()) {
            Text(
                text = error,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (jobState.success) {
            val jobs = jobState.successResult as? List<Post> ?: emptyList()
            val appliedPostIds = applicationState.successResult as? Set<String> ?: emptySet()
            @Suppress("UNCHECKED_CAST")
            val applicantsMap = applicantsCountState.successResult as? Map<String, Int> ?: emptyMap()

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

                        val liveApplicants = remember(applicantsMap, job) {
                            applicantsMap[job.id] ?: job.applicants.toIntOrNull() ?: 0
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
