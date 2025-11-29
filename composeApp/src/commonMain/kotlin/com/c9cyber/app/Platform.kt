package com.c9cyber.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform