package com.satwik.sbslaunchpad.features.editprofile

sealed class EditProfileFormEvent {
    data class NameChanged(val name: String) : EditProfileFormEvent()
    data class EmailChanged(val email: String) : EditProfileFormEvent()
    data class CourseChanged(val course: String) : EditProfileFormEvent()
    data class SemesterChanged(val semester: String) : EditProfileFormEvent()
    data class PhoneChanged(val phone: String) : EditProfileFormEvent()
    data class BacklogsChanged(val backlogs: String) : EditProfileFormEvent()
    data class RollNumberChanged(val rollNumber: String) : EditProfileFormEvent()
    data class CgpaChanged(val cgpa: String) : EditProfileFormEvent()
    data class TenthMarksPercentageChanged(val percentage: String) : EditProfileFormEvent()
    data class TwelfthMarksPercentageChanged(val percentage: String) : EditProfileFormEvent()
    data class YearChanged(val year: String) : EditProfileFormEvent()
    data class PermanentAddressChanged(val address: String) : EditProfileFormEvent()
    data class CurrentAddressChanged(val address: String) : EditProfileFormEvent()
    object Submit : EditProfileFormEvent()
}
