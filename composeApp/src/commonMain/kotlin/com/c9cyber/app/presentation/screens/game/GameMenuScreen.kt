package com.c9cyber.app.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.c9cyber.app.presentation.components.GameCard
import com.c9cyber.app.presentation.components.GameCategoryList
import com.c9cyber.app.presentation.components.SearchBar
import com.c9cyber.app.presentation.theme.BackgroundSecondary
import com.c9cyber.app.presentation.theme.TextSecondary

@Composable
fun GameMenuScreen(
    onGameClick: (com.c9cyber.app.domain.model.Game) -> Unit
) {
    val viewModel = remember { GameMenuViewModel() }
    val state by viewModel.uiState.collectAsState()

    val filteredGames = remember(state.games, state.selectedCategory, state.searchQuery) {
        viewModel.getFilteredGames()
    }

    Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        // --- CỘT TRÁI: DANH MỤC GAME ---
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundSecondary)
                .padding(16.dp)
        ) {
            GameCategoryList(
                selectedCategory = state.selectedCategory,
                onCategorySelected = viewModel::setSelectedCategory
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        // --- CỘT PHẢI: LƯỚI GAME ---
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                value = state.searchQuery,
                onValueChange = viewModel::setSearchQuery
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Đang tải...",
                        color = TextSecondary
                    )
                }
            } else if (filteredGames.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Không tìm thấy game nào",
                        color = TextSecondary
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredGames) { game ->
                        GameCard(
                            game = game,
                            onClick = { onGameClick(game) }
                        )
                    }
                }
            }
        }
    }
}
