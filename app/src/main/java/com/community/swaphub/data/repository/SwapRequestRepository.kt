package com.community.swaphub.data.repository

import com.community.swaphub.data.api.ApiService
import com.community.swaphub.data.local.dao.SwapRequestDao
import com.community.swaphub.data.model.CreateSwapRequest
import com.community.swaphub.data.model.SwapRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SwapRequestRepository @Inject constructor(
    private val apiService: ApiService,
    private val swapRequestDao: SwapRequestDao
) {
    suspend fun createSwapRequest(request: CreateSwapRequest): Result<SwapRequest> {
        return try {
            val response = apiService.createSwapRequest(request)
            if (response.isSuccessful && response.body() != null) {
                val swapRequest = response.body()!!
                swapRequestDao.insertSwapRequest(swapRequest)
                Result.success(swapRequest)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to create swap request"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getMySwapRequests(): Flow<Result<List<SwapRequest>>> = flow {
        try {
            val response = apiService.getMySwapRequests() // Backend uses /my-swaps endpoint
            if (response.isSuccessful && response.body() != null) {
                val requests = response.body()!!
                requests.forEach { swapRequestDao.insertSwapRequest(it) }
                emit(Result.success(requests))
            } else {
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch swap requests")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun acceptSwapRequest(id: String): Result<SwapRequest> { // UUID as String
        return try {
            val response = apiService.acceptSwapRequest(id)
            if (response.isSuccessful && response.body() != null) {
                val swapRequest = response.body()!!
                swapRequestDao.updateSwapRequest(swapRequest)
                Result.success(swapRequest)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to accept swap request"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun declineSwapRequest(id: String): Result<SwapRequest> { // UUID as String
        // Backend doesn't have decline endpoint, only accept and complete
        // This would need to be handled differently or removed
        return Result.failure(Exception("Decline endpoint not available in backend"))
    }
    
    suspend fun completeSwapRequest(id: String): Result<SwapRequest> { // UUID as String
        return try {
            val response = apiService.completeSwapRequest(id)
            if (response.isSuccessful && response.body() != null) {
                val swapRequest = response.body()!!
                swapRequestDao.updateSwapRequest(swapRequest)
                Result.success(swapRequest)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to complete swap request"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

