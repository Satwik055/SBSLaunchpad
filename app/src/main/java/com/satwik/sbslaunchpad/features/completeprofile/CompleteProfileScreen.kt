package com.satwik.sbslaunchpad.features.completeprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.components.LaunchpadButton
import com.satwik.sbslaunchpad.core.designsystem.components.MultiTextField
import com.satwik.sbslaunchpad.core.designsystem.components.UploadInputButton
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@Composable
fun CompleteProfileScreen(
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var backlogs by remember { mutableStateOf("") }
    var examRollNumber by remember { mutableStateOf("") }
    var permanentAddress by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val placeholders = remember {
        listOf(
            "First Name",
            "Last Name",
            "Semester",
            "Course",
            "Roll Number",
            "Backlogs",
            "Exam Roll Number",
            "Permanent Address"
        )
    }

    val values = listOf(
        firstName,
        lastName,
        semester,
        course,
        rollNumber,
        backlogs,
        examRollNumber,
        permanentAddress
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top Bar
        Box(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(
                onClick = { /* TODO: Handle back click */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_carret2),
                    contentDescription = "Back",
                    modifier = Modifier.size(16.dp),
                    tint = TextPrimary
                )
            }

            Text(
                text = "Complete Profile",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Form Fields
                MultiTextField(
                    items = placeholders,
                    values = values,
                    onValueChange = { index, newValue ->
                        when (index) {
                            0 -> firstName = newValue
                            1 -> lastName = newValue
                            2 -> semester = newValue
                            3 -> course = newValue
                            4 -> rollNumber = newValue
                            5 -> backlogs = newValue
                            6 -> examRollNumber = newValue
                            7 -> permanentAddress = newValue
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Upload Section
                Text(
                    text = "Upload Documents",
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = TextPrimary
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                UploadInputButton(
                    text = "Attach 10th Marksheet",
                    onClick = { /* TODO: Handle upload */ },
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                UploadInputButton(
                    text = "Attach 12th Marksheet",
                    onClick = { /* TODO: Handle upload */ },
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                UploadInputButton(
                    text = "Attach Resume",
                    onClick = { /* TODO: Handle upload */ },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Optimized Top shadow effect
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(24.dp)
                    .graphicsLayer {
                        // Use canScrollBackward to show shadow when user has scrolled down
                        alpha = if (scrollState.canScrollBackward) 1f else 0f
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Optimized Bottom shadow effect to indicate more scrollable content
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(24.dp)
                    .graphicsLayer {
                        // Move state read to draw phase to avoid recomposing the whole screen during scroll
                        alpha = if (scrollState.canScrollForward) 1f else 0f
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.08f)
                            )
                        )
                    )
            )
        }

        // Bottom Button (Sticky)
        LaunchpadButton(
            text = "Submit",
            onClick = { /* TODO: Handle submit */ },
            modifier = Modifier.padding(bottom = 10.dp, top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompleteProfileScreenPreview() {
    CompleteProfileScreen(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))
}
