package com.c9cyber.app.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(8.dp)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, AccentColor, cornerShape),
        placeholder = { Text("Tìm kiếm", color = TextSecondary) },

        leadingIcon = null,
        trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = AccentColor) },
        shape = cornerShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = AccentColor,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            unfocusedContainerColor = BackgroundSecondary,
            focusedContainerColor = BackgroundSecondary
        )
    )
}
