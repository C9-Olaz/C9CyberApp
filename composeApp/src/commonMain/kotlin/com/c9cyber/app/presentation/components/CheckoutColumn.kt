package com.c9cyber.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.domain.model.CartItem
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary
import com.c9cyber.app.utils.formatCurrency

@Composable
fun CheckoutColumn(
    cartItems: List<CartItem>,
    total: Int,
    onShowPinDialog: () -> Unit,
    onIncreaseItem: (String) -> Unit,
    onDecreaseItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(BackgroundSecondary, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Checkout",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Giỏ hàng trống",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { cartItem ->
                    CheckoutItemCard(
                        cartItem = cartItem,
                        onIncrease = { onIncreaseItem(cartItem.serviceItem.id) },
                        onDecrease = { onDecreaseItem(cartItem.serviceItem.id) },
                        onRemove = { onRemoveItem(cartItem.serviceItem.id) }
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Divider(color = AccentColor.copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            SummaryRow(label = "Tổng:", value = "${formatCurrency(total)} VND")
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Thành tiền", color = TextSecondary, fontSize = 16.sp)
            Text(
                text = "${formatCurrency(total)} VND",
                color = AccentColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onShowPinDialog,
                enabled = cartItems.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, AccentColor, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Thanh toán",
                    color = if (cartItems.isNotEmpty()) AccentColor else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary)
        Text(text = value, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
