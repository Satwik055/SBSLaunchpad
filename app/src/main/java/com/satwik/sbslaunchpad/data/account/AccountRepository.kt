package com.satwik.sbslaunchpad.data.account

interface AccountRepository {
    suspend fun getAccount(): Account?
    suspend fun updateAccount(account: Account)
    suspend fun uploadResume(uri: String): String
}
