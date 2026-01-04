package com.c9cyber.app.domain.model

import org.jetbrains.compose.resources.DrawableResource

enum class UserLevel { Bronze, Silver, Gold }

data class User(
    val id: String = "Null",
    val userName: String = "Null",
    val name: String = "Null",
    val balance: Int = 0,
    val level: UserLevel = UserLevel.Bronze,
    val avatar: ByteArray? = null,
    val isFistTimeLogin: Boolean = true,
    val totalUsableTime: Long = 0
)

enum class GameType { Online, Offline }

data class Game(
    val name: String,
    val id: String,
    val type: GameType,
    val imageRes: DrawableResource? = null,
    val imageUrl: String? = null,
    val executablePath: String? = null // Đường dẫn đến file executable của game
)

enum class ServiceType { Drink, Food }

data class ServiceItem(
    val id: String,
    val name: String,
    val type: ServiceType,
    val price: Int,
    val isAvailable: Boolean
)

data class CartItem(
    val serviceItem: ServiceItem,
    val quantity: Int
)

data class Transaction(
    val id: Short,
    val type: Byte,
    val amount: Short
)
