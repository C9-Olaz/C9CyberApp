package com.c9cyber.app.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun ServiceItemCard(
    itemName: String,
    price: String,
    modifier: Modifier = Modifier
) {
    var quantity by remember { mutableStateOf(0) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundSecondary)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(Res.drawable.food_placeholder),
                contentDescription = itemName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = itemName, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text(text = price, color = AccentColor, fontWeight = FontWeight.SemiBold)

                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(onClick = { if (quantity > 0) quantity-- }) {
                        Icon(
                            painter = painterResource(Res.drawable.minus_circle),
                            contentDescription = "Decrease quantity",
                            tint = TextSecondary // Thay đổi màu ở đây
                        )
                    }
                    Text(text = "$quantity", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { quantity++ }) {
                        Icon(
                            painter = painterResource(Res.drawable.plus_circle),
                            contentDescription = "Increase quantity",
                            tint = TextSecondary // Thay đổi màu ở đây
                        )
                    }
                }
            }
        }
    }
}
