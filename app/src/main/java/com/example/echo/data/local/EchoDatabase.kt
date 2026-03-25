package com.example.echo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.echo.data.local.dao.ReflectionDao
import com.example.echo.data.local.dao.UserDao
import com.example.echo.data.local.dao.WellnessDao
import com.example.echo.data.local.entity.Reflection
import com.example.echo.data.local.entity.User
import com.example.echo.data.local.entity.WellnessEntry

@Database(entities = [User::class, WellnessEntry::class, Reflection::class], version = 1, exportSchema = false)
abstract class EchoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun wellnessDao(): WellnessDao
    abstract fun reflectionDao(): ReflectionDao

    companion object {
        @Volatile
        private var INSTANCE: EchoDatabase? = null

        fun getDatabase(context: Context): EchoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EchoDatabase::class.java,
                    "echo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
