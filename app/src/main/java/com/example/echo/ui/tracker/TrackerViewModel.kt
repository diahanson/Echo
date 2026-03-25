package com.example.echo.ui.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.echo.data.local.entity.WellnessEntry
import com.example.echo.data.preferences.SessionManager
import com.example.echo.data.repository.WellnessRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TrackerViewModel(
    private val repository: WellnessRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    fun calculateAndSaveScore(mood: String, stress: Int, activities: List<String>, onResult: (Int) -> Unit) {
        viewModelScope.launch {
            val baseScore = 50
            
            val moodBonus = when (mood) {
                "Happy", "Calm", "Relaxed" -> 15
                "Neutral", "Okay" -> 5
                "Sad", "Anxious", "Angry" -> -10
                else -> 0
            }

            val activityBonus = activities.size * 5
            val stressDeduction = stress * 2

            var finalScore = baseScore + moodBonus + activityBonus - stressDeduction
            if (finalScore > 100) finalScore = 100
            if (finalScore < 0) finalScore = 0

            val userId = sessionManager.userIdFlow.first()
            if (userId != -1) {
                val entry = WellnessEntry(
                    userId = userId,
                    mood = mood,
                    stressLevel = stress,
                    activities = activities.joinToString(", "),
                    wellnessScore = finalScore,
                    timestamp = System.currentTimeMillis()
                )
                repository.addWellnessEntry(entry)
            }
            
            onResult(finalScore)
        }
    }

    suspend fun getEntryById(entryId: Int): WellnessEntry? {
        return repository.getEntryById(entryId)
    }

    fun updateExistingEntry(entryId: Int, mood: String, stress: Int, activities: List<String>, onResult: (Int) -> Unit) {
        viewModelScope.launch {
            val baseScore = 50
            
            val moodBonus = when (mood) {
                "Happy", "Calm", "Relaxed" -> 15
                "Neutral", "Okay" -> 5
                "Sad", "Anxious", "Angry" -> -10
                else -> 0
            }

            val activityBonus = activities.size * 5
            val stressDeduction = stress * 2

            var finalScore = baseScore + moodBonus + activityBonus - stressDeduction
            if (finalScore > 100) finalScore = 100
            if (finalScore < 0) finalScore = 0

            val existingEntry = repository.getEntryById(entryId)
            if (existingEntry != null) {
                val updatedEntry = existingEntry.copy(
                    mood = mood,
                    stressLevel = stress,
                    activities = activities.joinToString(", "),
                    wellnessScore = finalScore
                )
                repository.updateWellnessEntry(updatedEntry)
            }
            
            onResult(finalScore)
        }
    }
}

class TrackerViewModelFactory(
    private val wellnessRepository: WellnessRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackerViewModel(wellnessRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
