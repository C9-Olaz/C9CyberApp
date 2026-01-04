package com.c9cyber.app.presentation.screens.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.c9cyber.app.data.repository.MenuRepository
import com.c9cyber.app.data.repository.ServiceRepository
import com.c9cyber.app.domain.model.CartItem
import com.c9cyber.app.domain.model.ServiceItem
import com.c9cyber.app.domain.model.ServiceType
import com.c9cyber.app.domain.smartcard.PinVerifyResult
import com.c9cyber.app.domain.smartcard.SmartCardManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ServiceMenuUiState(
    val serviceItems: List<ServiceItem> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showPinDialog: Boolean = false,
    val pinError: String? = null,
    val isPaymentSuccess: Boolean = false
)

class ServiceMenuViewModel(
    private val smartCardManager: SmartCardManager,
    private val serviceRepository: ServiceRepository? = null,
    private val menuRepository: MenuRepository? = null
) {
    private val _uiState = MutableStateFlow(ServiceMenuUiState())
    val uiState = _uiState.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        loadServiceItems()
    }

    private fun loadServiceItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            if (menuRepository != null) {
                val result = menuRepository.getMenuItems()
                result.fold(
                    onSuccess = { menuItems ->
                        val serviceItems = menuItems.map { menuItem ->
                            ServiceItem(
                                id = menuItem.id.toString(),
                                name = menuItem.name,
                                price = menuItem.price.toInt(),
                                type = if (menuItem.category_name.contains("Food", ignoreCase = true) || menuItem.category_name.contains("Đồ ăn", ignoreCase = true)) ServiceType.Food else ServiceType.Drink,
                                isAvailable = menuItem.is_available
                            )
                        }
                        _uiState.update {
                            it.copy(
                                serviceItems = serviceItems,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                        return@launch
                    },
                    onFailure = {
                        println("API failed, falling back to dummy data: ${it.message}")
                    }
                )
            }

            // Fallback to dummy data
            val repository = serviceRepository ?: ServiceRepository()
            val result = repository.getServiceItems()
            
            result.fold(
                onSuccess = { items ->
                    _uiState.update {
                        it.copy(
                            serviceItems = items,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            serviceItems = emptyList(),
                            isLoading = false,
                            errorMessage = "Không thể tải danh sách món. ${error.message}"
                        )
                    }
                }
            )
        }
    }

    fun addToCart(item: ServiceItem) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        val existingItemIndex = currentCart.indexOfFirst { it.serviceItem.id == item.id }

        if (existingItemIndex != -1) {
            val existingItem = currentCart[existingItemIndex]
            currentCart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentCart.add(CartItem(item, 1))
        }
        _uiState.update { it.copy(cartItems = currentCart) }
    }

    fun removeFromCart(item: ServiceItem) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        val existingItemIndex = currentCart.indexOfFirst { it.serviceItem.id == item.id }

        if (existingItemIndex != -1) {
            val existingItem = currentCart[existingItemIndex]
            if (existingItem.quantity > 1) {
                currentCart[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity - 1)
            } else {
                currentCart.removeAt(existingItemIndex)
            }
            _uiState.update { it.copy(cartItems = currentCart) }
        }
    }
    
    fun removeEntireItem(item: ServiceItem) {
        val currentCart = _uiState.value.cartItems.toMutableList()
        currentCart.removeAll { it.serviceItem.id == item.id }
        _uiState.update { it.copy(cartItems = currentCart) }
    }

    fun showPinDialog() {
        if (_uiState.value.cartItems.isNotEmpty()) {
            _uiState.update { it.copy(showPinDialog = true, pinError = null) }
        }
    }

    fun hidePinDialog() {
        _uiState.update { it.copy(showPinDialog = false, pinError = null) }
    }

    fun processPayment(pin: String) {
        _uiState.update { it.copy(isLoading = true, pinError = null) }
        viewModelScope.launch {
            val verifyResult = smartCardManager.verifyPin(pin)
            when (verifyResult) {
                is PinVerifyResult.Success -> {
                    val total = calculateTotal()
                    val cardAmount = (total / 1000).toInt()
                    
                    if (cardAmount > Short.MAX_VALUE) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                pinError = "Tổng tiền quá lớn (tối đa ${Short.MAX_VALUE * 1000})"
                            )
                        }
                        return@launch
                    }
                    
                    val debitSuccess = smartCardManager.debitBalance(cardAmount.toShort())
                    
                    if (debitSuccess) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                showPinDialog = false,
                                cartItems = emptyList(),
                                isPaymentSuccess = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                pinError = "Không thể trừ tiền. Vui lòng kiểm tra số dư thẻ."
                            )
                        }
                    }
                }
                is PinVerifyResult.CardLocked -> {
                    _uiState.update { it.copy(isLoading = false, pinError = "Thẻ đã bị khóa do nhập sai quá nhiều lần") }
                }
                is PinVerifyResult.WrongPin -> {
                    _uiState.update { it.copy(isLoading = false, pinError = "Sai PIN. Còn lại ${verifyResult.remainingTries} lần thử") }
                }
                is PinVerifyResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, pinError = verifyResult.message) }
                }
            }
        }
    }
    
    fun resetPaymentSuccess() {
        _uiState.update { it.copy(isPaymentSuccess = false) }
    }

    fun calculateTotal(): Int {
        return _uiState.value.cartItems.sumOf { it.serviceItem.price * it.quantity }
    }
}
