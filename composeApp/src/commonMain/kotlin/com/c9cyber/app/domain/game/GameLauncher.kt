package com.c9cyber.app.domain.game

import com.c9cyber.app.domain.model.Game
import java.io.File

class GameLauncher {

    fun hasExecutable(game: Game): Boolean {
        return !game.executablePath.isNullOrBlank() && File(game.executablePath).exists()
    }

    fun launchGame(game: Game): Boolean {
        if (!hasExecutable(game)) {
            println("Error: Game executable path is not valid or does not exist: ${game.executablePath}")
            return false
        }

        return try {
            val processBuilder = ProcessBuilder(game.executablePath)
            processBuilder.directory(File(game.executablePath).parentFile) // Set working directory
            processBuilder.start()
            println("Successfully launched game: ${game.name}")
            true
        } catch (e: Exception) {
            println("Error launching game: ${game.name}. Path: ${game.executablePath}")
            e.printStackTrace()
            false
        }
    }
}

