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
fun ServiceCategoryList(onBack: () -> Unit) {
    val categories = listOf("Đồ ăn", "Đồ uống")
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.width(200.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            categories.forEachIndexed { index, category ->
                CategoryItem(
                    text = category,
                    isSelected = selectedIndex == index,
                    onClick = { selectedIndex = index }
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
