package com.example.echo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.echo.data.local.entity.WellnessEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WellnessDao {
    @Insert
    suspend fun insertEntry(entry: WellnessEntry): Long

    @androidx.room.Update
    suspend fun updateEntry(entry: WellnessEntry)

    @Delete
    suspend fun deleteEntry(entry: WellnessEntry)

    @Query("SELECT * FROM wellness_entries WHERE userId = :userId ORDER BY timestamp DESC")
    fun getEntriesForUser(userId: Int): Flow<List<WellnessEntry>>

    @Query("SELECT * FROM wellness_entries WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestEntryForUser(userId: Int): Flow<WellnessEntry?>
    
    @Query("SELECT * FROM wellness_entries WHERE entryId = :entryId LIMIT 1")
    suspend fun getEntryById(entryId: Int): WellnessEntry?
}
