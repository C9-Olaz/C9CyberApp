package com.c9cyber.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.c9cyber.app.presentation.theme.AccentColor
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.caret_left
import org.jetbrains.compose.resources.painterResource

@Composable
fun ServiceCategoryList(
    selectedCategory: com.c9cyber.app.domain.model.ServiceType?,
    onCategorySelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val categories = listOf("Tất cả", "Đồ ăn", "Đồ uống")

    Column(
        modifier = Modifier.width(200.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            categories.forEach { category ->
                val isSelected = when (category) {
                    "Tất cả" -> selectedCategory == null
                    "Đồ ăn" -> selectedCategory == com.c9cyber.app.domain.model.ServiceType.Food
                    "Đồ uống" -> selectedCategory == com.c9cyber.app.domain.model.ServiceType.Drink
                    else -> false
                }
                CategoryItem(
                    text = category,
                    isSelected = isSelected,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                painter = painterResource(Res.drawable.caret_left),
                contentDescription = "Trở về",
                tint = AccentColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
