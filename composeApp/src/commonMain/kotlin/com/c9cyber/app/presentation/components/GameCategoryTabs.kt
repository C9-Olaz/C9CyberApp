package com.c9cyber.app.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundPrimary
import com.c9cyber.app.presentation.theme.TextPrimary

@Composable
fun GameCategoryTabs() {
    val categories = listOf("Game Online", "Game Offline", "Ứng dụng")
    var selectedTabIndex by remember { mutableStateOf(0) }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = BackgroundPrimary,
        contentColor = TextPrimary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = AccentColor,
                height = 3.dp
            )
        }
    ) {
        categories.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { Text(text = title) },
                selectedContentColor = AccentColor,
                unselectedContentColor = TextPrimary.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}
