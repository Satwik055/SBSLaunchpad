package com.satwik.sbslaunchpad.data.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber


/**
 * Data Transfer Object for the `profile` table in Supabase.
 *
 * ## Null Handling Strategy
 *
 * ### Fetching (DB → App):
 * [ProfileDto] mirrors the DB schema where all fields are nullable.
 * [toProfile] converts null values to sentinel defaults:
 * - String fields → empty string ""
 * - Int fields    → -1
 * - Double fields → -1.0
 * This avoids null handling throughout the app.
 *
 * ### Updating (App → DB):
 * [toDto] converts sentinel defaults back to null:
 * - String fields → null if empty ""
 * - Int fields    → null if 0
 * - Double fields → null if 0.0
 * Since the Supabase client is configured with (explicitNulls = false),
 * null fields are excluded from the PATCH request entirely.
 * This ensures only fields with real values are sent to the DB.
 *
 * ## Important
 * - Never send [ProfileDto] directly with all fields — use [toDto] which handles sentinel conversion
 * - [postSelectedIn] is a UUID foreign key — must never be sent as a non-null non-UUID string
 * - [status] and [isBlacklisted] are admin-only fields — cant be update via dedicated repository functions
 */
@Serializable
data class ProfileDto(
    val id: String? = null,
    @SerialName("fcm_token")
    val fcmToken: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    val email: String? = null,
    @SerialName("twelfth_marksheet_url")
    val twelfthMarksheetUrl: String? = null,
    @SerialName("tenth_marksheet_url")
    val tenthMarksheetUrl: String? = null,
    @SerialName("profile_image_url")
    val profileImageUrl: String? = null,
    val course: String? = null,
    val semester: Int? = null,
    val phone: String? = null,
    val backlogs: Int? = null,
    val cgpa: Double? = null,
    @SerialName("tenth_marks_percentage")
    val tenthMarksPercentage: Double? = null,
    @SerialName("twelfth_marks_percentage")
    val twelfthMarksPercentage: Double? = null,
    @SerialName("post_selected_in")
    val postSelectedIn: String? = null,
    val year: Int? = null,
    @SerialName("roll_number")
    val rollNumber: String? = null,
    @SerialName("permanent_address")
    val permanentAddress: String? = null,
    @SerialName("current_address")
    val currentAddress: String? = null,
    @SerialName("resume_url")
    val resumeUrl: String? = null,
    val status: ProfileStatus? = null,
    @SerialName("is_blacklisted")
    val isBlacklisted: Boolean? = null,
)

private const val TAG = "Timber-ProfileDto"

fun ProfileDto.toProfile(): Profile? {
    return try {
        Timber.tag(TAG).d("Mapping ProfileDto to Profile for user: $id, current status: $status")
        Profile(
            id = id?: "null",
            fcmToken = fcmToken ?: "null",
            fullName = fullName ?: "null",
            email = email ?: "null",
            twelfthMarksheetUrl = twelfthMarksheetUrl ?: "null",
            tenthMarksheetUrl = tenthMarksheetUrl ?: "null",
            profileImageUrl = profileImageUrl ?: "null",
            course = course ?: "null",
            semester = semester ?: 0,
            phone = phone ?: "null",
            backlogs = backlogs ?: 0,
            cgpa = cgpa ?: 0.0,
            tenthMarksPercentage = tenthMarksPercentage ?: 0.0,
            twelfthMarksPercentage = twelfthMarksPercentage ?: 0.0,
            postSelectedIn = postSelectedIn ?: "null",
            year = year ?: 0,
            rollNumber = rollNumber ?: "null",
            permanentAddress = permanentAddress ?: "null",
            currentAddress = currentAddress ?: "null",
            resumeUrl = resumeUrl ?: "null",
            status = status ?: ProfileStatus.PROFILE_COMPLETION_REQUIRED,
            isBlacklisted = isBlacklisted ?: false,
        )
    } catch (e: Exception) {
        Timber.tag(TAG).e(e, "Error mapping ProfileDto to Profile for user: $id. Message: %s", e.message)
        null
    }
}

fun Profile.toDto(): ProfileDto {
    return ProfileDto(
        id = id.takeIf { it != "null" },
        fcmToken = fcmToken.takeIf { it != "null" },
        status = status,
        isBlacklisted = isBlacklisted,
        fullName = fullName.takeIf { it != "null" },
        email = email.takeIf { it != "null" },
        twelfthMarksheetUrl = twelfthMarksheetUrl.takeIf { it != "null" },
        tenthMarksheetUrl = tenthMarksheetUrl.takeIf { it != "null" },
        profileImageUrl = profileImageUrl.takeIf { it != "null" },
        course = course.takeIf { it != "null" },
        phone = phone.takeIf { it != "null" },
        rollNumber = rollNumber.takeIf { it != "null" },
        permanentAddress = permanentAddress.takeIf { it != "null" },
        currentAddress = currentAddress.takeIf { it != "null" },
        resumeUrl = resumeUrl.takeIf { it != "null" },
        postSelectedIn = postSelectedIn.takeIf { it != "null" },
        semester = semester.takeIf { it != -1},
        backlogs = backlogs.takeIf { it != -1 },
        cgpa = cgpa.takeIf { it != -1.0 },
        tenthMarksPercentage = tenthMarksPercentage.takeIf { it != -1.0},
        twelfthMarksPercentage = twelfthMarksPercentage.takeIf { it != -1.0 },
        year = year.takeIf { it != -1 },

    )
}
