package com.example.data.repository

import com.example.data.local.CategoryDao
import com.example.data.local.RecipeDao
import com.example.data.local.SeedData
import com.example.data.model.CategoryEntity
import com.example.data.model.RecipeEntity
import com.example.data.remote.MealDbApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val categoryDao: CategoryDao,
    private val mealDbApi: MealDbApi
) {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    val allRecipes: Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()
    val favoriteRecipes: Flow<List<RecipeEntity>> = recipeDao.getFavoriteRecipes()
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    fun getUserRecipes(userId: String): Flow<List<RecipeEntity>> = recipeDao.getUserRecipes(userId)

    fun getRecipeById(id: String): Flow<RecipeEntity?> = recipeDao.getRecipeById(id)

    suspend fun getRecipeByIdSync(id: String): RecipeEntity? = recipeDao.getRecipeByIdSync(id)

    fun searchRecipes(
        query: String = "",
        category: String = "All",
        dietary: String = "All",
        difficulty: String = "All",
        maxTime: Int = 0,
        sortBy: String = "newest"
    ): Flow<List<RecipeEntity>> {
        return recipeDao.searchRecipes(query, category, dietary, difficulty, maxTime, sortBy)
    }

    suspend fun initSeedDataIfEmpty() = withContext(Dispatchers.IO) {
        if (categoryDao.getCount() == 0) {
            categoryDao.insertAll(SeedData.defaultCategories)
        }
        if (recipeDao.getRecipeCount() == 0) {
            recipeDao.insertAll(SeedData.getInitialRecipes())
        }
    }

    suspend fun toggleFavorite(recipeId: String, currentStatus: Boolean) = withContext(Dispatchers.IO) {
        recipeDao.updateFavoriteStatus(recipeId, !currentStatus)
    }

    suspend fun saveRecipe(recipe: RecipeEntity) = withContext(Dispatchers.IO) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: RecipeEntity) = withContext(Dispatchers.IO) {
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(id: String) = withContext(Dispatchers.IO) {
        recipeDao.deleteRecipeById(id)
    }

    suspend fun addCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(id: String) = withContext(Dispatchers.IO) {
        categoryDao.deleteCustomCategory(id)
    }

    suspend fun searchExternalRecipes(query: String): Result<List<RecipeEntity>> = withContext(Dispatchers.IO) {
        try {
            val response = mealDbApi.searchMeals(query)
            val recipes = response.meals?.map { it.toRecipeEntity(moshi) } ?: emptyList()
            if (recipes.isNotEmpty()) {
                // Cache into local database
                recipeDao.insertAll(recipes)
            }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
