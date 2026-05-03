package com.satwik.sbslaunchpad.features.completeprofile

import android.util.Patterns

object FormValidator {

    fun validateName(name: String): String? {
        return if (name.isBlank()) "Name cannot be empty" else null
    }

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
    }

    fun validatePhone(phone: String): String? {
        return when {
            phone.isBlank() -> "Phone cannot be empty"
            phone.length < 10 -> "Invalid phone number"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return if (password != confirmPassword) "Passwords do not match" else null
    }

    fun validateSemester(semester: String): String? {
        return when {
            semester.isBlank() -> "Semester cannot be empty"
            !semester.all { it.isDigit() } -> "Invalid semester"
            else -> null
        }
    }

    fun validateCourse(course: String):
            String? {
        return if (course.isBlank()) "Course cannot be empty" else null
    }

    fun validateRollNumber(rollNumber: String): String? {
        return if (rollNumber.isBlank()) "Roll number cannot be empty" else null
    }

    fun validateBacklogs(backlogs: String): String? {
        return when {
            backlogs.isBlank() -> "Backlogs cannot be empty"
            !backlogs.all { it.isDigit() } -> "Invalid backlogs"
            else -> null
        }
    }

    fun validateCurrentAddress(address: String): String? {
        return if (address.isBlank()) "Current address cannot be empty" else null
    }

    fun validatePermanentAddress(address: String): String? {
        return if (address.isBlank()) "Permanent address cannot be empty" else null
    }

    fun validateCgpa(cgpa: String): String? {
        val cgpaValue = cgpa.toDoubleOrNull()
        return when {
            cgpa.isBlank() -> "CGPA cannot be empty"
            cgpaValue == null -> "Invalid CGPA format"
            cgpaValue !in 0.0..10.0 -> "Cgpa should be between 0 - 10"
            else -> null
        }
    }

    fun validateYear(year: String): String? {
        return when {
            year.isBlank() -> "Year cannot be empty"
            !year.all { it.isDigit() } -> "Invalid year"
            else -> null
        }
    }

    fun validateTenthMarks(percentage: String): String? {
        val value = percentage.toDoubleOrNull()
        return when {
            percentage.isBlank() -> "10th marks percentage cannot be empty"
            value == null -> "Invalid percentage format"
            value !in 0.0..100.0 -> "Percentage should be between 0 - 100"
            else -> null
        }
    }

    fun validateTwelfthMarks(percentage: String): String? {
        val value = percentage.toDoubleOrNull()
        return when {
            percentage.isBlank() -> "12th marks percentage cannot be empty"
            value == null -> "Invalid percentage format"
            value !in 0.0..100.0 -> "Percentage should be between 0 - 100"
            else -> null
        }
    }

    fun validateFile(url: String, fileName: String): String? {
        return if (url.isBlank()) "$fileName is required" else null
    }
}
