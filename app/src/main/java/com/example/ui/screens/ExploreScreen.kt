package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.RecipeEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.RecipeCard
import com.example.ui.components.SearchBarRow
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmCream
import com.example.ui.viewmodel.RecipeFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    recipes: List<RecipeEntity>,
    categories: List<CategoryEntity>,
    filterState: RecipeFilterState,
    isSearchingExternal: Boolean,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onApplyFilter: (RecipeFilterState) -> Unit,
    onResetFilters: () -> Unit,
    onSearchExternal: (String) -> Unit,
    onRecipeClick: (String) -> Unit,
    onFavoriteToggle: (RecipeEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val hasActiveFilters = filterState.category != "All" ||
            filterState.dietary != "All" ||
            filterState.difficulty != "All" ||
            filterState.maxTime > 0 ||
            filterState.sortBy != "newest"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search & Filter Trigger Bar
        SearchBarRow(
            query = filterState.query,
            onQueryChange = onQueryChange,
            onSearchSubmit = { onSearchExternal(it) },
            onFilterClick = { showFilterSheet = true },
            hasActiveFilters = hasActiveFilters
        )

        // Category Horizontal Fast Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filterState.category == "All",
                    onClick = { onCategoryChange("All") },
                    label = { Text("All Dishes") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreen,
                        selectedLabelColor = Color.White
                    )
                )
            }
            items(categories) { cat ->
                FilterChip(
                    selected = filterState.category == cat.name,
                    onClick = { onCategoryChange(cat.name) },
                    label = { Text(cat.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreen,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Active filter pills row (if any)
        if (hasActiveFilters) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Showing ${recipes.size} filtered recipes",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Clear All",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen,
                    modifier = Modifier
                        .clickable(onClick = onResetFilters)
                        .padding(4.dp)
                )
            }
        }

        // External TheMealDB Search Trigger Banner
        if (filterState.query.isNotBlank()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                color = ForestGreen.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Can't find what you need?",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        Text(
                            text = "Search TheMealDB online database",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (isSearchingExternal) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = ForestGreen,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Button(
                            onClick = { onSearchExternal(filterState.query) },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("search_external_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Fetch Online", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Recipes Grid
        if (recipes.isEmpty()) {
            EmptyStateView(
                title = "No Recipes Found",
                subtitle = "Try adjusting your search terms or clearing your category and time filters.",
                icon = Icons.Default.SearchOff,
                actionButtonText = "Reset Filters",
                onActionClick = onResetFilters,
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
                items(recipes, key = { it.id }) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onRecipeClick = { onRecipeClick(recipe.id) },
                        onFavoriteToggle = { onFavoriteToggle(recipe) }
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            sheetState = sheetState,
            categories = categories,
            currentFilter = filterState,
            onApplyFilter = onApplyFilter,
            onResetFilters = onResetFilters,
            onDismiss = { showFilterSheet = false }
        )
    }
}
