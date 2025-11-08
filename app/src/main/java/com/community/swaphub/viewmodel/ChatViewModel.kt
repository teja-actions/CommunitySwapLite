package com.community.swaphub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.community.swaphub.data.model.Chat
import com.community.swaphub.data.model.ChatConversation
import com.community.swaphub.data.model.SendMessageRequest
import com.community.swaphub.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    
    private val _conversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()
    
    private val _messages = MutableStateFlow<List<Chat>>(emptyList())
    val messages: StateFlow<List<Chat>> = _messages.asStateFlow()
    
    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    fun loadConversations(userId: String) { // UUID as String
        viewModelScope.launch {
            _uiState.value = ChatUiState.Loading
            chatRepository.getConversations(userId).collect { result ->
                result.onSuccess { convs ->
                    _conversations.value = convs
                    _uiState.value = ChatUiState.Success
                }.onFailure { error ->
                    _uiState.value = ChatUiState.Error(error.message ?: "Failed to load conversations")
                }
            }
        }
    }
    
    fun loadMessages(userId1: String, userId2: String, itemId: String? = null) { // UUID as String
        viewModelScope.launch {
            _uiState.value = ChatUiState.Loading
            chatRepository.getMessages(userId1, userId2, itemId).collect { result ->
                result.onSuccess { msgs ->
                    _messages.value = msgs
                    _uiState.value = ChatUiState.Success
                }.onFailure { error ->
                    _uiState.value = ChatUiState.Error(error.message ?: "Failed to load messages")
                }
            }
        }
    }
    
    fun sendMessage(receiverId: String, message: String, itemId: String? = null) { // UUID as String
        viewModelScope.launch {
            val result = chatRepository.sendMessage(SendMessageRequest(receiverId, itemId, message))
            if (result.isSuccess) {
                // Reload messages
                // Note: You might want to get current user ID from auth
            } else {
                _uiState.value = ChatUiState.Error(result.exceptionOrNull()?.message ?: "Failed to send message")
            }
        }
    }
    
    fun markAsRead(userId: String, otherUserId: String) { // UUID as String
        viewModelScope.launch {
            chatRepository.markAsRead(userId, otherUserId)
        }
    }
    
    fun clearError() {
        _uiState.value = ChatUiState.Idle
    }
}

sealed class ChatUiState {
    object Idle : ChatUiState()
    object Loading : ChatUiState()
    object Success : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}

