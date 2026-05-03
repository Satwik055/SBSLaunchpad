package com.satwik.sbslaunchpad.data.post

import com.satwik.sbslaunchpad.data.post.model.Post
import com.satwik.sbslaunchpad.data.post.model.PostType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class SupabasePostRepositoryImpl(
    private val client: SupabaseClient
) : PostRepository {

    private val tag = "Timber-${this::class.simpleName}"


    @OptIn(SupabaseExperimental::class)
    override fun getAllPostsByType(type: PostType): Flow<List<Post>> {
        Timber.tag(tag).d("Fetching all posts by type: %s", type)
        val posts = client.postgrest.from("post").selectAsFlow(
            primaryKey = Post::id,
            filter = FilterOperation("type", FilterOperator.EQ, type.name)
        ).onStart { (throw Exception("Ek pyara sa exception")) }.catch { e ->
            Timber.tag(tag).e(e, "Error fetching all posts by type: %s", type)
            throw e
        }
        return posts
    }


    @OptIn(SupabaseExperimental::class)
    override fun getPostById(id: String): Flow<Post?> {
        Timber.tag(tag).d("Fetching post by id: %s", id)
        return client.postgrest.from("post").selectSingleValueAsFlow(
            primaryKey = Post::id,
            filter = {
                eq("id", id)
            }
        ).catch { e ->
            Timber.tag(tag).e(e, "Error fetching post by id: %s", id)
            throw e
        }
    }

    override suspend fun searchPost(query: String): List<Post> {
        Timber.tag(tag).d("Searching posts with query: %s", query)
        try {
            val result = client.postgrest.from("post")
                .select {
                    filter {
                        or {
                            ilike("job_profile", "%$query%")
                            ilike("company_name", "%$query%")
                        }
                    }
                }
                .decodeList<Post>()
            Timber.tag(tag).d("Search results count: %d", result.size)
            return result
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Error searching posts with query: %s", query)
            throw e
        }
    }
}
