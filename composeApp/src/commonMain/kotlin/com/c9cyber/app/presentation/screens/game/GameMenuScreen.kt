package com.c9cyber.app.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.c9cyber.app.presentation.components.GameCard
import com.c9cyber.app.presentation.components.GameCategoryList
import com.c9cyber.app.presentation.theme.BackgroundSecondary

@Composable
fun GameMenuScreen() {
    val sampleGames = List(9) { "Game A" }

    Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        // --- CỘT TRÁI: DANH MỤC GAME ---
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp)) // Bo góc
                .background(BackgroundSecondary) // Thêm màu nền
                .padding(16.dp) // Thêm padding bên trong
        ) {
            GameCategoryList()
        }

        Spacer(modifier = Modifier.width(24.dp))

        // --- CỘT PHẢI: LƯỚI GAME ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sampleGames) { gameName ->
                GameCard(gameName = gameName)
            }
        }
    }
}
