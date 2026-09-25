package com.example.data.repository

import android.content.Context
import com.example.data.local.UserDao
import com.example.data.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID

class AuthRepository(
    context: Context,
    private val userDao: UserDao
) {
    private val prefs = context.getSharedPreferences("recipehub_auth_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    init {
        val savedId = prefs.getString("user_id", null)
        val savedName = prefs.getString("user_name", "Chef Julian")
        val savedEmail = prefs.getString("user_email", "chef@recipehub.com")
        val savedAvatar = prefs.getString("user_avatar", "https://images.unsplash.com/photo-1577219491135-ce391730fb2c?auto=format&fit=crop&w=300&q=80")

        if (savedId != null) {
            _currentUser.value = UserEntity(
                id = savedId,
                name = savedName ?: "Chef Julian",
                email = savedEmail ?: "chef@recipehub.com",
                avatarUrl = savedAvatar ?: ""
            )
        } else {
            // Default active demo chef
            val defaultUser = UserEntity(
                id = "user_julian_1",
                name = "Chef Julian Vance",
                email = "chef@recipehub.com",
                avatarUrl = "https://images.unsplash.com/photo-1577219491135-ce391730fb2c?auto=format&fit=crop&w=300&q=80"
            )
            _currentUser.value = defaultUser
            saveUserSession(defaultUser)
        }
    }

    private fun saveUserSession(user: UserEntity) {
        prefs.edit()
            .putString("user_id", user.id)
            .putString("user_name", user.name)
            .putString("user_email", user.email)
            .putString("user_avatar", user.avatarUrl)
            .apply()
    }

    suspend fun login(email: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isEmpty() || password.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Email and password cannot be empty."))
        }

        val existing = userDao.getUserByEmail(cleanEmail)
        if (existing != null) {
            _currentUser.value = existing
            saveUserSession(existing)
            Result.success(existing)
        } else {
            // Allow logging in or creating demo session
            val user = UserEntity(
                id = "user_" + UUID.randomUUID().toString().take(8),
                name = cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = cleanEmail,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
            )
            userDao.insertUser(user)
            _currentUser.value = user
            saveUserSession(user)
            Result.success(user)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        if (name.isBlank() || cleanEmail.isBlank() || password.length < 4) {
            return@withContext Result.failure(IllegalArgumentException("Please enter a valid name, email, and password (min 4 characters)."))
        }

        val existing = userDao.getUserByEmail(cleanEmail)
        if (existing != null) {
            return@withContext Result.failure(IllegalArgumentException("Account with this email already exists."))
        }

        val newUser = UserEntity(
            id = "user_" + UUID.randomUUID().toString().take(8),
            name = name.trim(),
            email = cleanEmail,
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80"
        )
        userDao.insertUser(newUser)
        _currentUser.value = newUser
        saveUserSession(newUser)
        Result.success(newUser)
    }

    suspend fun updateProfile(name: String, avatarUrl: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val current = _currentUser.value ?: return@withContext Result.failure(IllegalStateException("Not logged in"))
        val updated = current.copy(name = name.trim(), avatarUrl = avatarUrl.trim())
        userDao.updateUser(updated)
        _currentUser.value = updated
        saveUserSession(updated)
        Result.success(updated)
    }

    fun logout() {
        prefs.edit().clear().apply()
        // Reset to Guest or default
        val guest = UserEntity(
            id = "user_guest",
            name = "Guest Cook",
            email = "guest@recipehub.com",
            avatarUrl = ""
        )
        _currentUser.value = guest
        saveUserSession(guest)
    }
}
