package com.c9cyber.app.presentation.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c9cyber.app.domain.game.GameLauncher
import com.c9cyber.app.domain.gametime.GameTimeService
import com.c9cyber.app.domain.model.Game
import com.c9cyber.app.domain.model.GameType
import com.c9cyber.app.presentation.components.BalanceWarningDialog
import com.c9cyber.app.presentation.theme.*
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun GameDetailScreen(
    game: Game,
    gameTimeService: GameTimeService,
    currentBalance: Int,
    onBack: () -> Unit,
    onStartGame: () -> Unit
) {
    val gameTimeState by gameTimeService.gameTimeState.collectAsState()
    val gameLauncher = remember { GameLauncher() }
    var launchError by remember { mutableStateOf<String?>(null) }

    // Dừng game khi unmount
    DisposableEffect(Unit) {
        onDispose {
            // Không dừng game ở đây vì có thể user chỉ navigate, game vẫn tiếp tục chạy
        }
    }

    // Kiểm tra nếu hết tiền, tự động quay về
    LaunchedEffect(gameTimeState.showInsufficientFunds) {
        if (gameTimeState.showInsufficientFunds) {
            gameTimeService.stopGame()
            onBack()
        }
    }

    // Show warning dialogs
    if (gameTimeState.showLowBalanceWarning) {
        BalanceWarningDialog(
            isLowBalance = true,
            remainingTimeMinutes = gameTimeState.remainingTimeSeconds / 60,
            onDismiss = gameTimeService::dismissLowBalanceWarning
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundPrimary)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header với nút back
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = AccentColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Chi tiết game",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Game Image
                Image(
                    painter = painterResource(game.imageRes ?: Res.drawable.game_placeholder),
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                // Game Info
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = game.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (game.type == GameType.Online) AccentColor else TextSecondary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = if (game.type == GameType.Online) "Online" else "Offline",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Divider(color = AccentColor.copy(alpha = 0.3f), thickness = 1.dp)

                    Text(
                        text = "Mô tả",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Trải nghiệm ${game.name} với đồ họa tuyệt đẹp và gameplay hấp dẫn. Game ${if (game.type == GameType.Online) "yêu cầu kết nối internet" else "có thể chơi offline"}.",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                    
                    // Error message
                    if (launchError != null) {
                        Text(
                            text = launchError!!,
                            color = DestructiveColor,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Play Button
                Button(
                    onClick = {
                        if (gameTimeState.isPlaying) {
                            val success = gameLauncher.launchGame(game)
                            if (success) {
                                onStartGame() // Navigate to GamePlayScreen
                            } else {
                                launchError = "Không thể khởi chạy game. Vui lòng kiểm tra đường dẫn."
                            }
                        }
                    },
                    enabled = gameTimeState.isPlaying || currentBalance >= 2000,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentColor,
                        disabledContainerColor = TextSecondary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (gameTimeState.isPlaying) {
                        Text(
                            text = "Mở Game",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (currentBalance < 2000) {
                        Text(
                            text = "Số dư không đủ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Bắt đầu chơi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
