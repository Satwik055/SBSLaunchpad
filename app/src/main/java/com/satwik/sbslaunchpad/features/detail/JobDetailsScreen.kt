@file:OptIn(kotlin.time.ExperimentalTime::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.satwik.sbslaunchpad.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.ExpiringSoonBanner
import com.satwik.sbslaunchpad.core.designsystem.components.InfoChip
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.OnNeutralContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.core.util.toReadableDate
import com.satwik.sbslaunchpad.data.post.model.Post
import com.satwik.sbslaunchpad.core.designsystem.components.ScrollShadows
import com.satwik.sbslaunchpad.features.detail.local_components.JobDescriptionCard
import com.satwik.sbslaunchpad.features.detail.local_components.JobDetailsTopBar
import com.satwik.sbslaunchpad.features.detail.local_components.NoteSection
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant

@Composable
fun JobDetailsScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onApplyClick: () -> Unit = {}
) {
    val result by viewModel.uiState.collectAsState()
    val applicationResult by viewModel.applicationState.collectAsState()
    val isApplied by viewModel.isApplied.collectAsState()
    val isAppliedLoading by viewModel.isAppliedLoading.collectAsState()
    val isEligible by viewModel.isEligible.collectAsState()
    val eligibilityReasons by viewModel.eligibilityReasons.collectAsState()
    val applicantsCount by viewModel.applicantsCount.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(id) {
        viewModel.loadDetail(id)
    }

    LaunchedEffect(applicationResult) {
        if (applicationResult.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(applicationResult.error)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        if (result.isLoading || isAppliedLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrandPrimary)
        } else if (result.success) {
            val post = result.successResult as Post
            JobDetailsContent(
                title = post.jobProfile,
                company = post.companyName,
                group = post.group,
                city = post.city,
                applicants = applicantsCount.toString(),
                description = post.description,
                compensation = post.amount.toString(),
                requirements = post.requirements,
                deadline = post.deadline,
                selectionProcess = post.selectionProcess,
                createdAt = post.createdAt,
                note = post.note,
                companyLogoUrl = post.companyLogoUrl,
                onBackClick = onBackClick,
                onApplyClick = { viewModel.applyForPost(post.id) },
                isApplying = applicationResult.isLoading,
                isApplied = isApplied,
                isAppliedLoading = isAppliedLoading,
                isEligible = isEligible,
                onSeeWhyClick = { showBottomSheet = true }
            )
        } else if (result.error.isNotEmpty()) {
            Text(
                text = result.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = BackgroundDefault
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = "Eligibility Requirements",
                        style = TextStyle(
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = OnNeutralContainer
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(eligibilityReasons) { reason ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_stack),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = BrandPrimary
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = reason,
                                    style = TextStyle(
                                        fontFamily = poppins,
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
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
    createdAt: Instant?,
    note: String,
    companyLogoUrl: String?,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit,
    onSeeWhyClick: () -> Unit = {},
    isApplying: Boolean = false,
    isApplied: Boolean = false,
    isAppliedLoading: Boolean = false,
    isEligible: Boolean = true
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        JobDetailsTopBar(
            title = title,
            company = company,
            onBackClick = onBackClick,
            companyLogoUrl = companyLogoUrl
        )

        ExpiringSoonBanner(deadline = deadline)

        if (!isEligible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "You are not eligible for this role.",
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )
                Text(
                    text = "See why",
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { onSeeWhyClick() }
                )
            }
        }

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
                        label = "Group $group",
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
                    deadline = deadline,
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
                text = when {
                    isApplied -> "Applied"
                    !isEligible -> "Not Eligible"
                    else -> "Apply Now"
                },
                onClick = onApplyClick,
                enabled = !isApplying && !isApplied && isEligible,
                modifier = Modifier.fillMaxWidth(),
                loading = isApplying,
                leadingIcon = if (isApplied) Icons.Default.Check else null
            )
        }
    }
}
