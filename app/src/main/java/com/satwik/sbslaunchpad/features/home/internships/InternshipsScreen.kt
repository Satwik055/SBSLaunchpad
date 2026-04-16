package com.satwik.sbslaunchpad.features.home.internships

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

data class InternshipPost(
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
fun InternshipsScreen(
    modifier: Modifier = Modifier,
    onInternshipClick: (String) -> Unit = {}
) {
    // Placeholder internships data
    val internships = remember {
        listOf(
            InternshipPost(
                id = "1",
                jobProfile = "Marketing Intern",
                companyName = "Zomato",
                deadline = "2 Days",
                group = "Group A",
                city = "Gurgaon",
                applicants = "150 Applicants",
                salary = "₹ 15,000"
            ),
            InternshipPost(
                id = "2",
                jobProfile = "Software Engineer Intern",
                companyName = "Google",
                deadline = "5 Days",
                group = "Group B",
                city = "Bangalore",
                applicants = "500 Applicants",
                salary = "₹ 1,00,000"
            ),
            InternshipPost(
                id = "3",
                jobProfile = "Data Science Intern",
                companyName = "Microsoft",
                deadline = "7 Days",
                group = "Group A",
                city = "Hyderabad",
                applicants = "250 Applicants",
                salary = "₹ 80,000"
            ),
            InternshipPost(
                id = "4",
                jobProfile = "UX Research Intern",
                companyName = "Meta",
                deadline = "10 Days",
                group = "Group C",
                city = "Remote",
                applicants = "120 Applicants",
                salary = "₹ 60,000"
            ),
            InternshipPost(
                id = "5",
                jobProfile = "Finance Intern",
                companyName = "Goldman Sachs",
                deadline = "3 Days",
                group = "Group B",
                city = "Mumbai",
                applicants = "300 Applicants",
                salary = "₹ 75,000"
            ),
            InternshipPost(
                id = "6",
                jobProfile = "Content Writing Intern",
                companyName = "Swiggy",
                deadline = "1 Day",
                group = "Group C",
                city = "Bangalore",
                applicants = "80 Applicants",
                salary = "₹ 20,000"
            ),
            InternshipPost(
                id = "7",
                jobProfile = "Business Analyst Intern",
                companyName = "McKinsey",
                deadline = "12 Days",
                group = "Group A",
                city = "Gurgaon",
                applicants = "400 Applicants",
                salary = "₹ 90,000"
            )
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(internships, key = { it.id }) { internship ->
            LaunchpadJobPostCard(
                jobProfile = internship.jobProfile,
                companyName = internship.companyName,
                deadline = internship.deadline,
                group = internship.group,
                city = internship.city,
                applicants = internship.applicants,
                salary = internship.salary,
                isApplied = false,
                onClick = { onInternshipClick(internship.id) }
            )
        }
    }
}
