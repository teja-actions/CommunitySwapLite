package com.community.swaphub.data.local.dao

import androidx.room.*
import com.community.swaphub.data.model.SwapRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface SwapRequestDao {
    @Query("SELECT * FROM swap_requests WHERE requesterId = :userId OR ownerId = :userId")
    fun getSwapRequestsByUser(userId: String): Flow<List<SwapRequest>> // UUID as String
    
    @Query("SELECT * FROM swap_requests WHERE id = :id")
    fun getSwapRequestById(id: String): Flow<SwapRequest?> // UUID as String
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwapRequest(swapRequest: SwapRequest)
    
    @Update
    suspend fun updateSwapRequest(swapRequest: SwapRequest)
    
    @Delete
    suspend fun deleteSwapRequest(swapRequest: SwapRequest)
}

