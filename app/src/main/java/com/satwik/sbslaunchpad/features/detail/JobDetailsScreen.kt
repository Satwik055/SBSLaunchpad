@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.satwik.sbslaunchpad.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.InfoChip
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.OnNeutralContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.core.util.getRemainingTime
import com.satwik.sbslaunchpad.core.util.toReadableDate
import com.satwik.sbslaunchpad.data.post.Post
import com.satwik.sbslaunchpad.features.auth.local_component.ScrollShadows
import com.satwik.sbslaunchpad.features.detail.local_components.JobDescriptionCard
import com.satwik.sbslaunchpad.features.detail.local_components.JobDetailsTopBar
import com.satwik.sbslaunchpad.features.detail.local_components.NoteSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JobDetailsScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onApplyClick: () -> Unit = {}
) {
    val result by viewModel.uiState.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadDetail(id)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        if (result.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrandPrimary)
        } else if (result.success) {
            val post = result.successResult as Post
            JobDetailsContent(
                title = post.jobProfile,
                company = post.companyName,
                group = post.group,
                city = post.city,
                applicants = post.applicants,
                description = post.description,
                compensation = post.amount.toString(),
                requirements = post.requirements,
                deadline = post.deadline,
                selectionProcess = post.selectionProcess,
                createdAt = post.createdAt,
                note = post.note,
                companyLogoUrl = post.companyLogoUrl,
                onBackClick = onBackClick,
                onApplyClick = onApplyClick
            )
        } else if (result.error.isNotEmpty()) {
            Text(
                text = result.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun JobDetailsContent(
    title: String,
    company: String,
    group: String,
    city: String,
    applicants: String,
    description: String,
    compensation: String,
    requirements: String,
    deadline: kotlin.time.Instant,
    selectionProcess: String,
    createdAt: kotlinx.datetime.Instant?,
    note: String,
    companyLogoUrl: String?,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        JobDetailsTopBar(
            title = title,
            company = company,
            onBackClick = onBackClick,
            companyLogoUrl = companyLogoUrl
        )

        ScrollShadows(
            scrollState = scrollState,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChip(
                        label = group,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_stack),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = OnNeutralContainer
                            )
                        }
                    )
                    InfoChip(
                        label = city,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_map_pin),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = OnNeutralContainer
                            )
                        }
                    )
                    InfoChip(label = "$applicants Applicants")
                }

                JobDescriptionCard(
                    description = description,
                    compensation = compensation,
                    requirements = requirements,
                    deadline = deadline.getRemainingTime(),
                    selectionProcess = selectionProcess
                )

                if (note.isNotEmpty()) {
                    NoteSection(note = note)
                }

                if (createdAt != null) {
                    Text(
                        text = "Posted on: ${createdAt.toReadableDate()}",
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = TextSecondary
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundDefault)
                .padding(16.dp)
        ) {
            LaunchpadButton(
                text = "Apply",
                onClick = onApplyClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
