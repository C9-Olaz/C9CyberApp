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
        Runtime.getRuntime().exec(path)
    } catch (e: java.io.IOException) {
        e.printStackTrace()
    }
}
