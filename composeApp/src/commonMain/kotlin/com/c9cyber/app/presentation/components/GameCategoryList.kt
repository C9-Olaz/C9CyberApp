package com.c9cyber.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.c9cyber.app.domain.model.GameType
import com.c9cyber.app.presentation.theme.BackgroundSecondary

@Composable
fun GameCategoryList(
    selectedCategory: GameType?,
    onCategorySelected: (GameType?) -> Unit
) {
    val categories = listOf("Tất cả", "Game Online", "Game Offline")

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
            categories.forEach { category ->
                val isSelected = when (category) {
                    "Tất cả" -> selectedCategory == null
                    "Game Online" -> selectedCategory == GameType.Online
                    "Game Offline" -> selectedCategory == GameType.Offline
                    else -> false
                }
                CategoryItem(
                    text = category,
                    isSelected = isSelected,
                    onClick = {
                        onCategorySelected(
                            when (category) {
                                "Tất cả" -> null
                                "Game Online" -> GameType.Online
                                "Game Offline" -> GameType.Offline
                                else -> null
                            }
                        )
                    }
                )
            }
        }
    }
}
