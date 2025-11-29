package com.c9cyber.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.c9cyber.app.presentation.theme.BackgroundSecondary

@Composable
fun GameCategoryList() {
    val categories = listOf("Game Online", "Game Offline", "Ứng dụng")
    var selectedIndex by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxHeight(),
        color = BackgroundSecondary,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            categories.forEachIndexed { index, category ->
                CategoryItem(
                    text = category,
                    isSelected = selectedIndex == index,
                    onClick = { selectedIndex = index }
                )
            }
        }
    }
}
