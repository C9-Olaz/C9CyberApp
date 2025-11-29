package com.c9cyber.app.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.presentation.theme.DestructiveColor
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun CheckoutItemCard(modifier: Modifier = Modifier) {
    var quantity by remember { mutableStateOf(1) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.food_placeholder),
            contentDescription = "Xien ban",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Xien ban", color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { if (quantity > 0) quantity-- }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(Res.drawable.minus_circle),
                        contentDescription = "Decrease quantity",
                        tint = TextSecondary
                    )
                }
                Text(text = "$quantity", color = TextPrimary, fontWeight = FontWeight.Bold)
                IconButton(onClick = { quantity++ }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(Res.drawable.plus_circle),
                        contentDescription = "Increase quantity",
                        tint = TextSecondary
                    )
                }
            }
        }

        IconButton(onClick = { /* TODO: Remove item */ }) {
            Icon(
                painter = painterResource(Res.drawable.trash),
                contentDescription = "Remove item",
                tint = DestructiveColor
            )
        }
    }
}
