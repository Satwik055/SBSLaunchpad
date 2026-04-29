package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.ErrorContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.OnErrorContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.OnSuccessContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.OnWarningContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SuccessContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.WarningContainer
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

enum class CalloutType {
    Success,
    Error,
    Warning
}

@Composable
fun CalloutCard(
    title: String,
    description: String,
    type: CalloutType,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    reason: String? = null
) {
    val (containerColor, contentColor, iconRes) = when (type) {
        CalloutType.Success -> Triple(SuccessContainer, OnSuccessContainer, R.drawable.ic_check_filled)
        CalloutType.Error -> Triple(ErrorContainer, OnErrorContainer, R.drawable.ic_cross_filled)
        CalloutType.Warning -> Triple(WarningContainer, OnWarningContainer, R.drawable.ic_info_filled)
    }

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(17.dp)
                )

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = contentColor
                    )
                )

                if (onDismiss != null) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross),
                            contentDescription = "Dismiss",
                            tint = contentColor
                        )
                    }
                }
            }

            Column {
                Text(
                    text = description,
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = contentColor,
                        lineHeight = 20.sp
                    )
                )

                if (reason != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Reason for rejection: $reason",
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = contentColor
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalloutCardSuccessPreview() {
    SBSLaunchpadTheme {
        CalloutCard(
            title = "Profile Edit Request Approved",
            description = "Your profile edit request has been approved and changes has been made to your profile",
            type = CalloutType.Success,
            onDismiss = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalloutCardErrorPreview() {
    SBSLaunchpadTheme {
        CalloutCard(
            title = "Pending Edit Request Rejected",
            description = "Your profile edit request has been rejected by admin.",
            reason = "Marksheet is not in your name",
            type = CalloutType.Error,
            onDismiss = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalloutCardWarningPreview() {
    SBSLaunchpadTheme {
        CalloutCard(
            title = "Pending Edit Request",
            onDismiss = {},
            description = "You have an active profile edit request.Waiting for the admin approval to imply the changes",
            type = CalloutType.Warning,
            modifier = Modifier.padding(16.dp)
        )
    }
}
