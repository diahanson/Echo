package com.example.echo.data.repository

import com.example.echo.data.local.dao.UserDao
import com.example.echo.data.local.entity.User
import com.example.echo.data.preferences.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {
    suspend fun register(user: User): Long = withContext(Dispatchers.IO) {
        val hashedUser = user.copy(passwordHash = hashPassword(user.passwordHash))
        userDao.insertUser(hashedUser)
    }

    suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByEmail(email)
        val hashedInput = hashPassword(password)
        if (user != null && user.passwordHash == hashedInput) {
            sessionManager.saveSession(user.userId)
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }
    
    suspend fun logout() = withContext(Dispatchers.IO) {
        sessionManager.clearSession()
    }
    
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
