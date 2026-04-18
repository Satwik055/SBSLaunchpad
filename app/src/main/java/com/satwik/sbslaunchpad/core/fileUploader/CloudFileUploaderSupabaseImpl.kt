package com.satwik.sbslaunchpad.core.fileUploader

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CloudFileUploaderSupabaseImpl(
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
        bucket.uploadAsFlow(path, fileByteArray).collect {
            when (it) {
                is UploadStatus.Progress -> {
                    if (it.contentLength > 0L) {
                        emit((it.totalBytesSend.toFloat() / it.contentLength))
                    }
                }
                is UploadStatus.Success -> emit(1f)
            }
        }
    }

}
