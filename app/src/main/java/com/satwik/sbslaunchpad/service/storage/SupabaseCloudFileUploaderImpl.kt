package com.satwik.sbslaunchpad.service.storage

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SupabaseCloudFileUploaderImpl(
    private val supabase: SupabaseClient,
): CloudFileUploader {

    private fun getUserId(): String {
        return supabase.auth.currentUserOrNull()?.id ?: "unknown"
    }

    private fun getFilePath(folderName: String, fileName: String): String {
        return "${getUserId()}/$folderName/$fileName"
    }

    override fun uploadFileProgress(
        folderName: String,
        fileName: String,
        fileByteArray: ByteArray
    ): Flow<Float> = flow {
        val path = getFilePath(folderName, fileName)
        val bucket = supabase.storage.from("profile")
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

}
