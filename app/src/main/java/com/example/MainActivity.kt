package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.remote.MealDbApi
import com.example.data.repository.AuthRepository
import com.example.data.repository.RecipeRepository
import com.example.data.repository.ShoppingRepository
import com.example.data.repository.ThemeMode
import com.example.data.repository.ThemeRepository
import com.example.ui.navigation.RecipeHubApp
import com.example.ui.theme.RecipeHubTheme
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.RecipeViewModel
import com.example.ui.viewmodel.ShoppingViewModel
import com.example.ui.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themeRepository = ThemeRepository(applicationContext)
        val database = AppDatabase.getInstance(applicationContext)
        val mealDbApi = MealDbApi.create()
        val recipeRepository = RecipeRepository(
            recipeDao = database.recipeDao(),
            categoryDao = database.categoryDao(),
            mealDbApi = mealDbApi
        )
        val shoppingRepository = ShoppingRepository(
            shoppingListDao = database.shoppingListDao()
        )
        val authRepository = AuthRepository(
            context = applicationContext,
            userDao = database.userDao()
        )

        setContent {
            val themeViewModel: ThemeViewModel = viewModel(
                factory = ThemeViewModel.Factory(themeRepository)
            )
            val themeMode by themeViewModel.themeMode.collectAsState()
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            RecipeHubTheme(darkTheme = isDark) {
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModel.Factory(authRepository)
                )
                val recipeViewModel: RecipeViewModel = viewModel(
                    factory = RecipeViewModel.Factory(recipeRepository)
                )
                val currentUserId = authRepository.currentUser.value?.id ?: "user_julian_1"
                val shoppingViewModel: ShoppingViewModel = viewModel(
                    factory = ShoppingViewModel.Factory(shoppingRepository, currentUserId)
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeHubApp(
                        recipeViewModel = recipeViewModel,
                        shoppingViewModel = shoppingViewModel,
                        authViewModel = authViewModel,
                        themeMode = themeMode,
                        isDarkTheme = isDark,
                        onToggleTheme = { themeViewModel.toggleTheme(isDark) },
                        onSetThemeMode = { themeViewModel.setThemeMode(it) }
                    )
                }
            }
        }
    }
}
