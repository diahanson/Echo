package com.example.echo.data.repository

import com.example.echo.data.local.dao.ReflectionDao
import com.example.echo.data.local.dao.WellnessDao
import com.example.echo.data.local.entity.Reflection
import com.example.echo.data.local.entity.WellnessEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WellnessRepository(
    private val wellnessDao: WellnessDao,
    private val reflectionDao: ReflectionDao
) {
    suspend fun addWellnessEntry(entry: WellnessEntry) = withContext(Dispatchers.IO) {
        wellnessDao.insertEntry(entry)
    }

    suspend fun updateWellnessEntry(entry: WellnessEntry) = withContext(Dispatchers.IO) {
        wellnessDao.updateEntry(entry)
    }

    suspend fun deleteWellnessEntry(entry: WellnessEntry) = withContext(Dispatchers.IO) {
        wellnessDao.deleteEntry(entry)
    }

    fun getEntriesForUser(userId: Int): Flow<List<WellnessEntry>> {
        return wellnessDao.getEntriesForUser(userId)
    }
    
    fun getLatestEntryForUser(userId: Int): Flow<WellnessEntry?> {
        return wellnessDao.getLatestEntryForUser(userId)
    }
    
    suspend fun getEntryById(entryId: Int): WellnessEntry? = withContext(Dispatchers.IO) {
        wellnessDao.getEntryById(entryId)
    }

    suspend fun addReflection(reflection: Reflection) = withContext(Dispatchers.IO) {
        reflectionDao.insertReflection(reflection)
    }
    
    suspend fun deleteReflection(reflection: Reflection) = withContext(Dispatchers.IO) {
        reflectionDao.deleteReflection(reflection)
    }

    fun getReflectionsForUser(userId: Int): Flow<List<Reflection>> {
        return reflectionDao.getReflectionsForUser(userId)
    }
}
