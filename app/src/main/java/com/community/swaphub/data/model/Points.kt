package com.community.swaphub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_history")
data class PointsHistory(
    @PrimaryKey
    val id: Long? = null,
    val userId: Long,
    val points: Int,
    val reason: String,
    val type: PointsType,
    val createdAt: String? = null
)

enum class PointsType {
    EARNED,
    SPENT
}

