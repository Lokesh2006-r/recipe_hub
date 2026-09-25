package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ingredient(
    val name: String,
    val quantity: Double = 1.0,
    val unit: String = ""
) {
    fun displayQuantity(multiplier: Double = 1.0): String {
        val scaled = quantity * multiplier
        val formatted = if (scaled == scaled.toLong().toDouble()) {
            scaled.toLong().toString()
        } else {
            String.format("%.1f", scaled)
        }
        return if (unit.isNotBlank()) "$formatted $unit" else formatted
    }
}

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val userId: String = "system", // "system" or user id
    val title: String,
    val description: String,
    val imageUrl: String,
    val cuisine: String,
    val category: String,
    val dietaryPreference: String = "Vegetarian", // "Vegetarian", "Non-Vegetarian", "Vegan", "Gluten-Free"
    val difficulty: String = "Medium", // "Easy", "Medium", "Hard"
    val prepTimeMinutes: Int = 15,
    val cookTimeMinutes: Int = 30,
    val servings: Int = 4,
    val ingredientsJson: String = "[]",
    val instructionsJson: String = "[]",
    val isPublic: Boolean = true,
    val isFavorite: Boolean = false,
    val isExternal: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes
}

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String = "",
    val iconName: String = "restaurant",
    val isCustom: Boolean = false,
    val userId: String = "system"
)

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey
    val id: String,
    val userId: String = "user_default",
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "shopping_list_items")
data class ShoppingListItemEntity(
    @PrimaryKey
    val id: String,
    val listId: String,
    val name: String,
    val quantity: Double = 1.0,
    val unit: String = "",
    val isPurchased: Boolean = false,
    val recipeTitle: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val passwordHash: String = "",
    val avatarUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
