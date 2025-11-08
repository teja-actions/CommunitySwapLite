package com.community.swaphub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "items")
data class Item(
    @PrimaryKey
    val id: String, // UUID as String - non-null for Room
    val title: String,
    val description: String? = null,
    val category: String? = null, // Backend uses String, not enum
    val type: ItemType,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    val location: String? = null,
    val latitude: Double? = null, // Backend has latitude
    val longitude: Double? = null, // Backend has longitude
    @SerializedName("status")
    val status: ItemStatus = ItemStatus.AVAILABLE,
    @SerializedName("isActive")
    val isActive: Boolean = true,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    // Backend returns user object, but we'll extract these for convenience
    @SerializedName("ownerId")
    val ownerId: String? = null, // Extracted from user object
    val ownerName: String? = null
)

enum class ItemType {
    GIVEAWAY,
    SWAP
}

enum class ItemStatus {
    AVAILABLE,
    SWAPPED, // Backend uses SWAPPED, not COMPLETED
    PENDING_SWAP // Backend uses PENDING_SWAP, not PENDING
}

data class PostItemRequest(
    val title: String,
    val description: String? = null,
    val category: String? = null,
    val type: ItemType,
    val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

