package com.satwik.sbslaunchpad.features.internships

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
fun InternshipsScreen(
    modifier: Modifier = Modifier,
    viewModel: InternshipsViewModel = koinViewModel(),
    onInternshipClick: (String) -> Unit = {}
) {
    val internshipState by viewModel.internshipState.collectAsState()
    val userApplicationState by viewModel.userApplicationState.collectAsState()
    val applicantsCountState by viewModel.applicantsCountState.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val scrollState = rememberLazyListState()

    val isLoading = internshipState.isLoading || userApplicationState.isLoading || applicantsCountState.isLoading
    val error = internshipState.error.ifEmpty { userApplicationState.error.ifEmpty { applicantsCountState.error } }

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
        } else if (internshipState.success) {
            @Suppress("UNCHECKED_CAST")
            val internships = internshipState.successResult as? List<Post> ?: emptyList()
            @Suppress("UNCHECKED_CAST")
            val appliedPostIds = userApplicationState.successResult as? Set<String> ?: emptySet()
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
                    items(internships, key = { it.id }, contentType = { "internship" }) { internship ->
                        val isEligible = remember(profile, internship) {
                            profile?.let { checkEligibility(it, internship) } ?: true
                        }
                        val liveApplicants = remember(applicantsMap, internship) {
                            applicantsMap[internship.id] ?: internship.applicants.toIntOrNull() ?: 0
                        }
                        val applicantsText = remember(liveApplicants) {
                            "$liveApplicants Applicants "
                        }
                        val isApplied = remember(appliedPostIds, internship.id) {
                            appliedPostIds.contains(internship.id)
                        }
                        val onCardClick = remember(internship.id) {
                            { onInternshipClick(internship.id) }
                        }

                        LaunchpadJobPostCard(
                            jobProfile = internship.jobProfile,
                            companyName = internship.companyName,
                            deadline = internship.deadline,
                            group = "Group ${internship.group}",
                            city = internship.city,
                            applicants = applicantsText,
                            salary = internship.amount,
                            postType = PostType.INTERNSHIP,
                            isApplied = isApplied,
                            isEligible = isEligible,
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
