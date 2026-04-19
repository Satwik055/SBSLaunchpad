package com.satwik.sbslaunchpad.data.post

import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPostsByType(type: PostType): Flow<List<Post>>
    fun getPostById(id: String): Flow<Post?>

    suspend fun searchPost(query: String): List<Post>
}
