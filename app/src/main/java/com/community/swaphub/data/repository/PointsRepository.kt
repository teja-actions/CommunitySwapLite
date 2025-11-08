package com.community.swaphub.data.repository

import com.community.swaphub.data.api.ApiService
import com.community.swaphub.data.model.PointsHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PointsRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getUserPoints(userId: String): Flow<Result<Int>> = flow { // UUID as String
        try {
            val response = apiService.getUserPoints(userId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch points")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getPointsHistory(userId: String): Flow<Result<List<PointsHistory>>> = flow { // UUID as String
        try {
            val response = apiService.getPointsHistory(userId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch points history")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

