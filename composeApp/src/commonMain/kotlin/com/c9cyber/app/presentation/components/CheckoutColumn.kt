package com.c9cyber.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextPrimary
import com.c9cyber.app.presentation.theme.TextSecondary

@Composable
fun CheckoutColumn(
    modifier: Modifier = Modifier,
    onShowPinDialog: () -> Unit // 1. Thêm tham số lambda
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

        Column(modifier = Modifier.weight(1f)) {
            CheckoutItemCard()
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Divider(color = AccentColor.copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            SummaryRow(label = "Tổng:", value = "999.999.999 VND")
            SummaryRow(label = "Ưu đãi:", value = "10%")
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Thành tiền", color = TextSecondary, fontSize = 16.sp)
            Text(
                text = "999.999.999 VND",
                color = AccentColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onShowPinDialog, // 2. Gọi lambda khi nhấn nút
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, AccentColor, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Thanh toán",
                    color = AccentColor,
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
