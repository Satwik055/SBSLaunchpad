package com.satwik.sbslaunchpad.data.admin

import com.satwik.sbslaunchpad.data.admin.model.Admin
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import timber.log.Timber

class AdminRepositoryImpl(
    private val client: SupabaseClient
) : AdminRepository {

    private val tag = "Timber-${this::class.simpleName}"


    override suspend fun getAdminById(id: String): Admin? {
        Timber.tag(tag).d("Fetching admin by id: %s", id)
        return try {
            client.postgrest.from("admin").select {
                filter {
                    Admin::id eq id
                }
            }.decodeSingleOrNull<Admin>()
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Error fetching admin by id: %s", id)
            throw e
        }
    }
}