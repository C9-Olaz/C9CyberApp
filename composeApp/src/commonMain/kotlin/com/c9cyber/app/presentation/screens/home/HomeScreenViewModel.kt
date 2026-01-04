package com.c9cyber.app.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.c9cyber.app.domain.gametime.GameTimeService
import com.c9cyber.app.domain.model.User
import com.c9cyber.app.domain.smartcard.SmartCardManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class HomeUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeScreenViewModel(
    private val smartCardManager: SmartCardManager
) {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private val viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val gameTimeService = GameTimeService(smartCardManager)
    private var hasChargedOnLogin = false // Đánh dấu đã trừ tiền khi đăng nhập

    init {
        // Listen to game time state changes
        viewModelScope.launch {
            gameTimeService.gameTimeState.collect { gameState ->
                // Update user balance and totalUsableTime when it changes
                val currentUser = uiState.user
                if (currentUser != null) {
                    // Cập nhật totalUsableTime (tổng thời gian đã chơi tính bằng giây)
                    // Lưu ý: totalUsableTime là tổng tích lũy, không phải chỉ session hiện tại
                    val newTotalUsableTime = if (gameState.playTimeSeconds > 0) {
                        // Cập nhật với thời gian chơi hiện tại (seconds)
                        gameState.playTimeSeconds
                    } else {
                        currentUser.totalUsableTime
                    }
                    
                    uiState = uiState.copy(
                        user = currentUser.copy(
                            balance = gameState.balance,
                            totalUsableTime = newTotalUsableTime
                        )
                    )
                }
            }
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            // Load balance từ card (tiền đã được trừ khi đăng nhập)
            val rawBalance = smartCardManager.getBalance() ?: 0
            val balance = rawBalance * 1000
            
            val user = smartCardManager.loadUserInfo()
            val updatedUser = user?.copy(balance = balance) ?: user
            
            uiState = uiState.copy(user = updatedUser)
            
            if (updatedUser != null) {
                // Update game time service với balance mới
                gameTimeService.updateBalance(balance)
                
                // Không tự động bắt đầu game ở đây vì đã bắt đầu khi đăng nhập
                // Chỉ đảm bảo game time service có balance đúng
            }
        }
    }
    
    fun refreshBalance() {
        viewModelScope.launch {
            // Chỉ refresh balance, không trừ tiền lại
            val rawBalance = smartCardManager.getBalance() ?: 0
            val updatedBalance = rawBalance * 1000
            
            val currentUser = uiState.user
            if (currentUser != null) {
                uiState = uiState.copy(
                    user = currentUser.copy(balance = updatedBalance)
                )
                gameTimeService.updateBalance(updatedBalance)
            }
        }
    }

    fun startGame() {
        val balance = uiState.user?.balance ?: 0
        if (balance >= GameTimeService.CHARGE_AMOUNT_VND) {
            gameTimeService.startGame(balance)
        }
    }

    fun stopGame() {
        gameTimeService.stopGame()
    }

    fun onCleanup() {
        // Cập nhật thời gian sử dụng cuối cùng trước khi cleanup
        val finalPlayTime = gameTimeService.gameTimeState.value.playTimeSeconds
        val currentUser = uiState.user
        if (finalPlayTime > 0 && currentUser != null) {
            val updatedUser = currentUser.copy(
                totalUsableTime = finalPlayTime // Already in seconds
            )
            uiState = uiState.copy(user = updatedUser)
        }
        gameTimeService.cleanup()
    }
}
