package com.c9cyber.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary

@Composable
fun TimeStatusPanel(isExpanded: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundSecondary)
    ) {
        if (isExpanded) {
            ExpandedContent()
        } else {
            CollapsedContent()
        }
    }
}

@Composable
private fun ExpandedContent() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimeRowItem(label = "Tổng thời gian:", value = "99:99:99")
        TimeRowItem(label = "Thời gian còn lại:", value = "99:99:99", isAccent = true)
        TimeRowItem(label = "Sử dụng:", value = "00:21:00")
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { 0.25f },
            modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(50)),
            color = AccentColor,
            trackColor = Color.Gray.copy(alpha = 0.3f),
            strokeCap = StrokeCap.Round,
        )
    }
}

@Composable
private fun CollapsedContent() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Còn lại",
            fontSize = 12.sp,
            color = TextSecondary
        )
        Text(
            text = "01:29:55",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AccentColor
        )
    }
}

@Composable
private fun TimeRowItem(label: String, value: String, isAccent: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isAccent) AccentColor else TextPrimary
        )
    }
}
