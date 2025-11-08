package com.community.swaphub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "swap_requests")
data class SwapRequest(
    @PrimaryKey
    val id: String, // UUID as String - non-null for Room
    @SerializedName("itemId")
    val requestedItemId: String, // Backend DTO uses itemId
    @SerializedName("itemTitle")
    val requestedItemTitle: String? = null,
    @SerializedName("requesterId")
    val requesterId: String,
    @SerializedName("requesterName")
    val requesterName: String? = null,
    @SerializedName("itemOwnerId")
    val ownerId: String, // Backend DTO uses itemOwnerId
    @SerializedName("itemOwnerName")
    val ownerName: String? = null,
    @SerializedName("status")
    val status: SwapRequestStatus = SwapRequestStatus.REQUESTED,
    @SerializedName("createdAt")
    val createdAt: String? = null
)

enum class SwapRequestStatus {
    REQUESTED, // Backend uses REQUESTED, not PENDING
    ACCEPTED,
    REJECTED, // Backend uses REJECTED, not DECLINED
    COMPLETED
}

// Backend expects {requestedItemId: UUID, requesterId: UUID}
data class CreateSwapRequest(
    @SerializedName("requestedItemId")
    val requestedItemId: String, // UUID as String
    @SerializedName("requesterId")
    val requesterId: String // UUID as String
)

