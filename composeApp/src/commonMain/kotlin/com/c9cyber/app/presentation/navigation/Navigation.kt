package com.c9cyber.app.presentation.navigation

import com.c9cyber.app.domain.model.Game

// Định nghĩa các màn hình có thể có trong ứng dụng
// Đặt ở commonMain để tất cả các module đều có thể truy cập
sealed class Screen {
    data object Home : Screen()
    data object Service : Screen()
    data object Settings : Screen()
    data class GameDetail(val game: Game) : Screen()
    data class GamePlay(val game: Game) : Screen()
}
