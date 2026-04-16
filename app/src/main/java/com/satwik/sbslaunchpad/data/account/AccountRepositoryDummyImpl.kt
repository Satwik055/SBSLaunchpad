package com.satwik.sbslaunchpad.data.account

import kotlinx.coroutines.delay

class AccountRepositoryDummyImpl : AccountRepository {
    private var cachedAccount = Account(
        id = "user123",
        name = "Satwik",
        email = "satwik@example.com",
        university = "Delhi University",
        graduationYear = "2026",
        course = "Bcom(Hons)",
        semester = "6th",
        phone = "+91 2434453342",
        backlogs = "3",
        rollNumber = "2023/0399",
        bio = "Android Developer | Tech Enthusiast",
        isVerified = true
    )

    override suspend fun getAccount(): Account? {
        delay(1000)
        return cachedAccount
    }

    override suspend fun updateAccount(account: Account) {
        delay(1000)
        cachedAccount = account
    }

    override suspend fun uploadResume(uri: String): String {
        delay(2000)
        return "https://example.com/resumes/user123_resume.pdf"
    }
}
