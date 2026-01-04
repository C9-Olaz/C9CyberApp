package com.c9cyber.app.domain.gametime

import com.c9cyber.app.domain.smartcard.SmartCardManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Service quản lý thời gian chơi và trừ tiền tự động
 * - Cứ 10 phút trừ 2000 VND
 * - Cảnh báo khi tài khoản thấp (< 40 phút)
 * - Dừng khi hết tiền
 */
class GameTimeService(
    private val smartCardManager: SmartCardManager
) {
    companion object {
        const val CHARGE_INTERVAL_MINUTES = 10L // Cứ 10 phút trừ tiền
        const val CHARGE_AMOUNT_VND = 2000 // Mỗi lần trừ 2000 VND
        const val LOW_BALANCE_WARNING_MINUTES = 40L // Cảnh báo khi còn < 40 phút
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var gameTimeJob: Job? = null
    private var timeUpdateJob: Job? = null // Job để cập nhật thời gian real-time
    private var isPlaying = false
    private var currentBalance: Int = 0
    private var startTime: Long = 0 // Thời điểm bắt đầu chơi (milliseconds)
    private var hasShownLowBalanceWarning = false // Đánh dấu đã hiển thị cảnh báo 40 phút
    private var paidTimeSeconds: Long = 0 // Thời gian đã thanh toán (giây)

    private val _gameTimeState = MutableStateFlow(GameTimeState())
    val gameTimeState = _gameTimeState.asStateFlow()

    data class GameTimeState(
        val isPlaying: Boolean = false,
        val playTimeSeconds: Long = 0,
        val balance: Int = 0,
        val remainingTimeSeconds: Long = 0,
        val showLowBalanceWarning: Boolean = false,
        val showInsufficientFunds: Boolean = false
    )

    /**
     * Bắt đầu tính giờ chơi (tiền đã được trừ khi đăng nhập)
     */
    fun startGame(initialBalance: Int) {
        if (isPlaying) return
        
        // Lưu ý: initialBalance là số dư SAU KHI đã trừ tiền cho 10 phút đầu tiên
        // Nên dù balance = 0 vẫn có thể chơi được 10 phút
        
        currentBalance = initialBalance
        isPlaying = true
        startTime = System.currentTimeMillis()
        hasShownLowBalanceWarning = false // Reset flag khi bắt đầu mới
        paidTimeSeconds = CHARGE_INTERVAL_MINUTES * 60
        
        val remainingTime = calculateRemainingTimeSeconds(currentBalance, 0)
        val remainingMinutes = remainingTime / 60
        val showLowBalance = remainingMinutes <= LOW_BALANCE_WARNING_MINUTES && !hasShownLowBalanceWarning
        
        _gameTimeState.value = GameTimeState(
            isPlaying = true,
            playTimeSeconds = 0,
            balance = currentBalance,
            remainingTimeSeconds = remainingTime,
            showLowBalanceWarning = showLowBalance
        )
        
        // Bắt đầu cập nhật thời gian real-time
        startTimeUpdate()

        // Bắt đầu timer để trừ tiền định kỳ
        // Hủy job cũ nếu có để tránh nhiều timer chạy cùng lúc
        gameTimeJob?.cancel()
        gameTimeJob = scope.launch {
            val chargeInterval = CHARGE_INTERVAL_MINUTES * 60 * 1000L // Convert to milliseconds

            while (isPlaying) {
                delay(chargeInterval) // Đợi đúng 10 phút
                
                // Kiểm tra lại isPlaying sau khi delay
                if (!isPlaying) break
                
                // Trừ tiền từ thẻ cho 10 phút TIẾP THEO
                val nextCardAmount = (CHARGE_AMOUNT_VND / 1000).toInt()
                
                // Kiểm tra xem có đủ tiền để trừ không
                if (currentBalance >= CHARGE_AMOUNT_VND) {
                    if (nextCardAmount > 0) {
                        val debitSuccess = smartCardManager.debitBalance(nextCardAmount.toShort())
                        
                        if (debitSuccess) {
                            // Cập nhật balance từ thẻ để đảm bảo đồng bộ
                            val updatedRawBalance = smartCardManager.getBalance() ?: 0
                            currentBalance = updatedRawBalance * 1000
                            paidTimeSeconds += CHARGE_INTERVAL_MINUTES * 60
                            
                            // playTimeSeconds is updated in startTimeUpdate loop
                            // We need to get current playTimeSeconds to calculate remaining
                            val currentPlayTimeSeconds = _gameTimeState.value.playTimeSeconds
                            val remainingTime = calculateRemainingTimeSeconds(currentBalance, currentPlayTimeSeconds)
                            val remainingMinutes = remainingTime / 60
                            
                            // Chỉ hiển thị cảnh báo một lần khi đạt ngưỡng 40 phút
                            val showLowBalance = if (!hasShownLowBalanceWarning && remainingMinutes <= LOW_BALANCE_WARNING_MINUTES && remainingTime > 0) {
                                hasShownLowBalanceWarning = true
                                true
                            } else {
                                false
                            }
                            
                            _gameTimeState.update {
                                it.copy(
                                    balance = currentBalance,
                                    remainingTimeSeconds = remainingTime,
                                    showLowBalanceWarning = showLowBalance,
                                    showInsufficientFunds = false
                                )
                            }
                        } else {
                            // Nếu không trừ được tiền (có thể do lỗi), dừng game
                            stopGame()
                            _gameTimeState.update { it.copy(showInsufficientFunds = true, isPlaying = false) }
                            break
                        }
                    }
                } else {
                    // Không đủ tiền để gia hạn thêm 10 phút nữa
                    // Dừng game
                    stopGame()
                    _gameTimeState.update { it.copy(showInsufficientFunds = true, isPlaying = false) }
                    break
                }
            }
        }
    }

    /**
     * Cập nhật thời gian real-time (mỗi giây)
     */
    private fun startTimeUpdate() {
        timeUpdateJob?.cancel()
        timeUpdateJob = scope.launch {
            while (isPlaying) {
                delay(1000) // Cập nhật mỗi giây
                
                if (!isPlaying) break
                
                // Tính thời gian đã chơi từ startTime
                val elapsedMillis = System.currentTimeMillis() - startTime
                val elapsedSeconds = elapsedMillis / 1000
                
                // Tính thời gian còn lại
                val remainingTime = calculateRemainingTimeSeconds(currentBalance, elapsedSeconds)
                val remainingMinutes = remainingTime / 60
                
                // Kiểm tra cảnh báo 40 phút (chỉ một lần)
                val showLowBalance = if (!hasShownLowBalanceWarning && remainingMinutes <= LOW_BALANCE_WARNING_MINUTES && remainingTime > 0) {
                    hasShownLowBalanceWarning = true
                    true
                } else {
                    false
                }
                
                _gameTimeState.update {
                    it.copy(
                        playTimeSeconds = elapsedSeconds,
                        remainingTimeSeconds = remainingTime,
                        showLowBalanceWarning = showLowBalance
                    )
                }
                
                // Tự động dừng khi hết thời gian (remainingTime <= 0)
                if (remainingTime <= 0) {
                     stopGame()
                    _gameTimeState.update { it.copy(showInsufficientFunds = true, isPlaying = false) }
                    break
                }
            }
        }
    }
    
    /**
     * Dừng chơi game
     */
    fun stopGame() {
        isPlaying = false
        gameTimeJob?.cancel()
        gameTimeJob = null
        timeUpdateJob?.cancel()
        timeUpdateJob = null
        
        _gameTimeState.update { it.copy(isPlaying = false) }
    }

    /**
     * Cập nhật balance (khi nạp thêm tiền)
     */
    fun updateBalance(newBalance: Int) {
        currentBalance = newBalance
        val currentPlayTimeSeconds = _gameTimeState.value.playTimeSeconds
        val remainingTime = calculateRemainingTimeSeconds(newBalance, currentPlayTimeSeconds)
        val remainingMinutes = remainingTime / 60
        
        // Chỉ hiển thị cảnh báo một lần
        val showLowBalance = if (!hasShownLowBalanceWarning && remainingMinutes <= LOW_BALANCE_WARNING_MINUTES && remainingTime > 0 && isPlaying) {
            hasShownLowBalanceWarning = true
            true
        } else {
            false
        }
        
        _gameTimeState.update {
            it.copy(
                balance = newBalance,
                remainingTimeSeconds = remainingTime,
                showLowBalanceWarning = showLowBalance,
                showInsufficientFunds = false
            )
        }
    }

    /**
     * Tính thời gian còn lại dựa trên số dư và thời gian đã chơi
     */
    private fun calculateRemainingTimeSeconds(balance: Int, playTimeSeconds: Long): Long {
        val remainingFromBalance = (balance / CHARGE_AMOUNT_VND).toLong() * CHARGE_INTERVAL_MINUTES * 60
        val remainingFromPaid = paidTimeSeconds - playTimeSeconds
        return maxOf(0, remainingFromPaid + remainingFromBalance)
    }

    /**
     * Dismiss cảnh báo
     * Flag hasShownLowBalanceWarning vẫn được giữ để không hiển thị lại
     */
    fun dismissLowBalanceWarning() {
        _gameTimeState.update { it.copy(showLowBalanceWarning = false) }
        // Không reset hasShownLowBalanceWarning để đảm bảo chỉ hiển thị một lần
    }

    fun dismissInsufficientFunds() {
        _gameTimeState.update { it.copy(showInsufficientFunds = false) }
    }

    fun cleanup() {
        stopGame()
        scope.cancel()
    }
}
