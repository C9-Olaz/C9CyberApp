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
fun TimeStatusPanel(
    isExpanded: Boolean,
    remainingTimeMinutes: Long = 0,
    playTimeSeconds: Long = 0,
    isPlaying: Boolean = false,
    totalUsableTimeSeconds: Long = 0
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundSecondary)
    ) {
        if (isExpanded) {
            ExpandedContent(
                remainingTimeMinutes = remainingTimeMinutes,
                playTimeSeconds = playTimeSeconds,
                isPlaying = isPlaying,
                totalUsableTimeSeconds = totalUsableTimeSeconds
            )
        } else {
            CollapsedContent(remainingTimeMinutes = remainingTimeMinutes)
        }
    }
}

@Composable
private fun ExpandedContent(
    remainingTimeMinutes: Long,
    playTimeSeconds: Long,
    isPlaying: Boolean,
    totalUsableTimeSeconds: Long
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimeRowItem(
            label = "Thời gian còn lại:",
            value = formatTime(remainingTimeMinutes),
            isAccent = true
        )
//        TimeRowItem(
//            label = "Sử dụng:",
//            value = formatTimeFromSeconds(playTimeSeconds)
//        )
        TimeRowItem(
            label = "Tổng thời gian sử dụng:",
            value = formatTimeFromSeconds(totalUsableTimeSeconds)
        )
//        if (!isPlaying) {
//            Text(
//                text = "Chưa bắt đầu chơi",
//                fontSize = 12.sp,
//                color = TextSecondary
//            )
//        }
        Spacer(modifier = Modifier.height(12.dp))
        val progress = if (remainingTimeMinutes > 0 && playTimeSeconds > 0) {
            // Convert remainingTimeMinutes to seconds for calculation
            val remainingSeconds = remainingTimeMinutes * 60
            (playTimeSeconds.toFloat() / (playTimeSeconds + remainingSeconds).toFloat()).coerceIn(0f, 1f)
        } else 0f
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(50)),
            color = AccentColor,
            trackColor = Color.Gray.copy(alpha = 0.3f),
            strokeCap = StrokeCap.Round,
        )
    }
}

@Composable
private fun CollapsedContent(remainingTimeMinutes: Long) {
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
            text = formatTime(remainingTimeMinutes),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AccentColor
        )
    }
}

private fun formatTime(minutes: Long): String {
    if (minutes <= 0) return "00:00"
    val hours = minutes / 60
    val mins = minutes % 60
    return String.format("%02d:%02d", hours, mins)
}

private fun formatTimeFromSeconds(seconds: Long): String {
    if (seconds <= 0) return "00:00:00"
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
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
