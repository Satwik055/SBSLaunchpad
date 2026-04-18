package com.satwik.sbslaunchpad.core.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

/**
 * Extracts the real file name from a Uri.
 */
fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    
    // Handle "content://" URIs (Standard for modern Android File Picker)
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    result = it.getString(nameIndex)
                }
            }
        }
    }
    
    // Fallback for "file://" URIs
    if (result == null) {
        result = uri.path?.let { path ->
            val cut = path.lastIndexOf('/')
            if (cut != -1) path.substring(cut + 1) else path
        }
    }
    
    return result
}