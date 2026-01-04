package com.c9cyber.app.presentation.screens.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.c9cyber.app.data.dummy.DummyGameData
import com.c9cyber.app.domain.model.Game
import com.c9cyber.app.domain.model.GameType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameMenuUiState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: GameType? = null,
    val searchQuery: String = ""
)

class GameMenuViewModel {
    private val _uiState = MutableStateFlow(GameMenuUiState())
    val uiState = _uiState.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Load dummy data directly
            val games = DummyGameData.getDummyGames()
            _uiState.update {
                it.copy(
                    games = games,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun setSelectedCategory(category: GameType?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun getFilteredGames(): List<Game> {
        return _uiState.value.games.filter { game ->
            val matchesCategory = _uiState.value.selectedCategory == null || game.type == _uiState.value.selectedCategory
            val matchesSearch = _uiState.value.searchQuery.isBlank() || 
                game.name.contains(_uiState.value.searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }
}
