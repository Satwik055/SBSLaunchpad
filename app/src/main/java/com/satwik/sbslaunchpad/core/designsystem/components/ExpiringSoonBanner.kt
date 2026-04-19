package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandOnPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.core.util.getRemainingTime
import androidx.compose.ui.tooling.preview.Preview
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun ExpiringSoonBanner(
    deadline: Instant,
    modifier: Modifier = Modifier
) {
    var remainingTime by remember(deadline) { mutableStateOf(deadline.getRemainingTime()) }

    LaunchedEffect(deadline) {
        while (true) {
            remainingTime = deadline.getRemainingTime()
            delay(1.seconds)
        }
    }

    if (remainingTime.contains(":")) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .background(BrandPrimary)
                .padding(vertical = 8.dp, horizontal = 16.dp),
        ) {

            Text(
                text = "This post is expiring soon",
                style = TextStyle(
                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = BrandOnPrimary
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Time Left: ",
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = BrandOnPrimary
                    )
                )
                Text(
                    text = remainingTime,
                    style = TextStyle(
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = BrandOnPrimary
                    )
                )

            }

        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun ExpiringSoonBannerPreview() {
    val deadline = Instant.fromEpochSeconds(Clock.System.now().epochSeconds + 3600)
    SBSLaunchpadTheme {
        ExpiringSoonBanner(deadline = deadline)
    }
}
