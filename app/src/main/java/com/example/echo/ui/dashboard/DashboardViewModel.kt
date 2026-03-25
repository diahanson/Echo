package com.example.echo.ui.dashboard

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

class DashboardViewModel(
    private val wellnessRepository: WellnessRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _latestEntry = MutableStateFlow<WellnessEntry?>(null)
    val latestEntry: StateFlow<WellnessEntry?> = _latestEntry.asStateFlow()

    init {
        loadLatestEntry()
    }

    private fun loadLatestEntry() {
        viewModelScope.launch {
            val userId = sessionManager.userIdFlow.first()
            if (userId != -1) {
                wellnessRepository.getLatestEntryForUser(userId).collect { entry ->
                    _latestEntry.value = entry
                }
            }
        }
    }
}

class DashboardViewModelFactory(
    private val wellnessRepository: WellnessRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(wellnessRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
