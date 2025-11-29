package com.c9cyber.app.domain.model

enum class UserLevel { Bronze, Silver, Gold }

data class User(
    val id: String,
    val name: String,
    val balance: Int,
    val level: UserLevel,
    val avatar: ByteArray,
    val totalUsableTime: Long
)

enum class GameType { Online, Offline }

data class Game(
    val name: String,
    val id: String,
    val type: GameType,
    val image: ByteArray
)

enum class ServiceType { Drink, Food }

data class ServiceItem(
    val id: String,
    val name: String,
    val type: ServiceType,
    val price: Int,
    val isAvailable: Boolean,
    val image: String
)