package com.satwik.sbslaunchpad.data.post

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class SupabasePostRepositoryImpl(
    private val client: SupabaseClient
) : PostRepository {

    @OptIn(SupabaseExperimental::class)
    override fun getAllPostsByType(type: PostType): Flow<List<Post>> {
        return client.postgrest.from("post").selectAsFlow(
            primaryKey = Post::id,
            filter = FilterOperation("type", FilterOperator.EQ, type.name)
        )
    }


    @OptIn(SupabaseExperimental::class)
    override fun getPostDetail(id: String): Flow<Post?> {
        return client.postgrest.from("post").selectSingleValueAsFlow(
            primaryKey = Post::id,
            filter = {
                eq("id", id)
            }
        )
    }
}
