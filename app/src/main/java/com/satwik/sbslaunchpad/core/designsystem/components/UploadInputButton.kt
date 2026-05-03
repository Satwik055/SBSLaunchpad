package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.R
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.IconSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceDefault
import com.satwik.sbslaunchpad.core.designsystem.theme.SurfaceOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.TextPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TextSecondary
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily
import kotlinx.coroutines.selects.select

@Composable
fun UploadInputButton(
    text: String,
    selectedFileName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    uploadProgress: Float = 0f,
    onDeleteClick: (() -> Unit)? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = uploadProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "uploadProgress"
    )

    Surface(
        onClick = if (loading) ({}) else onClick,
        modifier = modifier
            .customShadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(12.dp),
                alpha = 0.2f,
            )
            .fillMaxWidth()
            .height(57.dp),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceDefault
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (loading) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.BottomCenter),
                    color = BrandPrimary,
                    trackColor = SurfaceOutline
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = TextPrimary,
                    ),
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Selected file name
                    Text(
                        text = if (loading) "Uploading..." else selectedFileName,
                        modifier.width(90.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = TextSecondary,
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    if (selectedFileName.isNotEmpty() && !loading && onDeleteClick != null) {
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_upload),
                            contentDescription = "Upload",
                            tint = IconSecondary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UploadInputButtonPreview() {
    UploadInputButton(
        text = "Attach 10th Marksheet",
        selectedFileName = "myresufdgdfgdfgdfgdfme.pdf",
        onClick = {},
        modifier = Modifier.padding(16.dp),
    )
}
