package com.satwik.sbslaunchpad.core.util

data class Result(
    val success: Boolean = false,
    val successResult: Any? = null,
    val error: String = "",
    val isLoading: Boolean = false,
)
