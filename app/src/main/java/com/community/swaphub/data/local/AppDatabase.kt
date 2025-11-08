package com.community.swaphub.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.community.swaphub.data.local.dao.*
import com.community.swaphub.data.model.*

@Database(
    entities = [User::class, Item::class, SwapRequest::class, Chat::class, PointsHistory::class],
    version = 2, // Incremented to force Room to regenerate after schema changes
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun itemDao(): ItemDao
    abstract fun swapRequestDao(): SwapRequestDao
    abstract fun chatDao(): ChatDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "swap_hub_database"
                )
                .fallbackToDestructiveMigration() // Allow Room to recreate DB on schema changes (for development)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

