package com.satwik.sbslaunchpad.data.post

interface PostRepository {
    suspend fun getAllPostsByType(type: PostType): List<Post>
    suspend fun getPostDetail(id: String): Post?
}
