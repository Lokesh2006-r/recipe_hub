package com.example.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

class ThemeRepository(context: Context) {
    private val prefs = context.getSharedPreferences("recipehub_theme_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadSavedTheme())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private fun loadSavedTheme(): ThemeMode {
        val saved = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(saved ?: ThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _themeMode.value = mode
    }

    fun toggleTheme(isCurrentlyDark: Boolean) {
        val nextMode = if (isCurrentlyDark) ThemeMode.LIGHT else ThemeMode.DARK
        setThemeMode(nextMode)
    }

    companion object {
        private const val KEY_THEME_MODE = "key_theme_mode"
    }
}
