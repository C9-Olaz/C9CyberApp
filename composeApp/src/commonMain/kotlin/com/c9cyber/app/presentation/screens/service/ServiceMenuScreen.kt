package com.c9cyber.app.presentation.screens.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.c9cyber.app.data.repository.MenuRepository
import com.c9cyber.app.data.repository.ServiceRepository
import com.c9cyber.app.domain.model.ServiceType
import com.c9cyber.app.domain.smartcard.SmartCardManager
import com.c9cyber.app.presentation.components.*
import com.c9cyber.app.presentation.navigation.Screen
import com.c9cyber.app.presentation.theme.BackgroundPrimary
import com.c9cyber.app.presentation.theme.BackgroundSecondary

@Composable
fun ServiceMenuScreen(
    smartCardManager: SmartCardManager,
    serviceRepository: ServiceRepository? = null,
    menuRepository: MenuRepository? = null,
    navigateTo: (Screen) -> Unit
) {
    val viewModel = remember { ServiceMenuViewModel(smartCardManager, serviceRepository, menuRepository) }
    val state by viewModel.uiState.collectAsState()

    var selectedCategory by remember { mutableStateOf<ServiceType?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter items based on category and search
    val filteredItems = remember(state.serviceItems, selectedCategory, searchQuery) {
        state.serviceItems.filter { item ->
            val matchesCategory = selectedCategory == null || item.type == selectedCategory
            val matchesSearch = searchQuery.isBlank() || 
                item.name.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    // Get cart quantity for each item
    fun getCartQuantity(itemId: String): Int {
        return state.cartItems.find { it.serviceItem.id == itemId }?.quantity ?: 0
    }

    LaunchedEffect(state.isPaymentSuccess) {
        if (state.isPaymentSuccess) {
            viewModel.resetPaymentSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize().background(BackgroundPrimary).padding(24.dp)) {
            // --- CỘT 1: DANH MỤC ---
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(BackgroundSecondary)
                    .padding(16.dp)
            ) {
                ServiceCategoryList(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        selectedCategory = when (category) {
                            "Đồ ăn" -> ServiceType.Food
                            "Đồ uống" -> ServiceType.Drink
                            else -> null
                        }
                    },
                    onBack = { navigateTo(Screen.Home) }
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // --- CỘT 2: MENU CHÍNH ---
            Column(modifier = Modifier.weight(1f)) {
                SearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                if (filteredItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.material3.Text(
                            text = if (state.serviceItems.isEmpty()) "Đang tải..." else "Không tìm thấy món nào",
                            color = com.c9cyber.app.presentation.theme.TextSecondary
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredItems) { item ->
                            ServiceItemCard(
                                item = item,
                                quantity = getCartQuantity(item.id),
                                onAdd = { viewModel.addToCart(item) },
                                onRemove = { viewModel.removeFromCart(item) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // --- CỘT 3: CHECKOUT ---
            CheckoutColumn(
                cartItems = state.cartItems,
                total = viewModel.calculateTotal(),
                onShowPinDialog = viewModel::showPinDialog,
                onIncreaseItem = { itemId ->
                    val item = state.serviceItems.find { it.id == itemId }
                    item?.let { viewModel.addToCart(it) }
                },
                onDecreaseItem = { itemId ->
                    val item = state.serviceItems.find { it.id == itemId }
                    item?.let { viewModel.removeFromCart(it) }
                },
                onRemoveItem = { itemId ->
                    val item = state.serviceItems.find { it.id == itemId }
                    item?.let { viewModel.removeEntireItem(it) }
                }
            )
        }

        if (state.showPinDialog) {
            PinDialog(
                errorMessage = state.pinError,
                isLoading = state.isLoading,
                onDismissRequest = viewModel::hidePinDialog,
                onConfirm = { pin ->
                    viewModel.processPayment(pin)
                }
            )
        }
    }
}
