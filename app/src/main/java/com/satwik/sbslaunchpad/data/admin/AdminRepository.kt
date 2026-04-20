package com.satwik.sbslaunchpad.data.admin

import com.satwik.sbslaunchpad.data.admin.Admin

interface AdminRepository {
    suspend fun getAdminById(id: String): Admin?
}