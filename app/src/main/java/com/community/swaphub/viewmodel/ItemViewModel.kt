package com.community.swaphub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.community.swaphub.data.model.Item
import com.community.swaphub.data.model.ItemType
import com.community.swaphub.data.model.PostItemRequest
import com.community.swaphub.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
    
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()
    
    private val _selectedItem = MutableStateFlow<Item?>(null)
    val selectedItem: StateFlow<Item?> = _selectedItem.asStateFlow()
    
    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Idle)
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()
    
    private val _filterCategory = MutableStateFlow<String?>(null) // Backend uses String
    val filterCategory: StateFlow<String?> = _filterCategory.asStateFlow()
    
    private val _filterLocation = MutableStateFlow<String?>(null)
    val filterLocation: StateFlow<String?> = _filterLocation.asStateFlow()
    
    private val _filterType = MutableStateFlow<ItemType?>(null)
    val filterType: StateFlow<ItemType?> = _filterType.asStateFlow()
    
    fun loadItems(category: String? = null, location: String? = null, type: ItemType? = null) {
        viewModelScope.launch {
            _uiState.value = ItemUiState.Loading
            _filterCategory.value = category
            _filterLocation.value = location
            _filterType.value = type
            
            itemRepository.getItems(category, location, type).collect { result ->
                result.onSuccess { items ->
                    _items.value = items
                    _uiState.value = ItemUiState.Success
                }.onFailure { error ->
                    _uiState.value = ItemUiState.Error(error.message ?: "Failed to load items")
                }
            }
        }
    }
    
    fun loadItem(id: String) { // UUID as String
        viewModelScope.launch {
            _uiState.value = ItemUiState.Loading
            itemRepository.getItem(id).collect { result ->
                result.onSuccess { item ->
                    _selectedItem.value = item
                    _uiState.value = ItemUiState.Success
                }.onFailure { error ->
                    _uiState.value = ItemUiState.Error(error.message ?: "Failed to load item")
                }
            }
        }
    }
    
    fun postItem(
        title: String,
        description: String? = null,
        category: String? = null, // Backend uses String
        type: ItemType,
        location: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = ItemUiState.Loading
            val result = itemRepository.postItem(
                PostItemRequest(title, description, category, type, location, latitude, longitude)
            )
            _uiState.value = when {
                result.isSuccess -> ItemUiState.Success
                else -> ItemUiState.Error(result.exceptionOrNull()?.message ?: "Failed to post item")
            }
        }
    }
    
    fun deleteItem(id: String) { // UUID as String
        viewModelScope.launch {
            _uiState.value = ItemUiState.Loading
            val result = itemRepository.deleteItem(id)
            _uiState.value = when {
                result.isSuccess -> ItemUiState.Success
                else -> ItemUiState.Error(result.exceptionOrNull()?.message ?: "Failed to delete item")
            }
        }
    }
    
    fun clearError() {
        _uiState.value = ItemUiState.Idle
    }
}

sealed class ItemUiState {
    object Idle : ItemUiState()
    object Loading : ItemUiState()
    object Success : ItemUiState()
    data class Error(val message: String) : ItemUiState()
}

