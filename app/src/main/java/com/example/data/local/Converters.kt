package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.Ingredient
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val ingredientListType = Types.newParameterizedType(List::class.java, Ingredient::class.java)
    private val ingredientAdapter = moshi.adapter<List<Ingredient>>(ingredientListType)

    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter = moshi.adapter<List<String>>(stringListType)

    @TypeConverter
    fun fromIngredientList(ingredients: List<Ingredient>?): String {
        return ingredientAdapter.toJson(ingredients ?: emptyList())
    }

    @TypeConverter
    fun toIngredientList(json: String?): List<Ingredient> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            ingredientAdapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromStringList(strings: List<String>?): String {
        return stringListAdapter.toJson(strings ?: emptyList())
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            stringListAdapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
