package com.community.swaphub.data.repository

import com.community.swaphub.data.api.ApiService
import com.community.swaphub.data.local.dao.ChatDao
import com.community.swaphub.data.model.Chat
import com.community.swaphub.data.model.ChatConversation
import com.community.swaphub.data.model.SendMessageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ApiService,
    private val chatDao: ChatDao
) {
    suspend fun sendMessage(request: SendMessageRequest): Result<Chat> {
        return try {
            val response = apiService.sendMessage(request)
            if (response.isSuccessful && response.body() != null) {
                val chat = response.body()!!
                chatDao.insertMessage(chat)
                Result.success(chat)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to send message"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getConversations(userId: String): Flow<Result<List<ChatConversation>>> = flow { // UUID as String
        try {
            val response = apiService.getConversations(userId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch conversations")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getMessages(userId1: String, userId2: String, itemId: String? = null): Flow<Result<List<Chat>>> = flow { // UUID as String
        try {
            val response = apiService.getMessages(userId1, userId2, itemId)
            if (response.isSuccessful && response.body() != null) {
                val messages = response.body()!!
                chatDao.insertMessages(messages)
                emit(Result.success(messages))
            } else {
                emit(Result.failure(Exception(response.message() ?: "Failed to fetch messages")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    fun getLocalMessages(userId1: String, userId2: String): Flow<List<Chat>> { // UUID as String
        return chatDao.getMessages(userId1, userId2)
    }
    
    suspend fun markAsRead(userId: String, otherUserId: String) { // UUID as String
        chatDao.markAsRead(userId, otherUserId)
    }
}

