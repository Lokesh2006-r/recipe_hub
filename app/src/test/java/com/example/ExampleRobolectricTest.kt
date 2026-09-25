package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.Converters
import com.example.data.local.SeedData
import com.example.data.model.Ingredient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RecipeHub", appName)
    }

    @Test
    fun `seed data contains authentic recipes`() {
        val recipes = SeedData.getInitialRecipes()
        assertTrue("Seed recipes should have at least 15 items", recipes.size >= 15)
        assertTrue(recipes.any { it.title.contains("Butter Chicken") })
        assertTrue(recipes.any { it.title.contains("Masala Dosa") })
        assertTrue(recipes.any { it.title.contains("Margherita Pizza") })
    }

    @Test
    fun `converters properly serialize and deserialize ingredients`() {
        val converters = Converters()
        val original = listOf(
            Ingredient("Chicken thighs", 500.0, "g"),
            Ingredient("Heavy cream", 1.0, "cup")
        )
        val json = converters.fromIngredientList(original)
        assertNotNull(json)
        val restored = converters.toIngredientList(json)
        assertEquals(2, restored.size)
        assertEquals("Chicken thighs", restored[0].name)
        assertEquals(500.0, restored[0].quantity, 0.001)
        assertEquals("g", restored[0].unit)
    }

    @Test
    fun `ingredient display quantity scales accurately`() {
        val ing = Ingredient("Tomatoes", 2.0, "pcs")
        val scaled = ing.displayQuantity(multiplier = 2.5)
        assertEquals("5 pcs", scaled)
    }

    @Test
    fun `theme repository defaults to system and persists modes`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = com.example.data.repository.ThemeRepository(context)

        // Initial mode should be SYSTEM
        assertEquals(com.example.data.repository.ThemeMode.SYSTEM, repo.themeMode.value)

        // Set to DARK
        repo.setThemeMode(com.example.data.repository.ThemeMode.DARK)
        assertEquals(com.example.data.repository.ThemeMode.DARK, repo.themeMode.value)

        // Toggle from dark -> should switch to LIGHT
        repo.toggleTheme(isCurrentlyDark = true)
        assertEquals(com.example.data.repository.ThemeMode.LIGHT, repo.themeMode.value)

        // Toggle from light -> should switch to DARK
        repo.toggleTheme(isCurrentlyDark = false)
        assertEquals(com.example.data.repository.ThemeMode.DARK, repo.themeMode.value)
    }
}
