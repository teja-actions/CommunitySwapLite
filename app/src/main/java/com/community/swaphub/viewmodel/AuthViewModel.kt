package com.community.swaphub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.community.swaphub.data.model.LoginRequest
import com.community.swaphub.data.model.RegisterRequest
import com.community.swaphub.data.model.User
import com.community.swaphub.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(email, password)
            _uiState.value = when {
                result.isSuccess -> {
                    // User is fetched separately in repository, check current user
                    checkAuthStatus()
                    AuthUiState.Success(_currentUser.value)
                }
                else -> AuthUiState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }
    
    fun register(
        name: String, // Backend uses 'name' not 'username'
        email: String,
        password: String,
        location: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.register(name, email, password, location)
            _uiState.value = when {
                result.isSuccess -> {
                    // User is fetched separately in repository, check current user
                    checkAuthStatus()
                    AuthUiState.Success(_currentUser.value)
                }
                else -> AuthUiState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _currentUser.value = null
            _uiState.value = AuthUiState.Idle
        }
    }
    
    fun clearError() {
        _uiState.value = AuthUiState.Idle
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

