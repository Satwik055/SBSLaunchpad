package com.satwik.sbslaunchpad.features.completeprofile

data class CompleteProfileFormState(
    val semester: String = "",
    val semesterError: String? = null,
    val course: String = "",
    val courseError: String? = null,
    val rollNumber: String = "",
    val rollNumberError: String? = null,
    val backlogs: String = "",
    val backlogsError: String? = null,
    val currentAddress: String = "",
    val currentAddressError: String? = null,
    val permanentAddress: String = "",
    val permanentAddressError: String? = null,
    val cgpa: String = "",
    val cgpaError: String? = null,
    val year: String = "",
    val yearError: String? = null,
    val tenthMarksPercentage: String = "",
    val tenthMarksPercentageError: String? = null,
    val twelfthMarksPercentage: String = "",
    val twelfthMarksPercentageError: String? = null,
    val resumeError: String? = null,
    val tenthMarksheetError: String? = null,
    val twelfthMarksheetError: String? = null
)
