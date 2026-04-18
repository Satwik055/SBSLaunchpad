package com.satwik.sbslaunchpad.data.post

import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPostsByType(type: PostType): Flow<List<Post>>
    fun getPostDetail(id: String): Flow<Post?>
}
