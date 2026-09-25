package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY createdAt DESC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: String): Flow<RecipeEntity?>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeByIdSync(id: String): RecipeEntity?

    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserRecipes(userId: String): Flow<List<RecipeEntity>>

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getRecipeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(recipes: List<RecipeEntity>)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun deleteRecipeById(id: String)

    @Query("UPDATE recipes SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFav: Boolean)

    @Query("""
        SELECT * FROM recipes 
        WHERE (:query = '' OR title LIKE '%' || :query || '%' OR cuisine LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR ingredientsJson LIKE '%' || :query || '%')
        AND (:category = 'All' OR category = :category)
        AND (:dietary = 'All' OR dietaryPreference = :dietary)
        AND (:difficulty = 'All' OR difficulty = :difficulty)
        AND (:maxTime <= 0 OR (prepTimeMinutes + cookTimeMinutes) <= :maxTime)
        ORDER BY 
            CASE WHEN :sortBy = 'shortest_time' THEN (prepTimeMinutes + cookTimeMinutes) END ASC,
            CASE WHEN :sortBy = 'alphabetical' THEN title END ASC,
            CASE WHEN :sortBy = 'newest' THEN createdAt END DESC
    """)
    fun searchRecipes(
        query: String,
        category: String,
        dietary: String,
        difficulty: String,
        maxTime: Int,
        sortBy: String
    ): Flow<List<RecipeEntity>>
}
