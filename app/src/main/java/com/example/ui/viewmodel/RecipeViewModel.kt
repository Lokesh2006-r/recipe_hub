package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CategoryEntity
import com.example.data.model.RecipeEntity
import com.example.data.repository.RecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RecipeFilterState(
    val query: String = "",
    val category: String = "All",
    val dietary: String = "All", // "All", "Vegetarian", "Non-Vegetarian", "Vegan"
    val difficulty: String = "All", // "All", "Easy", "Medium", "Hard"
    val maxTime: Int = 0, // 0 = any, 15, 30, 60 mins
    val sortBy: String = "newest" // "newest", "shortest_time", "alphabetical"
)

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    val allCategories: StateFlow<List<CategoryEntity>> = recipeRepository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteRecipes: StateFlow<List<RecipeEntity>> = recipeRepository.favoriteRecipes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _filterState = MutableStateFlow(RecipeFilterState())
    val filterState: StateFlow<RecipeFilterState> = _filterState.asStateFlow()

    private val _isSearchingExternal = MutableStateFlow(false)
    val isSearchingExternal: StateFlow<Boolean> = _isSearchingExternal.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    val filteredRecipes: StateFlow<List<RecipeEntity>> = _filterState.flatMapLatest { filter ->
        recipeRepository.searchRecipes(
            query = filter.query.trim(),
            category = filter.category,
            dietary = filter.dietary,
            difficulty = filter.difficulty,
            maxTime = filter.maxTime,
            sortBy = filter.sortBy
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredRecipes: StateFlow<List<RecipeEntity>> = recipeRepository.allRecipes
        .combine(_filterState) { recipes, _ ->
            recipes.take(6)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quickMeals: StateFlow<List<RecipeEntity>> = recipeRepository.allRecipes
        .combine(_filterState) { recipes, _ ->
            recipes.filter { (it.prepTimeMinutes + it.cookTimeMinutes) <= 30 }.take(6)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            recipeRepository.initSeedDataIfEmpty()
        }
    }

    fun setQuery(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    fun setCategory(category: String) {
        _filterState.value = _filterState.value.copy(category = category)
    }

    fun setDietary(dietary: String) {
        _filterState.value = _filterState.value.copy(dietary = dietary)
    }

    fun setDifficulty(difficulty: String) {
        _filterState.value = _filterState.value.copy(difficulty = difficulty)
    }

    fun setMaxTime(maxTime: Int) {
        _filterState.value = _filterState.value.copy(maxTime = maxTime)
    }

    fun setSortBy(sortBy: String) {
        _filterState.value = _filterState.value.copy(sortBy = sortBy)
    }

    fun clearFilters() {
        _filterState.value = RecipeFilterState()
    }

    fun toggleFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeRepository.toggleFavorite(recipe.id, recipe.isFavorite)
            val msg = if (!recipe.isFavorite) "Added to Favorites" else "Removed from Favorites"
            _userMessage.emit(msg)
        }
    }

    fun searchExternalRecipesOnline(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _isSearchingExternal.value = true
            val result = recipeRepository.searchExternalRecipes(query)
            _isSearchingExternal.value = false
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _userMessage.emit("Discovered ${list.size} online recipes from TheMealDB!")
                } else {
                    _userMessage.emit("No external recipes found for '$query'.")
                }
            }.onFailure {
                _userMessage.emit("External search unavailable. Browsing offline recipes.")
            }
        }
    }

    fun saveRecipe(recipe: RecipeEntity, onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            recipeRepository.saveRecipe(recipe)
            _userMessage.emit("Recipe saved successfully!")
            onSaved()
        }
    }

    fun deleteRecipe(id: String) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(id)
            _userMessage.emit("Recipe deleted.")
        }
    }

    fun addCustomCategory(name: String, description: String = "", iconName: String = "restaurant") {
        if (name.isBlank()) return
        viewModelScope.launch {
            val cat = CategoryEntity(
                id = "cat_custom_" + System.currentTimeMillis(),
                name = name.trim(),
                description = description.trim(),
                iconName = iconName,
                isCustom = true
            )
            recipeRepository.addCategory(cat)
            _userMessage.emit("Category '$name' created!")
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            recipeRepository.deleteCategory(id)
            _userMessage.emit("Category removed.")
        }
    }

    fun getUserRecipes(userId: String) = recipeRepository.getUserRecipes(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    class Factory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeViewModel(repository) as T
        }
    }
}
