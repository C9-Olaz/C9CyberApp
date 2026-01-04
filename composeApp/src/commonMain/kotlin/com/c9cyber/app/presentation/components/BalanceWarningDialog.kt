package com.c9cyber.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.DestructiveColor
import com.c9cyber.app.presentation.theme.TextPrimary

@Composable
fun BalanceWarningDialog(
    isLowBalance: Boolean,
    remainingTimeMinutes: Long = 0,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = onDismiss
) {
    // Nếu không phải low balance (tức là hết tiền), không cho phép đóng dialog
    val canDismiss = isLowBalance
    
    Dialog(
        onDismissRequest = { if (canDismiss) onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = canDismiss,
            dismissOnClickOutside = canDismiss
        )
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = BackgroundSecondary,
            modifier = Modifier.width(450.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isLowBalance) "Cảnh báo tài khoản thấp" else "Không đủ số dư",
                    color = if (isLowBalance) AccentColor else DestructiveColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (isLowBalance) {
                    Text(
                        text = "Tài khoản của bạn chỉ còn đủ chơi khoảng ${remainingTimeMinutes} phút.\nVui lòng nạp thêm để tiếp tục chơi.",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentColor
                        )
                    ) {
                        Text(
                            text = "Đã hiểu",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                } else {
                    Text(
                        text = "Số dư của bạn không đủ để sử dụng dịch vụ.",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Vui lòng cầm thẻ ra chủ quán để nạp thêm tiền.",
                        color = AccentColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Icon hoặc visual indicator
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AccentColor.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "💳",
                                fontSize = 48.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

