package com.c9cyber.app.data.repository

import com.c9cyber.app.data.api.ApiService
import com.c9cyber.app.data.model.MenuCategory
import com.c9cyber.app.data.model.MenuItem

class MenuRepository(private val apiService: ApiService) {

    private var cachedCategories: List<MenuCategory>? = null
    private var cachedItems: List<MenuItem>? = null

    suspend fun getMenuCategories(forceRefresh: Boolean = false): Result<List<MenuCategory>> {
        if (!forceRefresh && cachedCategories != null) {
            println("Returning cached categories")
            return Result.success(cachedCategories!!)
        }
        println("Fetching categories from API...")
        return try {
            val categories = apiService.getAllMenuCategories()
            if (categories != null) {
                println("Successfully fetched ${categories.size} categories.")
                cachedCategories = categories
                Result.success(categories)
            } else {
                println("API returned null categories.")
                Result.failure(Exception("API returned null categories"))
            }
        } catch (e: Exception) {
            println("Failed to fetch categories: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getMenuItems(forceRefresh: Boolean = false): Result<List<MenuItem>> {
        if (!forceRefresh && cachedItems != null) {
            println("Returning cached menu items")
            return Result.success(cachedItems!!)
        }
        println("Fetching menu items from API...")
        return try {
            val items = apiService.getAllMenuItems()
            if (items != null) {
                println("Successfully fetched ${items.size} menu items.")
                items.forEach { println("Item: $it") }
                cachedItems = items
                Result.success(items)
            } else {
                println("API returned null menu items.")
                Result.failure(Exception("API returned null items"))
            }
        } catch (e: Exception) {
            println("Failed to fetch menu items: ${e.message}")
            Result.failure(e)
        }
    }
}
