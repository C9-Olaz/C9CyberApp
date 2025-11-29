package com.c9cyber.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.presentation.theme.*
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.avatar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserProfileCard(isExpanded: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundSecondary),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.avatar),
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(if (isExpanded) 64.dp else 48.dp)
                    .clip(CircleShape)
                    .border(2.dp, AccentColor, CircleShape)
            )


            AnimatedVisibility(visible = isExpanded) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "Tên",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Số dư: ###.###.###VND",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ID",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Expanded")
@Composable
private fun UserProfileCardExpandedPreview() {
    MaterialTheme(typography = AppTypography) {
        Surface(color = BackgroundPrimary, modifier = Modifier.width(300.dp)) {
            UserProfileCard(isExpanded = true)
        }
    }
}

@Preview(name = "Collapsed")
@Composable
private fun UserProfileCardCollapsedPreview() {
    MaterialTheme(typography = AppTypography) {
        Surface(color = BackgroundPrimary, modifier = Modifier.width(90.dp)) {
            UserProfileCard(isExpanded = false)
        }
    }
}
