package com.community.swaphub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String, // UUID as String - non-null for Room
    val name: String, // Backend uses 'name' not 'username' or 'fullName'
    val email: String,
    val password: String? = null, // Don't store password in local DB
    val location: String? = null,
    val role: UserRole = UserRole.USER,
    val points: Int = 0,
    @SerializedName("createdAt")
    val createdAt: String? = null
)

enum class UserRole {
    USER, ADMIN
}

data class LoginRequest(
    val email: String,
    val password: String
)

// Backend expects a User object for registration, not a separate RegisterRequest
data class RegisterRequest(
    val name: String, // Backend uses 'name'
    val email: String,
    val password: String,
    val location: String? = null
)

// Backend returns {token: String, userId: UUID}
data class AuthResponse(
    val token: String? = null,
    @SerializedName("userId")
    val userId: String? = null // UUID as String
)

