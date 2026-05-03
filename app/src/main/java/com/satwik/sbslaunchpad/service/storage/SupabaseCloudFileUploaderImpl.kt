package com.satwik.sbslaunchpad.service.storage

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SupabaseCloudFileUploaderImpl(
    private val supabase: SupabaseClient,
): CloudFileUploader {

    override suspend fun moveFile(bucketName: String, oldPath: String, newPath: String) {
        val bucket = supabase.storage.from(bucketName)
        bucket.move(oldPath, newPath)
    }

    override suspend fun listFiles(bucketName: String, path: String): List<String> {
        val bucket = supabase.storage.from(bucketName)
        return try {
            bucket.list(path).map { it.name }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun uploadFile(
        bucketName: String,
        path: String,
        fileByteArray: ByteArray
    ): Flow<Float> = flow {
        val bucket = supabase.storage.from(bucketName)
        bucket.uploadAsFlow(path, fileByteArray) {
            upsert = true
        }.collect { status ->
            when (status) {
                is UploadStatus.Progress -> {
                    if (status.contentLength > 0L) {
                        emit((status.totalBytesSend.toFloat() / status.contentLength))
                    }
                }

                is UploadStatus.Success -> emit(1f)
            }
        }
    }

    override suspend fun deleteFile(bucketName: String, path: String) {
        val bucket = supabase.storage.from(bucketName)
        bucket.delete(path)
    }

    override fun getPublicUrl(bucketName: String, path: String): String {
        return supabase.storage.from(bucketName).publicUrl(path)
    }

}
