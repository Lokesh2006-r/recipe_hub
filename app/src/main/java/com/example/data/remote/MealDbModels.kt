package com.example.data.remote

import com.example.data.model.Ingredient
import com.example.data.model.RecipeEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class MealResponse(
    @Json(name = "meals") val meals: List<MealDto>?
)

@JsonClass(generateAdapter = true)
data class MealDto(
    @Json(name = "idMeal") val idMeal: String,
    @Json(name = "strMeal") val strMeal: String?,
    @Json(name = "strCategory") val strCategory: String?,
    @Json(name = "strArea") val strArea: String?,
    @Json(name = "strInstructions") val strInstructions: String?,
    @Json(name = "strMealThumb") val strMealThumb: String?,
    @Json(name = "strIngredient1") val strIngredient1: String? = null,
    @Json(name = "strIngredient2") val strIngredient2: String? = null,
    @Json(name = "strIngredient3") val strIngredient3: String? = null,
    @Json(name = "strIngredient4") val strIngredient4: String? = null,
    @Json(name = "strIngredient5") val strIngredient5: String? = null,
    @Json(name = "strIngredient6") val strIngredient6: String? = null,
    @Json(name = "strIngredient7") val strIngredient7: String? = null,
    @Json(name = "strIngredient8") val strIngredient8: String? = null,
    @Json(name = "strIngredient9") val strIngredient9: String? = null,
    @Json(name = "strIngredient10") val strIngredient10: String? = null,
    @Json(name = "strIngredient11") val strIngredient11: String? = null,
    @Json(name = "strIngredient12") val strIngredient12: String? = null,
    @Json(name = "strIngredient13") val strIngredient13: String? = null,
    @Json(name = "strIngredient14") val strIngredient14: String? = null,
    @Json(name = "strIngredient15") val strIngredient15: String? = null,
    @Json(name = "strMeasure1") val strMeasure1: String? = null,
    @Json(name = "strMeasure2") val strMeasure2: String? = null,
    @Json(name = "strMeasure3") val strMeasure3: String? = null,
    @Json(name = "strMeasure4") val strMeasure4: String? = null,
    @Json(name = "strMeasure5") val strMeasure5: String? = null,
    @Json(name = "strMeasure6") val strMeasure6: String? = null,
    @Json(name = "strMeasure7") val strMeasure7: String? = null,
    @Json(name = "strMeasure8") val strMeasure8: String? = null,
    @Json(name = "strMeasure9") val strMeasure9: String? = null,
    @Json(name = "strMeasure10") val strMeasure10: String? = null,
    @Json(name = "strMeasure11") val strMeasure11: String? = null,
    @Json(name = "strMeasure12") val strMeasure12: String? = null,
    @Json(name = "strMeasure13") val strMeasure13: String? = null,
    @Json(name = "strMeasure14") val strMeasure14: String? = null,
    @Json(name = "strMeasure15") val strMeasure15: String? = null
) {
    fun toRecipeEntity(moshi: Moshi): RecipeEntity {
        val rawIngredients = listOf(
            strIngredient1 to strMeasure1,
            strIngredient2 to strMeasure2,
            strIngredient3 to strMeasure3,
            strIngredient4 to strMeasure4,
            strIngredient5 to strMeasure5,
            strIngredient6 to strMeasure6,
            strIngredient7 to strMeasure7,
            strIngredient8 to strMeasure8,
            strIngredient9 to strMeasure9,
            strIngredient10 to strMeasure10,
            strIngredient11 to strMeasure11,
            strIngredient12 to strMeasure12,
            strIngredient13 to strMeasure13,
            strIngredient14 to strMeasure14,
            strIngredient15 to strMeasure15
        )

        val ingredientsList = rawIngredients.mapNotNull { (name, measure) ->
            if (!name.isNullOrBlank()) {
                val measureClean = measure?.trim() ?: ""
                // Parse numeric quantity if starts with number
                val qtyMatch = Regex("""^([\d/.]+)""").find(measureClean)
                val quantity = qtyMatch?.groupValues?.get(1)?.let {
                    if (it.contains("/")) {
                        val parts = it.split("/")
                        parts[0].toDoubleOrNull()?.div(parts[1].toDoubleOrNull() ?: 1.0) ?: 1.0
                    } else {
                        it.toDoubleOrNull() ?: 1.0
                    }
                } ?: 1.0
                val unit = measureClean.replace(Regex("""^[\d/.\s]+"""), "").trim()
                Ingredient(name = name.trim(), quantity = quantity, unit = unit)
            } else null
        }

        val rawInstructions = strInstructions?.split(Regex("""\r?\n+"""))
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() && it.length > 5 }
            ?: listOf("Prepare ingredients according to recipe guidelines.", "Cook until heated through and serve warm.")

        val ingAdapter = moshi.adapter<List<Ingredient>>(
            Types.newParameterizedType(List::class.java, Ingredient::class.java)
        )
        val instAdapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        )

        val cuisine = strArea ?: "International"
        val isVeg = strCategory?.contains("Vegetarian", ignoreCase = true) == true ||
                strCategory?.contains("Vegan", ignoreCase = true) == true
        val dietary = if (isVeg) "Vegetarian" else "Non-Vegetarian"

        return RecipeEntity(
            id = "ext_$idMeal",
            userId = "themealdb",
            title = strMeal ?: "Unknown Dish",
            description = "Delicious $cuisine recipe sourced from TheMealDB culinary database.",
            imageUrl = strMealThumb ?: "https://images.unsplash.com/photo-1495521821757-a1efb6729352?auto=format&fit=crop&w=800&q=80",
            cuisine = cuisine,
            category = strCategory ?: "Dinner",
            dietaryPreference = dietary,
            difficulty = "Medium",
            prepTimeMinutes = 15,
            cookTimeMinutes = 25,
            servings = 4,
            ingredientsJson = ingAdapter.toJson(ingredientsList),
            instructionsJson = instAdapter.toJson(rawInstructions),
            isPublic = true,
            isFavorite = false,
            isExternal = true
        )
    }
}
