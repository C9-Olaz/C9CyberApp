package com.c9cyber.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.domain.model.ServiceItem
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary
import com.c9cyber.app.utils.formatCurrency
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun ServiceItemCard(
    item: ServiceItem,
    quantity: Int = 0,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().height(160.dp), // Set a fixed height for consistent card size
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundSecondary),
//        enabled = item.isAvailable
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Use SpaceBetween to push content apart
        ) {
            // Item Info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.name,
                    color = if (item.isAvailable) TextPrimary else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${formatCurrency(item.price)} VND",
                    color = AccentColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            // Controls
            if (!item.isAvailable) {
                Text(
                    text = "Hết hàng",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            } else {
                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = onRemove,
                        enabled = quantity > 0
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.minus_circle),
                            contentDescription = "Decrease quantity",
                            tint = if (quantity > 0) TextSecondary else TextSecondary.copy(alpha = 0.5f)
                        )
                    }
                    Text(
                        text = "$quantity",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onAdd) {
                        Icon(
                            painter = painterResource(Res.drawable.plus_circle),
                            contentDescription = "Increase quantity",
                            tint = TextSecondary
                        )
                    }
                }
            }
        }
    }
}
