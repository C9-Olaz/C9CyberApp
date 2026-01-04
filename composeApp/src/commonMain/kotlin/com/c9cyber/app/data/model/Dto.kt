package com.c9cyber.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val user_id: String,
    val public_key: String
)

@Serializable
data class ChallengeRequest(
    val user_id: String
)

@Serializable
data class ChallengeResponse(
    val challenge: String
)

@Serializable
data class VerifyRequest(
    val user_id: String,
    val encrypted_challenge: String
)

@Serializable
data class VerifyResponse(
    val status: String,
    val message: String
)

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String
)

@Serializable
data class MenuCategory(
    val id: Int,
    val name: String,
    val description: String? = null
)

@Serializable
data class CreateMenuCategoryRequest(
    val name: String,
    val description: String? = null
)

@Serializable
data class UpdateMenuCategoryRequest(
    val name: String? = null,
    val description: String? = null
)

@Serializable
data class MenuItem(
    val id: Int,
    val category_id: Int,
    val category_name: String,
    val name: String,
    val price: Double,
    val is_available: Boolean,
    val created_at: String
)

@Serializable
data class CreateMenuItemRequest(
    val category_id: Int,
    val name: String,
    val price: Double,
    val is_available: Boolean? = true
)

@Serializable
data class UpdateMenuItemRequest(
    val category_id: Int? = null,
    val name: String? = null,
    val price: Double? = null,
    val is_available: Boolean? = null
)

@Serializable
data class UpdateMenuItemStatusRequest(
    val is_available: Boolean
)
