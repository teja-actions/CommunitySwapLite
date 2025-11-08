package com.community.swaphub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.community.swaphub.data.model.CreateSwapRequest
import com.community.swaphub.data.model.SwapRequest
import com.community.swaphub.data.repository.SwapRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwapRequestViewModel @Inject constructor(
    private val swapRequestRepository: SwapRequestRepository
) : ViewModel() {
    
    private val _swapRequests = MutableStateFlow<List<SwapRequest>>(emptyList())
    val swapRequests: StateFlow<List<SwapRequest>> = _swapRequests.asStateFlow()
    
    private val _uiState = MutableStateFlow<SwapRequestUiState>(SwapRequestUiState.Idle)
    val uiState: StateFlow<SwapRequestUiState> = _uiState.asStateFlow()
    
    fun loadSwapRequests() {
        viewModelScope.launch {
            _uiState.value = SwapRequestUiState.Loading
            swapRequestRepository.getMySwapRequests().collect { result ->
                result.onSuccess { requests ->
                    _swapRequests.value = requests
                    _uiState.value = SwapRequestUiState.Success
                }.onFailure { error ->
                    _uiState.value = SwapRequestUiState.Error(error.message ?: "Failed to load swap requests")
                }
            }
        }
    }
    
    fun createSwapRequest(requestedItemId: String, requesterId: String) { // Backend expects both IDs
        viewModelScope.launch {
            _uiState.value = SwapRequestUiState.Loading
            val result = swapRequestRepository.createSwapRequest(
                CreateSwapRequest(requestedItemId, requesterId) // Backend expects {requestedItemId, requesterId}
            )
            _uiState.value = when {
                result.isSuccess -> SwapRequestUiState.Success
                else -> SwapRequestUiState.Error(result.exceptionOrNull()?.message ?: "Failed to create swap request")
            }
        }
    }
    
    fun acceptSwapRequest(id: String) { // UUID as String
        viewModelScope.launch {
            _uiState.value = SwapRequestUiState.Loading
            val result = swapRequestRepository.acceptSwapRequest(id)
            _uiState.value = when {
                result.isSuccess -> SwapRequestUiState.Success
                else -> SwapRequestUiState.Error(result.exceptionOrNull()?.message ?: "Failed to accept swap request")
            }
        }
    }
    
    fun declineSwapRequest(id: String) { // UUID as String
        viewModelScope.launch {
            _uiState.value = SwapRequestUiState.Loading
            val result = swapRequestRepository.declineSwapRequest(id)
            _uiState.value = when {
                result.isSuccess -> SwapRequestUiState.Success
                else -> SwapRequestUiState.Error(result.exceptionOrNull()?.message ?: "Failed to decline swap request")
            }
        }
    }
    
    fun completeSwapRequest(id: String) { // UUID as String
        viewModelScope.launch {
            _uiState.value = SwapRequestUiState.Loading
            val result = swapRequestRepository.completeSwapRequest(id)
            _uiState.value = when {
                result.isSuccess -> SwapRequestUiState.Success
                else -> SwapRequestUiState.Error(result.exceptionOrNull()?.message ?: "Failed to complete swap request")
            }
        }
    }
    
    fun clearError() {
        _uiState.value = SwapRequestUiState.Idle
    }
}

sealed class SwapRequestUiState {
    object Idle : SwapRequestUiState()
    object Loading : SwapRequestUiState()
    object Success : SwapRequestUiState()
    data class Error(val message: String) : SwapRequestUiState()
}

