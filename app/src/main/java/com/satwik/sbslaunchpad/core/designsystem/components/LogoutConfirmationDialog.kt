package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.satwik.sbslaunchpad.R

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(
        title = "Logout",
        message = "Are you sure you want to logout of the app ?",
        confirmButtonText = "Confirm",
        cancelButtonText = "Cancel",
        confirmButtonColor = Color.Red,
        icon = painterResource(id = R.drawable.ic_caution),
        iconTint = Color.Red,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}
