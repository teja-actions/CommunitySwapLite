package com.community.swaphub.data.local.dao

import androidx.room.*
import com.community.swaphub.data.model.Item
import com.community.swaphub.data.model.ItemType
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>
    
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: String): Flow<Item?> // UUID as String
    
    @Query("SELECT * FROM items WHERE ownerId = :ownerId")
    fun getItemsByOwner(ownerId: String): Flow<List<Item>> // UUID as String
    
    @Query("SELECT * FROM items WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<Item>> // Backend uses String
    
    @Query("SELECT * FROM items WHERE location LIKE :location")
    fun getItemsByLocation(location: String): Flow<List<Item>>
    
    @Query("SELECT * FROM items WHERE type = :type")
    fun getItemsByType(type: ItemType): Flow<List<Item>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)
    
    @Update
    suspend fun updateItem(item: Item)
    
    @Delete
    suspend fun deleteItem(item: Item)
    
    @Query("DELETE FROM items")
    suspend fun clearAll()
}

