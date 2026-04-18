package com.satwik.sbslaunchpad.core.fileUploader

import kotlinx.coroutines.flow.Flow

interface CloudFileUploader {
    fun uploadFileProgress(
        folderName: String,
        fileName: String,
        fileByteArray: ByteArray
    ): Flow<Float>
}
