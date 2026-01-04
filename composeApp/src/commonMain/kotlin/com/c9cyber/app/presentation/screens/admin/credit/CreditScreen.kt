package com.c9cyber.app.presentation.screens.admin.credit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.c9cyber.app.presentation.admincomponents.Button
import com.c9cyber.app.presentation.admincomponents.StatusDialog
import com.c9cyber.app.presentation.components.TextField
import com.c9cyber.app.presentation.theme.AccentColor
import com.c9cyber.app.presentation.theme.TextSecondary

@Composable
fun CreditScreen(
    viewModel: CreditScreenViewModel,
    isReaderReady: Boolean
) {
    val state = viewModel.uiState

    StatusDialog(
        isOpen = state.showDialog,
        title = state.dialogTitle,
        message = state.dialogMessage,
        isSuccess = state.isSuccess,
        onDismiss = viewModel::onDismissDialog
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
                .alpha(if (isReaderReady) 1f else 0.5f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Nạp tiền", color = AccentColor, fontWeight = FontWeight.Bold)
            Text("Nhập số tiền cần nạp vào thẻ (đơn vị: VND).", color = TextSecondary)

            Spacer(Modifier.height(16.dp))

            TextField(
                value = state.amount,
                onValueChange = viewModel::onAmountChange,
                label = "Số tiền (VND)",
                icon = Icons.Default.AccountBalance,
                enabled = isReaderReady
            )

            Spacer(Modifier.height(16.dp))

            Button(
                text = "Nạp tiền",
                onClick = viewModel::onCreditClicked,
                isLoading = state.isLoading,
                enabled = isReaderReady
            )
        }
    }
}

