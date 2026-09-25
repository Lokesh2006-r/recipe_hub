package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.repository.ThemeMode
import com.example.data.repository.ThemeRepository
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = themeRepository.themeMode

    fun setThemeMode(mode: ThemeMode) {
        themeRepository.setThemeMode(mode)
    }

    fun toggleTheme(isCurrentlyDark: Boolean) {
        themeRepository.toggleTheme(isCurrentlyDark)
    }

    class Factory(private val themeRepository: ThemeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
                return ThemeViewModel(themeRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
