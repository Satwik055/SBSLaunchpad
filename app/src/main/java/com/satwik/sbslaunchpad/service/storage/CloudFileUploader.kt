package com.satwik.sbslaunchpad.service.storage

import kotlinx.coroutines.flow.Flow

interface CloudFileUploader {
    fun uploadFileProgress(
        folderName: String,
        fileName: String,
        fileByteArray: ByteArray
    ): Flow<Float>
}
