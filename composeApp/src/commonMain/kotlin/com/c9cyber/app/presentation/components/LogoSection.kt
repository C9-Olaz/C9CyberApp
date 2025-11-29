package com.c9cyber.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.TextSecondary
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun LogoSection(isExpanded: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "C9 Cyber Center Logo",
            colorFilter = ColorFilter.tint(AccentColor),
            // Tăng kích thước logo ở đây
            modifier = Modifier.size(if (isExpanded) 64.dp else 56.dp)
        )
        AnimatedVisibility(visible = isExpanded) {
            Text(
                text = "C9 Cyber Center",
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}
