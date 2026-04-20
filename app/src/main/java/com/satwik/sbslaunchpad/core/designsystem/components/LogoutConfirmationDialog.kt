package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = BackgroundDefault,
            modifier = Modifier.fillMaxWidth().padding(horizontal =20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(23.dp)
                    .fillMaxWidth()
            ) {
                //Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_caution),
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Logout",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Message
                Text(
                    text = "Are you sure you want to logout of the app ?",
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LaunchpadButton(
                        text = "Confirm",
                        onClick = onConfirm,
                        modifier = Modifier
                            .height(45.dp)
                            .weight(1f)
                            .customShadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                alpha = 0.4f
                            ),
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )

                    LaunchpadButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        modifier = Modifier
                            .height(45.dp)
                            .weight(1f)
                            .customShadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                alpha = 0.5f
                            )
                        ,
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LogoutConfirmationDialogPreview() {
    LogoutConfirmationDialog(
        onConfirm = {},
        onDismiss = {}
    )
}
