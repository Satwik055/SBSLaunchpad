package com.satwik.sbslaunchpad.core.util

import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.auth.exception.AuthRestException

object ErrorMessages {

    //Showing only one simple error and avoided showing different error messages for different types of errors to avoid users to get confused by the error message
    const val JOB_SCREEN_ERROR = "Something went wrong while finding jobs"
    const val INTERNSHIP_SCREEN_ERROR = "Something went wrong while finding internships"
    const val MARK_AS_READ_ERROR = "Something went wrong"
}

