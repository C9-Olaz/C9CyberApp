package com.c9cyber.app.utils

actual fun shutdownPC() {
    // Code to execute shutdown command on desktop (Windows, Mac, Linux)
    // For example, on Windows:
    try {
        Runtime.getRuntime().exec("shutdown -s -t 0")
    } catch (e: java.io.IOException) {
        e.printStackTrace()
    }
}

actual fun openExe(path: String) {
    // Code to open an .exe file
    try {
        val file = java.io.File(path)
        if (!file.exists()) {
            throw java.io.FileNotFoundException("File not found: $path")
        }
        
        val processBuilder = ProcessBuilder(file.absolutePath)
        processBuilder.directory(file.parentFile)
        processBuilder.start()
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}
