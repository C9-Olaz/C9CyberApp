import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.compose.foundation.layout.fillMaxSize
import com.c9cyber.app.domain.smartcard.SmartCardManager
import com.c9cyber.app.domain.smartcard.SmartCardMonitor
import com.c9cyber.app.domain.smartcard.SmartCardTransportImpl
import com.c9cyber.app.presentation.navigation.Screen
import com.c9cyber.app.presentation.screens.home.HomeScreen
import com.c9cyber.app.presentation.screens.home.HomeScreenViewModel
import com.c9cyber.app.presentation.screens.service.ServiceMenuScreen
import com.c9cyber.app.presentation.screens.settings.SettingScreenViewModel
import com.c9cyber.app.presentation.screens.settings.SettingsScreen
import com.c9cyber.app.presentation.screens.standby.StandbyScreenViewModel
import com.c9cyber.app.presentation.screens.standby.StandbyScreens
import com.c9cyber.app.presentation.theme.AppTypography
import com.c9cyber.app.presentation.theme.BackgroundPrimary

fun main() = application {
    var isLoggedIn by remember { mutableStateOf(false) }

    val smartCardTransport = remember { SmartCardTransportImpl() }
    val smartCardMonitor = remember { SmartCardMonitor(smartCardTransport) }

    val smartCardManager = SmartCardManager(
        transport = smartCardTransport,
        monitor = smartCardMonitor
    )

    val standbyViewModel = remember(isLoggedIn) {
        StandbyScreenViewModel(smartCardManager)
    }
    val homeViewModel = remember(isLoggedIn)
    {
        HomeScreenViewModel(smartCardManager)
    }

    val settingViewModel = remember(isLoggedIn)
    {
        SettingScreenViewModel(smartCardTransport)
    }

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
            size = DpSize(600.dp, 400.dp)
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
                            isLoggedIn = true
                        }
                    )
                }
            }
        }
    }

    if (isLoggedIn) {
        val mainWindowState = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(1280.dp, 720.dp)
        )

        var currentMainScreen by remember { mutableStateOf(Screen.Home) }

        Window(
            onCloseRequest = ::exitApplication,
            title = "C9Cyber - Dashboard",
            state = mainWindowState,
            resizable = false,
        ) {
            MaterialTheme(typography = AppTypography) {
                Surface(color = BackgroundPrimary, modifier = Modifier.fillMaxSize()) {

                    when (currentMainScreen) {
                        Screen.Home -> {
                            HomeScreen(
                                viewModel = homeViewModel,
                                navigateTo = { screen -> currentMainScreen = screen },
                            )
                        }

                        Screen.Service -> {
                            ServiceMenuScreen(
                                navigateTo = { screen -> currentMainScreen = screen }
                            )
                        }

                        Screen.Settings -> {
                            SettingsScreen(
                                viewModel = settingViewModel,
                                navigateTo = { screen -> currentMainScreen = screen }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}