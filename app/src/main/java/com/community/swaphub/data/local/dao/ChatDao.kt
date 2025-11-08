package com.community.swaphub.data.local.dao

import androidx.room.*
import com.community.swaphub.data.model.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("""
        SELECT * FROM chats 
        WHERE (senderId = :userId1 AND receiverId = :userId2) 
        OR (senderId = :userId2 AND receiverId = :userId1)
        ORDER BY timestamp ASC
    """)
    fun getMessages(userId1: String, userId2: String): Flow<List<Chat>> // UUID as String
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(chat: Chat)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(chats: List<Chat>)
    
    @Query("UPDATE chats SET isRead = 1 WHERE receiverId = :userId AND senderId = :otherUserId")
    suspend fun markAsRead(userId: String, otherUserId: String) // UUID as String
}

