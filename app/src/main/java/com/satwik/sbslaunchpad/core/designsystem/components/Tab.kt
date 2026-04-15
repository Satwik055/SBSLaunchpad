package com.satwik.sbslaunchpad.core.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    modifier: Modifier = Modifier
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
                    height = 4.dp,
                    color = TabActiveIndicator
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = selectedTabIndex == index
                val interactionSource = remember { MutableInteractionSource() }
                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    Tab(
                        selected = selected,
                        onClick = { onTabSelected(index) },
                        interactionSource = interactionSource,
                        text = {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = if (selected) TabActiveText else TabInactiveText
                                )
                            )
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
        onTabSelected = {}
    )
}
