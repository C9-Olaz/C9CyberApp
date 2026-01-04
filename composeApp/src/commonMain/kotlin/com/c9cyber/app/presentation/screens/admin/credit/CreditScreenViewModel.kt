package com.c9cyber.app.presentation.screens.admin.credit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.c9cyber.admin.domain.AdminSmartCardManager
import com.c9cyber.app.utils.formatCurrency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CreditUiState(
    val amount: String = "",
    val isLoading: Boolean = false,
    val showDialog: Boolean = false,
    val dialogTitle: String = "",
    val dialogMessage: String = "",
    val isSuccess: Boolean = false
)

class CreditScreenViewModel(private val manager: AdminSmartCardManager) {
    var uiState by mutableStateOf(CreditUiState())
        private set

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun onAmountChange(value: String) {
        // Only allow numeric input
        val filtered = value.filter { it.isDigit() }
        uiState = uiState.copy(amount = filtered)
    }

    fun onDismissDialog() {
        uiState = uiState.copy(showDialog = false)
    }

    fun onCreditClicked() {
        val amountStr = uiState.amount.trim()
        
        if (amountStr.isEmpty()) {
            uiState = uiState.copy(
                showDialog = true,
                dialogTitle = "Lỗi",
                dialogMessage = "Vui lòng nhập số tiền.",
                isSuccess = false
            )
            return
        }

        val amount = amountStr.toIntOrNull()
        if (amount == null || amount <= 0) {
            uiState = uiState.copy(
                showDialog = true,
                dialogTitle = "Lỗi",
                dialogMessage = "Số tiền không hợp lệ.",
                isSuccess = false
            )
            return
        }

        // Convert to card units: 1 card unit = 1000, so divide by 1000
        val cardAmount = (amount / 1000).toInt()
        
        if (cardAmount <= 0) {
            uiState = uiState.copy(
                showDialog = true,
                dialogTitle = "Lỗi",
                dialogMessage = "Số tiền quá nhỏ (tối thiểu 1000).",
                isSuccess = false
            )
            return
        }

        if (cardAmount > Short.MAX_VALUE) {
            uiState = uiState.copy(
                showDialog = true,
                dialogTitle = "Lỗi",
                dialogMessage = "Số tiền quá lớn (tối đa ${Short.MAX_VALUE * 1000}).",
                isSuccess = false
            )
            return
        }

        scope.launch {
            updateState { it.copy(isLoading = true) }

            // Blocking call on IO thread
            val success = manager.creditBalance(cardAmount.toShort())

            updateState { state ->
                if (success) {
                    state.copy(
                        isLoading = false,
                        amount = "",
                        dialogTitle = "Thành công",
                        dialogMessage = "Đã nạp ${formatCurrency(amount)} VND vào thẻ.",
                        isSuccess = true,
                        showDialog = true
                    )
                } else {
                    state.copy(
                        isLoading = false,
                        dialogTitle = "Lỗi",
                        dialogMessage = "Không thể nạp tiền. Vui lòng kiểm tra kết nối thẻ.",
                        isSuccess = false,
                        showDialog = true
                    )
                }
            }
        }
    }

    private suspend fun updateState(update: (CreditUiState) -> CreditUiState) {
        withContext(Dispatchers.Main) {
            uiState = update(uiState)
        }
    }
}

