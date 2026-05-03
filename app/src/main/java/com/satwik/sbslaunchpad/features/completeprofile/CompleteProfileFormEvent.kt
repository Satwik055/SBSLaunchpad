package com.satwik.sbslaunchpad.features.completeprofile

sealed class CompleteProfileFormEvent {
    data class SemesterChanged(val semester: String) : CompleteProfileFormEvent()
    data class CourseChanged(val course: String) : CompleteProfileFormEvent()
    data class RollNumberChanged(val rollNumber: String) : CompleteProfileFormEvent()
    data class BacklogsChanged(val backlogs: String) : CompleteProfileFormEvent()
    data class CurrentAddressChanged(val currentAddress: String) : CompleteProfileFormEvent()
    data class PermanentAddressChanged(val permanentAddress: String) : CompleteProfileFormEvent()
    data class CgpaChanged(val cgpa: String) : CompleteProfileFormEvent()
    data class YearChanged(val year: String) : CompleteProfileFormEvent()
    data class TenthMarksPercentageChanged(val tenthMarksPercentage: String) : CompleteProfileFormEvent()
    data class TwelfthMarksPercentageChanged(val twelfthMarksPercentage: String) : CompleteProfileFormEvent()
    object DeleteProfileImage : CompleteProfileFormEvent()
    object DeleteResume : CompleteProfileFormEvent()
    object DeleteTenthMarksheet : CompleteProfileFormEvent()
    object DeleteTwelfthMarksheet : CompleteProfileFormEvent()
    object Submit : CompleteProfileFormEvent()
}
