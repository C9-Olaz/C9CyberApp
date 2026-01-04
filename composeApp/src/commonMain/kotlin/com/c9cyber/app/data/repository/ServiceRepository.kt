package com.c9cyber.app.data.repository

import com.c9cyber.app.data.api.ApiService
import com.c9cyber.app.data.dummy.DummyServiceData
import com.c9cyber.app.domain.model.ServiceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiceRepository(
    private val apiService: ApiService? = null
) {
    suspend fun getServiceItems(): Result<List<ServiceItem>> = withContext(Dispatchers.IO) {
        // This repository now only provides dummy data as the main logic is in MenuRepository
        try {
            Result.success(DummyServiceData.getDummyServiceItems())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

