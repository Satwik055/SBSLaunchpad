package com.satwik.sbslaunchpad.features.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.theme.BackgroundDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.LaunchpadYellow
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

import androidx.compose.ui.tooling.preview.Preview
import com.satwik.sbslaunchpad.core.designsystem.components.customShadow
import com.satwik.sbslaunchpad.core.designsystem.theme.DefaultElevation
import com.satwik.sbslaunchpad.core.designsystem.theme.ElevationStrength
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDefault)
    ) {
        // Bottom Doodle Image
        Image(
            painter = painterResource(id = R.drawable.ic_college_doodle),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_home_app_icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(180.dp))

            // Title
            Text(
                text = "Shahed Bhagat Singh",
                fontSize = 20.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.offset(y=10.dp)
            )

            Text(
                text = "Launchpad",
                fontSize = 50.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(9.dp))

            HorizontalDivider(
                modifier = Modifier.width(240.dp),
                thickness = 1.dp,
                color = Color.Black.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "A platform to launch your career",
                fontSize = 13.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 120.dp), // Height above the doodle
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LaunchpadButton(
                    text = "Register",
                    onClick = onRegisterClick,
                    containerColor = LaunchpadYellow,
                    contentColor = Color.Black,
                    modifier = Modifier.customShadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(50.dp),
                        alpha = 0.5f
                    ),
                )

                LaunchpadButton(
                    text = "Login",
                    onClick = onLoginClick,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier.customShadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(50.dp),
                        alpha = 0.5f
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SBSLaunchpadTheme {
        WelcomeScreen()
    }
}
