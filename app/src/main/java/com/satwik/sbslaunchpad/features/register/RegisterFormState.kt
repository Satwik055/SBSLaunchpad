package com.satwik.sbslaunchpad.features.register

data class RegisterFormState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null
)
