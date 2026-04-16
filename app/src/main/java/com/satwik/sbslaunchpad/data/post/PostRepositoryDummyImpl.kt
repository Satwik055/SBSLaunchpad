package com.satwik.sbslaunchpad.data.post

import kotlinx.coroutines.delay

class PostRepositoryDummyImpl : PostRepository {
    private val posts = listOf(
        Post(
            id = "j1",
            profile = "Audit Assistant",
            companyName = "Deloitte",
            deadline = "3 Days",
            group = "Group A",
            city = "Banglore",
            applicants = "40 Applicants",
            amount = "₹ 6,00,000 - ₹ 7,00,000",
            type = PostType.JOB,
            description = "We are looking for an Audit Assistant to join our team...",
            requirements = "Bachelor's degree in Accounting or Finance...",
            postedDate = "4 April 2026"
        ),
        Post(
            id = "j2",
            profile = "Key Accounts Manag...",
            companyName = "District",
            deadline = "4 Days",
            group = "Group B",
            city = "New Delhi",
            applicants = "20 Applicants",
            amount = "₹6,50,000",
            type = PostType.JOB,
            description = "Description for Key Accounts Manager...",
            requirements = "Requirements for Key Accounts Manager...",
            postedDate = "5 April 2026"
        ),
        Post(
            id = "j3",
            profile = "Associate",
            companyName = "Daloopa",
            deadline = "5 Days",
            group = "Group B",
            city = "Mumbai",
            applicants = "10 Applicants",
            amount = "₹5,00,000",
            type = PostType.JOB,
            description = "Description for Associate...",
            requirements = "Requirements for Associate...",
            postedDate = "6 April 2026"
        ),
        Post(
            id = "j4",
            profile = "Data Analyst",
            companyName = "Microsoft",
            deadline = "1 Week",
            group = "Group A",
            city = "Hyderabad",
            applicants = "85 Applicants",
            amount = "₹ 8,00,000 - ₹ 10,00,000",
            type = PostType.JOB,
            description = "Analyze complex data sets to provide actionable insights...",
            requirements = "Strong proficiency in SQL, Python, and PowerBI...",
            postedDate = "10 April 2026"
        ),
        Post(
            id = "j5",
            profile = "UX Designer",
            companyName = "Adobe",
            deadline = "2 Weeks",
            group = "Group C",
            city = "Noida",
            applicants = "45 Applicants",
            amount = "₹ 12,00,000",
            type = PostType.JOB,
            description = "Create intuitive and beautiful user experiences for our flagship products...",
            requirements = "Portfolio demonstrating strong design thinking and UI skills...",
            postedDate = "12 April 2026"
        ),
        Post(
            id = "i1",
            profile = "Marketing Intern",
            companyName = "Zomato",
            deadline = "2 Days",
            group = "Group A",
            city = "Gurgaon",
            applicants = "150 Applicants",
            amount = "₹ 15,000",
            type = PostType.INTERNSHIP,
            description = "Description for Marketing Intern...",
            requirements = "Requirements for Marketing Intern...",
            postedDate = "7 April 2026"
        ),
        Post(
            id = "i2",
            profile = "Software Engineer Intern",
            companyName = "Google",
            deadline = "5 Days",
            group = "Group B",
            city = "Bangalore",
            applicants = "500 Applicants",
            amount = "₹ 1,00,000",
            type = PostType.INTERNSHIP,
            description = "Description for Software Engineer Intern...",
            requirements = "Requirements for Software Engineer Intern...",
            postedDate = "8 April 2026"
        ),
        Post(
            id = "i3",
            profile = "Product Intern",
            companyName = "Flipkart",
            deadline = "10 Days",
            group = "Group B",
            city = "Bangalore",
            applicants = "200 Applicants",
            amount = "₹ 40,000",
            type = PostType.INTERNSHIP,
            description = "Assist product managers in defining features and conducting market research...",
            requirements = "MBA or final year engineering students with product interest...",
            postedDate = "14 April 2026"
        ),
        Post(
            id = "i4",
            profile = "Android Developer Intern",
            companyName = "Amazon",
            deadline = "1 Week",
            group = "Group A",
            city = "Chennai",
            applicants = "320 Applicants",
            amount = "₹ 60,000",
            type = PostType.INTERNSHIP,
            description = "Work with the mobile team to build scalable features for the Amazon app...",
            requirements = "Solid understanding of Kotlin and Jetpack Compose...",
            postedDate = "15 April 2026"
        )
    )

    override suspend fun getAllPostsByType(type: PostType): List<Post> {
        delay(2000) // Simulate network delay
        return posts.filter { it.type == type }
    }

    override suspend fun getPostDetail(id: String): Post? {
        delay(2000) // Simulate network delay
        return posts.find { it.id == id }
    }
}
