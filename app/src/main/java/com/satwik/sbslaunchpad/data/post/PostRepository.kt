package com.satwik.sbslaunchpad.data.post

import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPostsByType(type: PostType): Flow<List<Post>>
    fun getPostById(id: String): Flow<Post?>

    fun searchPost(query: String): Flow<List<Post>>)
}
