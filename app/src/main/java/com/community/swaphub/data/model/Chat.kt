package com.community.swaphub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey
    val id: String, // UUID as String - non-null for Room
    val senderId: String, // UUID as String
    val receiverId: String, // UUID as String
    val itemId: String? = null, // UUID as String
    val message: String,
    val timestamp: String? = null,
    val isRead: Boolean = false
)

data class ChatConversation(
    val otherUserId: String, // UUID as String
    val otherUserName: String? = null,
    val otherUserImageUrl: String? = null,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val unreadCount: Int = 0,
    val itemId: String? = null, // UUID as String
    val itemTitle: String? = null
)

data class SendMessageRequest(
    val receiverId: String, // UUID as String
    val itemId: String? = null, // UUID as String
    val message: String
)

