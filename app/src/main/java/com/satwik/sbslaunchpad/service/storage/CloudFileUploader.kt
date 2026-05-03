package com.satwik.sbslaunchpad.service.storage

import kotlinx.coroutines.flow.Flow

//Remember to delete the users files from unapproved directory once the profile is rejected
interface CloudFileUploader {
    fun uploadFile(bucketName: String, path: String, fileByteArray: ByteArray): Flow<Float>
    
    suspend fun deleteFile(bucketName: String, path: String)

    suspend fun moveFile(bucketName: String, oldPath: String, newPath: String)

    suspend fun listFiles(bucketName: String, path: String): List<String>

    fun getPublicUrl(bucketName: String, path: String): String
}
