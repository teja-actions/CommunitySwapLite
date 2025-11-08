package com.community.swaphub.data.repository

import com.community.swaphub.data.api.ApiService
import com.community.swaphub.data.local.dao.ItemDao
import com.community.swaphub.data.model.Item
import com.community.swaphub.data.model.ItemType
import com.community.swaphub.data.model.PostItemRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) {
    fun getItems(
        category: String? = null, // Backend uses String, not ItemCategory
        location: String? = null,
        type: ItemType? = null
    ): Flow<Result<List<Item>>> = flow {
        try {
            val response = apiService.getItems(
                type?.name, // Backend uses 'type' query param, not 'category'
                location
            )
            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!
                itemDao.insertItems(items)
                emit(Result.success(items))
            } else {
                // Try to get from local DB
                val localItems = itemDao.getAllItems()
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch items")))
            }
        } catch (e: Exception) {
            // Try local DB on error
            try {
                val localItems = itemDao.getAllItems()
                emit(Result.failure(e))
            } catch (dbError: Exception) {
                emit(Result.failure(e))
            }
        }
    }
    
    fun getItem(id: String): Flow<Result<Item>> = flow { // UUID as String
        try {
            val response = apiService.getItem(id)
            if (response.isSuccessful && response.body() != null) {
                val item = response.body()!!
                itemDao.insertItem(item)
                emit(Result.success(item))
            } else {
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch item")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun postItem(request: PostItemRequest): Result<Item> {
        return try {
            val response = apiService.postItem(request)
            if (response.isSuccessful && response.body() != null) {
                val item = response.body()!!
                itemDao.insertItem(item)
                Result.success(item)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to post item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteItem(id: String): Result<Unit> { // UUID as String
        return try {
            val response = apiService.deleteItem(id)
            if (response.isSuccessful) {
                itemDao.getItemById(id).collect { item ->
                    item?.let { itemDao.deleteItem(it) }
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to delete item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getMyItems(): Flow<Result<List<Item>>> = flow {
        try {
            val response = apiService.getMyItems() // Backend uses /my-items endpoint
            if (response.isSuccessful && response.body() != null) {
                val items = response.body()!!
                itemDao.insertItems(items)
                emit(Result.success(items))
            } else {
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch user items")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getLocalItems(): Flow<List<Item>> = itemDao.getAllItems()
}

