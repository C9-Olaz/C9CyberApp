package com.c9cyber.app.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.TextPrimary

@Composable
fun CategoryItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (isSelected) AccentColor else TextPrimary
    val borderModifier = if (isSelected) {
        Modifier.border(2.dp, AccentColor, RoundedCornerShape(8.dp))
    } else {
        Modifier
    }

    Text(
        text = text,
        color = textColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(borderModifier)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    )
}
