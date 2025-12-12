package com.c9cyber.app.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.c9cyber.app.domain.model.User
import com.c9cyber.app.domain.smartcard.SmartCardManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    fun loadUserInfo() {
        viewModelScope.launch {
            val user = smartCardManager.loadUserInfo()
            uiState = uiState.copy(user = user)
        }
    }

}