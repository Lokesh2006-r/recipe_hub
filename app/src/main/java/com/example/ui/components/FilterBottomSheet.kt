package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CategoryEntity
import com.example.ui.theme.ForestGreen
import com.example.ui.viewmodel.RecipeFilterState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    categories: List<CategoryEntity>,
    currentFilter: RecipeFilterState,
    onApplyFilter: (RecipeFilterState) -> Unit,
    onResetFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(currentFilter.category) }
    var selectedDietary by remember { mutableStateOf(currentFilter.dietary) }
    var selectedDifficulty by remember { mutableStateOf(currentFilter.difficulty) }
    var selectedMaxTime by remember { mutableIntStateOf(currentFilter.maxTime) }
    var selectedSortBy by remember { mutableStateOf(currentFilter.sortBy) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Recipes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close filter sheet")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sort By
            Text("Sort By", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val sortOptions = listOf(
                    "newest" to "Newest Added",
                    "shortest_time" to "Shortest Cooking Time",
                    "alphabetical" to "A - Z"
                )
                sortOptions.forEach { (key, label) ->
                    FilterChip(
                        selected = selectedSortBy == key,
                        onClick = { selectedSortBy = key },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category
            Text("Category", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedCategory == "All",
                    onClick = { selectedCategory = "All" },
                    label = { Text("All Categories") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreen,
                        selectedLabelColor = androidx.compose.ui.graphics.Color.White
                    )
                )
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat.name,
                        onClick = { selectedCategory = cat.name },
                        label = { Text(cat.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dietary Preference
            Text("Dietary Preference", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val diets = listOf("All", "Vegetarian", "Non-Vegetarian", "Vegan")
                diets.forEach { diet ->
                    FilterChip(
                        selected = selectedDietary == diet,
                        onClick = { selectedDietary = diet },
                        label = { Text(diet) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty
            Text("Difficulty", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val diffs = listOf("All", "Easy", "Medium", "Hard")
                diffs.forEach { diff ->
                    FilterChip(
                        selected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        label = { Text(diff) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Max Cooking Time
            Text("Max Cooking Time", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val times = listOf(0 to "Any time", 15 to "< 15 mins", 30 to "< 30 mins", 45 to "< 45 mins", 60 to "< 60 mins")
                times.forEach { (mins, label) ->
                    FilterChip(
                        selected = selectedMaxTime == mins,
                        onClick = { selectedMaxTime = mins },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        selectedCategory = "All"
                        selectedDietary = "All"
                        selectedDifficulty = "All"
                        selectedMaxTime = 0
                        selectedSortBy = "newest"
                        onResetFilters()
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("reset_filters_button")
                ) {
                    Text("Clear All")
                }

                Button(
                    onClick = {
                        onApplyFilter(
                            RecipeFilterState(
                                query = currentFilter.query,
                                category = selectedCategory,
                                dietary = selectedDietary,
                                difficulty = selectedDifficulty,
                                maxTime = selectedMaxTime,
                                sortBy = selectedSortBy
                            )
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("apply_filters_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Apply")
                }
            }
        }
    }
}
