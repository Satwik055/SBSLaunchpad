package com.satwik.sbslaunchpad.data.admin

import com.satwik.sbslaunchpad.data.admin.model.Admin

interface AdminRepository {
    suspend fun getAdminById(id: String): Admin?
}