package com.example.echo.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.echo.data.local.entity.WellnessEntry
import com.example.echo.data.preferences.SessionManager
import com.example.echo.data.repository.WellnessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: WellnessRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _entries = MutableStateFlow<List<WellnessEntry>>(emptyList())
    val entries: StateFlow<List<WellnessEntry>> = _entries.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            val userId = sessionManager.userIdFlow.first()
            if (userId != -1) {
                repository.getEntriesForUser(userId).collect { list ->
                    _entries.value = list
                }
            }
        }
    }

    fun deleteEntry(entry: WellnessEntry) {
        viewModelScope.launch {
            repository.deleteWellnessEntry(entry)
        }
    }
}

class HistoryViewModelFactory(
    private val wellnessRepository: WellnessRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(wellnessRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
