import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.foundation.layout.fillMaxSize
import com.c9cyber.app.presentation.navigation.Screen
import com.c9cyber.app.presentation.screens.home.HomeScreen
import com.c9cyber.app.presentation.screens.service.ServiceMenuScreen
import com.c9cyber.app.presentation.theme.AppTypography
import com.c9cyber.app.presentation.theme.BackgroundPrimary

fun main() = application {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    val navigateTo: (Screen) -> Unit = { screen ->
        currentScreen = screen
    }

    Window(onCloseRequest = ::exitApplication, title = "C9Cyber") {
        MaterialTheme(
            typography = AppTypography
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = BackgroundPrimary
            ) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen(navigateTo = navigateTo)

                    Screen.Service -> ServiceMenuScreen(navigateTo = navigateTo)
                    Screen.Settings -> { /* Todo để màn hình cài đặt */ }
                }
            }
        }
    }
}
