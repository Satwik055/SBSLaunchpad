package com.satwik.sbslaunchpad.features.verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import com.satwik.sbslaunchpad.core.util.Result
import com.satwik.sbslaunchpad.features.main.LocalHorizontalAppPadding

@Composable
fun ProfileRejectedScreen(
    rejectionReason: String,
    onBackClick: () -> Unit,
    onResubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: Result = Result(),
    onClearError: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(uiState.error)
            onClearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundDefault,
        modifier = modifier.statusBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_carret2),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = TextPrimary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = LocalHorizontalAppPadding.current),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_circle_cross),
                    contentDescription = null,
                    modifier = Modifier.size(160.dp),
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Your profile was rejected",
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Note: $rejectionReason",
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(48.dp))

                LaunchpadButton(
                    text = "Resubmit",
                    onClick = onResubmitClick,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    loading = uiState.isLoading
                )

                // Spacer at the bottom to offset the top bar height and keep it visually centered
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileRejectedScreenPreview() {
    ProfileRejectedScreen(
        rejectionReason = "The picture you attached was blurry, please attatch a clear picture of yours",
        onBackClick = {},
        onResubmitClick = {}
    )
}
