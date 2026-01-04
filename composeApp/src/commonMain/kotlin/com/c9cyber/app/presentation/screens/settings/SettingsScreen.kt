package com.c9cyber.app.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.domain.model.Transaction
import com.c9cyber.app.presentation.components.ChangePinForm
import com.c9cyber.app.presentation.components.PinDialog
import com.c9cyber.app.presentation.components.UserInfoForm
import com.c9cyber.app.presentation.navigation.Screen
import com.c9cyber.app.presentation.theme.*
import com.c9cyber.app.utils.formatCurrency
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    viewModel: SettingScreenViewModel,
    navigateTo: (Screen) -> Unit,
    onCardLocked: () -> Unit
) {
    val state = viewModel.uiState
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Thông tin cá nhân", "Thay đổi mã PIN", "Lịch sử giao dịch")

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            delay(3000)
            viewModel.dismissSuccessMessage()
        }
    }

    LaunchedEffect(state.isCardLocked) {
        if (state.isCardLocked) {
            onCardLocked() // Gọi callback để thoát ra main
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserInfoFromCard()
    }

    // Load transaction history when tab is selected
    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 2) {
            viewModel.loadTransactionHistory()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundPrimary)
            .padding(24.dp)
    ) {
        IconButton(
            onClick = {
                navigateTo(Screen.Home)
                viewModel.resetState()
            },
            modifier = Modifier.align(Alignment.TopStart).size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = AccentColor,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "THÔNG TIN",
                color = AccentColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = AccentColor,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = AccentColor
                    )
                },
                modifier = Modifier.width(600.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) AccentColor else Color.Gray
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = BackgroundSecondary),
                modifier = Modifier.width(600.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (selectedTabIndex) {
                        0 -> {
                            UserInfoForm(
                                state = state,
                                onFullNameChange = viewModel::onFullNameChange,
                                onUserNameChange = viewModel::onUsernameChange,
                                onAvatarChange = viewModel::onAvatarChange,
                                onEditClicked = viewModel::onEditClicked,
                                onCancelEditClicked = viewModel::onCancelEditClicked,
                                onSaveInfoClicked = viewModel::onSaveInfoClicked
                            )
                        }
                        1 -> {
                            ChangePinForm(
                                state = state,
                                onOldPinChange = viewModel::onOldPinChange,
                                onNewPinChange = viewModel::onNewPinChange,
                                onConfirmNewPinChange = viewModel::onConfirmPinChange,
                                onChangePinClicked = viewModel::onChangePinClicked
                            )
                        }
                        2 -> {
                            if (state.isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(300.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = AccentColor)
                                }
                            } else if (state.transactions.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(300.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Chưa có giao dịch nào",
                                        color = TextSecondary,
                                        fontSize = 16.sp
                                    )
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth().height(400.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(state.transactions) { transaction ->
                                        TransactionItem(transaction)
                                        Divider(color = Color.Gray.copy(alpha = 0.2f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.successMessage != null) {
            Surface(
                color = AccentColor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
            ) {
                Text(
                    text = state.successMessage,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (state.errorMessage != null && !state.showPinDialog) {
            Surface(
                color = DestructiveColor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
            ) {
                Text(
                    text = state.errorMessage,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (state.showPinDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                PinDialog(
                    errorMessage = state.errorMessage,
                    isLoading = state.isLoading,
                    onDismissRequest = { viewModel.onPinDismiss() },
                    onConfirm = { pin -> viewModel.updateInformation(pin) }
                )
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (transaction.type == 1.toByte()) "Nạp tiền" else "Thanh toán",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        
        Text(
            text = "${if (transaction.type == 1.toByte()) "+" else "-"}${formatCurrency(transaction.amount.toInt() * 1000)} VND",
            color = if (transaction.type == 1.toByte()) AccentColor else DestructiveColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = if (enabled) AccentColor else Color.Gray) },
        singleLine = true,
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentColor,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.DarkGray,
            focusedLabelColor = AccentColor,
            unfocusedLabelColor = Color.Gray,
            disabledLabelColor = Color.Gray,
            cursorColor = AccentColor,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledTextColor = Color.LightGray
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
