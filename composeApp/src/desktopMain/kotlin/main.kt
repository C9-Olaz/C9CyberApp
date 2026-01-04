import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.c9cyber.app.data.api.ApiService
import com.c9cyber.app.data.api.createHttpClient
import com.c9cyber.app.data.repository.AuthRepository
import com.c9cyber.app.data.repository.MenuRepository
import com.c9cyber.app.domain.smartcard.SmartCardManager
import com.c9cyber.app.domain.smartcard.SmartCardMonitor
import com.c9cyber.app.domain.smartcard.SmartCardTransportImpl
import com.c9cyber.app.presentation.navigation.Screen
import com.c9cyber.app.presentation.screens.game.GameDetailScreen
import com.c9cyber.app.presentation.screens.home.HomeScreen
import com.c9cyber.app.presentation.screens.home.HomeScreenViewModel
import com.c9cyber.app.presentation.screens.service.ServiceMenuScreen
import com.c9cyber.app.presentation.screens.settings.SettingScreenViewModel
import com.c9cyber.app.presentation.screens.settings.SettingsScreen
import com.c9cyber.app.presentation.screens.standby.StandbyScreenViewModel
import com.c9cyber.app.presentation.screens.standby.StandbyScreens
import com.c9cyber.app.presentation.theme.AppTypography
import com.c9cyber.app.presentation.theme.BackgroundPrimary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() = application {
    var isLoggedIn by remember { mutableStateOf(false) }
    var startBalance by remember { mutableStateOf<Int?>(null) }

    val smartCardTransport = remember { SmartCardTransportImpl() }
    val smartCardMonitor = remember { SmartCardMonitor(smartCardTransport) }

    val smartCardManager = SmartCardManager(
        transport = smartCardTransport,
        monitor = smartCardMonitor
    )

    val httpClient = remember { createHttpClient() }
    val apiHandler = remember { ApiService(httpClient) }
    val authRepository = remember { AuthRepository(apiHandler, smartCardManager) }
    val menuRepository = remember { MenuRepository(apiHandler) }
    val serviceRepository = remember { com.c9cyber.app.data.repository.ServiceRepository(apiHandler) }


    val standbyViewModel = remember(isLoggedIn) {
        StandbyScreenViewModel(smartCardManager, authRepository)
    }
    val homeViewModel = remember(isLoggedIn) {
        HomeScreenViewModel(smartCardManager)
    }

    val settingViewModel = remember(isLoggedIn)
    {
        SettingScreenViewModel(smartCardManager)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        smartCardMonitor.startMonitoring(
            onCardRemoved = {
                isLoggedIn = false
                println("Card Removed")
            },
            onCardInserted = {
                println("Card Inserted")
            }
        )
    }

    if (!isLoggedIn) {
        val standbyWindowState = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(600.dp, 600.dp)
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "C9Cyber - Standby",
            state = standbyWindowState,
            resizable = false,
            alwaysOnTop = true
        ) {
            MaterialTheme(typography = AppTypography) {
                Surface(color = BackgroundPrimary, modifier = Modifier.fillMaxSize()) {
                    StandbyScreens(
                        viewModel = standbyViewModel,
                        onLoginSuccess = {
                            scope.launch {
                                val user = smartCardManager.loadUserInfo()
                                if (user != null && user.balance >= 2000) {
                                    val cardAmount = (2000 / 1000).toInt()
                                    if (cardAmount > 0) {
                                        val debitSuccess = smartCardManager.debitBalance(cardAmount.toShort())
                                        if (debitSuccess) {
                                            val rawBalance = smartCardManager.getBalance() ?: 0
                                            val updatedBalance = rawBalance * 1000
                                            startBalance = updatedBalance
                                        }
                                    }
                                }
                                isLoggedIn = true
                            }
                        }
                    )
                }
            }
        }
    }

    if (isLoggedIn) {
        val mainWindowState = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(1280.dp, 850.dp)
        )

        var currentMainScreen by remember { mutableStateOf<Screen>(Screen.Home) }
        
        // Start game if needed (after login)
        LaunchedEffect(startBalance) {
            if (startBalance != null) {
                homeViewModel.gameTimeService.startGame(startBalance!!)
                startBalance = null
            }
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "C9Cyber - Dashboard",
            state = mainWindowState,
            resizable = false,
        ) {
            MaterialTheme(typography = AppTypography) {
                Surface(color = BackgroundPrimary, modifier = Modifier.fillMaxSize()) {

                    when (val screen = currentMainScreen) {
                        is Screen.Home -> {
                            HomeScreen(
                                viewModel = homeViewModel,
                                navigateTo = { newScreen -> currentMainScreen = newScreen },
                            )
                        }

                        is Screen.Service -> {
                            ServiceMenuScreen(
                                smartCardManager = smartCardManager,
                                serviceRepository = serviceRepository,
                                menuRepository = menuRepository,
                                navigateTo = { newScreen -> currentMainScreen = newScreen }
                            )
                        }

                        is Screen.Settings -> {
                            SettingsScreen(
                                viewModel = settingViewModel,
                                navigateTo = { newScreen -> currentMainScreen = newScreen },
                                onCardLocked = {
                                    scope.launch {
                                        delay(1000)
                                        isLoggedIn = false
                                    }
                                }
                            )
                        }

                        is Screen.GameDetail -> {
                            GameDetailScreen(
                                game = screen.game,
                                gameTimeService = homeViewModel.gameTimeService,
                                currentBalance = homeViewModel.uiState.user?.balance ?: 0,
                                onBack = { currentMainScreen = Screen.Home },
                                onStartGame = {
                                    // Tiền đã được trừ khi đăng nhập, chỉ cần navigate
                                    val balance = homeViewModel.uiState.user?.balance ?: 0
                                    // Đảm bảo game time service đang chạy
                                    if (!homeViewModel.gameTimeService.gameTimeState.value.isPlaying) {
                                        homeViewModel.gameTimeService.startGame(balance)
                                    }
                                    // Không navigate đi đâu cả
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
