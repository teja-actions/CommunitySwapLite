package com.community.swaphub.data.repository

import com.community.swaphub.data.api.ApiService
import com.community.swaphub.data.local.dao.UserDao
import com.community.swaphub.data.model.AuthResponse
import com.community.swaphub.data.model.LoginRequest
import com.community.swaphub.data.model.RegisterRequest
import com.community.swaphub.data.model.User
import com.community.swaphub.util.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val preferencesManager: PreferencesManager
) {
    // ------------------ REGISTER ------------------
    suspend fun register(name: String, email: String, password: String, location: String?): Result<AuthResponse> {
        return try {
            // Backend expects a User object for registration
            // Use placeholder ID - backend will generate the real UUID
            val user = User(
                id = "", // Placeholder - backend will generate UUID
                name = name,
                email = email,
                password = password,
                location = location
            )

            val response = apiService.register(user)

            if (response.isSuccessful && response.body() != null) {
                val savedUser = response.body()!!
                
                // Save user to local database
                userDao.insertUser(savedUser)
                preferencesManager.saveUserId(savedUser.id)
                preferencesManager.saveUserEmail(savedUser.email)

                // After registration, we need to login to get the token
                // Backend registration doesn't return a token, so we login
                val loginResult = login(email, password)
                return loginResult
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ------------------ LOGIN ------------------
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Backend returns {token, userId}, not user object
                // We need to fetch the user separately
                authResponse.userId?.let { userId ->
                    preferencesManager.saveUserId(userId)
                    preferencesManager.saveUserEmail(email)
                    
                    // Fetch user details
                    try {
                        val userResponse = apiService.getUser(userId)
                        if (userResponse.isSuccessful && userResponse.body() != null) {
                            val user = userResponse.body()!!
                            userDao.insertUser(user)
                        }
                    } catch (e: Exception) {
                        // If fetching user fails, we still have the token and userId
                        // User can be fetched later
                    }
                }

                authResponse.token?.let { token ->
                    preferencesManager.saveAuthToken(token)
                }

                Result.success(authResponse)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ------------------ LOGOUT ------------------
    suspend fun logout() {
        preferencesManager.clearAuth()
        userDao.clearAll()
    }

    // ------------------ CURRENT USER ------------------
    fun getCurrentUser(): Flow<User?> {
        return preferencesManager.userId.flatMapLatest { userId ->
            userId?.let { userDao.getUserById(it) } ?: flowOf(null)
        }
    }

    suspend fun getCurrentUserId(): String? {
        return preferencesManager.userId.first()
    }

    suspend fun isLoggedIn(): Boolean {
        return preferencesManager.authToken.first() != null
    }
}
