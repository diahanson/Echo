package com.example.echo

import android.app.Application
import com.example.echo.data.local.EchoDatabase
import com.example.echo.data.preferences.SessionManager
import com.example.echo.data.repository.AuthRepository
import com.example.echo.data.repository.WellnessRepository

class EchoApplication : Application() {
    val database by lazy { EchoDatabase.getDatabase(this) }
    val sessionManager by lazy { SessionManager(this) }
    
    val authRepository by lazy { AuthRepository(database.userDao(), sessionManager) }
    val wellnessRepository by lazy { WellnessRepository(database.wellnessDao(), database.reflectionDao()) }
}
