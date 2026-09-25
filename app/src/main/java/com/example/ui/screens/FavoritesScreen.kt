package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CategoryEntity
import com.example.data.model.RecipeEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.RecipeCard
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmCream

@Composable
fun FavoritesScreen(
    favoriteRecipes: List<RecipeEntity>,
    categories: List<CategoryEntity>,
    onRecipeClick: (String) -> Unit,
    onFavoriteToggle: (RecipeEntity) -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredFavorites = favoriteRecipes.filter { recipe ->
        val matchesQuery = searchQuery.isBlank() ||
                recipe.title.contains(searchQuery, ignoreCase = true) ||
                recipe.cuisine.contains(searchQuery, ignoreCase = true)
        val matchesCat = selectedCategory == "All" || recipe.category.equals(selectedCategory, ignoreCase = true)
        matchesQuery && matchesCat
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Saved Favorites",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "${favoriteRecipes.size} recipes saved for quick access",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search within favorites
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("favorites_search_input"),
                placeholder = { Text("Search your saved favorites...", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = ForestGreen)
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = ForestGreen
                ),
                singleLine = true
            )
        }

        // Category Fast Filters
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == "All",
                    onClick = { selectedCategory = "All" },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreen,
                        selectedLabelColor = Color.White
                    )
                )
            }
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat.name,
                    onClick = { selectedCategory = cat.name },
                    label = { Text(cat.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreen,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredFavorites.isEmpty()) {
            EmptyStateView(
                title = if (favoriteRecipes.isEmpty()) "No Favorites Yet" else "No Matching Favorites",
                subtitle = if (favoriteRecipes.isEmpty())
                    "Tap the heart icon on any recipe to save it to your personal favorites cookbook."
                else "No saved recipes match '$searchQuery'.",
                icon = Icons.Default.Favorite,
                actionButtonText = if (favoriteRecipes.isEmpty()) "Explore Recipes" else "Clear Search",
                onActionClick = if (favoriteRecipes.isEmpty()) onExploreClick else { { searchQuery = ""; selectedCategory = "All" } },
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredFavorites, key = { it.id }) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onRecipeClick = { onRecipeClick(recipe.id) },
                        onFavoriteToggle = { onFavoriteToggle(recipe) }
                    )
                }
            }
        }
    }
}
