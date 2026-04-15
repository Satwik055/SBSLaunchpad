package com.satwik.sbslaunchpad.features.home.jobs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadJobPostCard

data class JobPost(
    val id: String,
    val jobProfile: String,
    val companyName: String,
    val deadline: String,
    val group: String,
    val city: String,
    val applicants: String,
    val salary: String,
    val companyLogoUrl: String? = null
)

@Composable
fun JobsScreen(
    modifier: Modifier = Modifier,
    onJobClick: (String) -> Unit = {}
) {
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
            )
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(jobs, key = { it.id }) { job ->
            LaunchpadJobPostCard(
                jobProfile = job.jobProfile,
                companyName = job.companyName,
                deadline = job.deadline,
                group = job.group,
                city = job.city,
                applicants = job.applicants,
                salary = job.salary,
                isApplied = false,
                onClick = { onJobClick(job.id) }
            )
        }
    }
}
