package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.UserEntity
import com.example.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = authRepository.currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    fun login(email: String, pass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            val res = authRepository.login(email, pass)
            _isLoading.value = false
            res.onSuccess {
                _userMessage.emit("Welcome back, ${it.name}!")
                onSuccess()
            }.onFailure {
                _authError.value = it.message ?: "Failed to log in"
            }
        }
    }

    fun register(name: String, email: String, pass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            val res = authRepository.register(name, email, pass)
            _isLoading.value = false
            res.onSuccess {
                _userMessage.emit("Account created! Welcome, ${it.name}!")
                onSuccess()
            }.onFailure {
                _authError.value = it.message ?: "Failed to register"
            }
        }
    }

    fun updateProfile(name: String, avatarUrl: String) {
        viewModelScope.launch {
            val res = authRepository.updateProfile(name, avatarUrl)
            res.onSuccess {
                _userMessage.emit("Profile updated!")
            }.onFailure {
                _authError.value = it.message
            }
        }
    }

    fun logout() {
        authRepository.logout()
        viewModelScope.launch {
            _userMessage.emit("Logged out. Switched to Guest mode.")
        }
    }

    fun clearError() {
        _authError.value = null
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authRepository) as T
        }
    }
}
