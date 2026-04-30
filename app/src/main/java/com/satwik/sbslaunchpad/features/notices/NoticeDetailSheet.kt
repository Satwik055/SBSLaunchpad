package com.satwik.sbslaunchpad.features.notices

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.theme.SBSLaunchpadTheme
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.poppins
import com.satwik.sbslaunchpad.core.util.toFormattedTimestamp
import com.satwik.sbslaunchpad.data.notice.model.Notice

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeDetailSheet(
    notice: Notice,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDefault,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 48.dp)
        ) {
            Text(
                text = notice.title,
                fontFamily = poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = SurfaceOutline, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = notice.body,
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = notice.createdAt?.toFormattedTimestamp() ?: "",
                modifier = Modifier.align(Alignment.End),
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun NoticeDetailSheetPreview() {
    val sampleNotice = Notice(
        id = 1,
        title = "Final WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal WarningFinal Warning",
        body = "Students are advised to not use fake internship certificates in their resumes, once caught they will be blacklisted permanently from the placements. This is a very serious matter and the college will take strict action against those who are found guilty.",
        senderId = "admin1",
        createdAt = "2023-10-27T10:15:30+05:30"
    )

    SBSLaunchpadTheme {
        NoticeDetailSheet(
            notice = sampleNotice,
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {}
        )
    }
}
