package com.satwik.sbslaunchpad.data.admin

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class AdminRepositoryImpl(
    private val client: SupabaseClient
) : AdminRepository {
    override suspend fun getAdminById(id: String): Admin? {
        return client.postgrest.from("admin").select {
            filter {
                Admin::id eq id
            }
        }.decodeSingleOrNull<Admin>()
    }
}