package com.example.echo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.echo.data.local.entity.Reflection
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionDao {
    @Insert
    suspend fun insertReflection(reflection: Reflection): Long

    @Delete
    suspend fun deleteReflection(reflection: Reflection)

    @Query("SELECT * FROM reflections WHERE userId = :userId ORDER BY timestamp DESC")
    fun getReflectionsForUser(userId: Int): Flow<List<Reflection>>
}
