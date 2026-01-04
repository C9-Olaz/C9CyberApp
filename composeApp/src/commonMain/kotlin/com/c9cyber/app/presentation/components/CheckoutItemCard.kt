package com.c9cyber.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.domain.model.CartItem
import com.c9cyber.app.presentation.theme.DestructiveColor
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary
import com.c9cyber.app.utils.formatCurrency
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun CheckoutItemCard(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartItem.serviceItem.name,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${formatCurrency(cartItem.serviceItem.price)} VND",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.minus_circle),
                        contentDescription = "Decrease quantity",
                        tint = TextSecondary
                    )
                }
                Text(
                    text = "${cartItem.quantity}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.plus_circle),
                        contentDescription = "Increase quantity",
                        tint = TextSecondary
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${formatCurrency(cartItem.serviceItem.price * cartItem.quantity)} VND",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.trash),
                    contentDescription = "Remove item",
                    tint = DestructiveColor
                )
            }
        }
    }
}
