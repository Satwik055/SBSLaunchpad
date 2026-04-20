package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandOnPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.BrandPrimary
import com.satwik.sbslaunchpad.core.designsystem.theme.TabActiveIndicator
import com.satwik.sbslaunchpad.core.designsystem.theme.TabActiveText
import com.satwik.sbslaunchpad.core.designsystem.theme.TabInactiveText
import com.satwik.sbslaunchpad.core.designsystem.theme.TabOutline
import com.satwik.sbslaunchpad.core.designsystem.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchpadTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    unreadCounts: List<Int> = emptyList()
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = TabActiveText,
            divider = {}, // We will use a custom divider below to match the design
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
                    height = 3.dp,
                    color = TabActiveIndicator
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = selectedTabIndex == index
                val unreadCount = unreadCounts.getOrNull(index) ?: 0
                val interactionSource = remember { MutableInteractionSource() }
                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    Tab(
                        selected = selected,
                        onClick = { onTabSelected(index) },
                        interactionSource = interactionSource,
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = title,
                                    style = TextStyle(
                                        fontFamily = fontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = if (selected) TabActiveText else TabInactiveText
                                    )
                                )

                                if (unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(BrandPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                                            style = TextStyle(
                                                fontFamily = fontFamily,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 11.sp,
                                                color = BrandOnPrimary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = TabOutline
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LaunchpadTabRowPreview() {
    LaunchpadTabRow(
        selectedTabIndex = 0,
        tabs = listOf("Updates", "Notices"),
        onTabSelected = {},
        unreadCounts = listOf(5, 0)
    )
}
