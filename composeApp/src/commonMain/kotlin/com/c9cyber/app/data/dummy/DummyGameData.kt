package com.c9cyber.app.data.dummy

import com.c9cyber.app.domain.model.Game
import com.c9cyber.app.domain.model.GameType
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.game_placeholder

object DummyGameData {
    fun getDummyGames(): List<Game> {
        return listOf(
            Game(
                id = "1",
                name = "League of Legends",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "2",
                name = "Valorant",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "3",
                name = "Counter-Strike 2",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "4",
                name = "PUBG",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "5",
                name = "GTA V",
                type = GameType.Offline,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "6",
                name = "FIFA 24",
                type = GameType.Offline,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "7",
                name = "Call of Duty: Warzone",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "8",
                name = "Apex Legends",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "9",
                name = "Minecraft",
                type = GameType.Offline,
                imageRes = Res.drawable.game_placeholder,
                executablePath = "E:\\PollyMC-Windows-MinGW-w64-Portable-6.3/pollymc.exe"
            ),
            Game(
                id = "10",
                name = "Rocket League",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "11",
                name = "Fortnite",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            ),
            Game(
                id = "12",
                name = "Dota 2",
                type = GameType.Online,
                imageRes = Res.drawable.game_placeholder
            )
        )
    }
}
