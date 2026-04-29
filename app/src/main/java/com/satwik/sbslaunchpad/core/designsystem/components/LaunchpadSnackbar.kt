package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun LaunchpadSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp, start = 10.dp, end = 10.dp)
    ) { data ->
        Surface(
            color = Color.Red,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(12.dp)
                .customShadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    alpha = 0.8f
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_caution),
                    contentDescription = "Error",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = data.visuals.message,
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun LaunchpadSnackbarHostPreview() {
    val hostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        hostState.showSnackbar("This is a sample error message.")
    }

    SBSLaunchpadTheme {
        LaunchpadSnackbarHost(hostState = hostState)
    }
}

