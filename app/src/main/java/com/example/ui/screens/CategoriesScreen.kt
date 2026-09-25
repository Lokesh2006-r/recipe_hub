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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmCream

@Composable
fun CategoriesScreen(
    categories: List<CategoryEntity>,
    onCategoryClick: (String) -> Unit,
    onAddCategory: (String, String) -> Unit,
    onDeleteCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newCatName by remember { mutableStateOf("") }
    var newCatDesc by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Recipe Categories",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Organize dishes by culinary group",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = {
                    newCatName = ""
                    newCatDesc = ""
                    showAddDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("add_category_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add", fontSize = 12.sp)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(categories, key = { it.id }) { cat ->
                CategoryGridItem(
                    category = cat,
                    onClick = { onCategoryClick(cat.name) },
                    onDelete = if (cat.isCustom) { { onDeleteCategory(cat.id) } } else null
                )
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Custom Category") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCatName,
                        onValueChange = { newCatName = it },
                        label = { Text("Category Name *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("custom_cat_name_input")
                    )
                    OutlinedTextField(
                        value = newCatDesc,
                        onValueChange = { newCatDesc = it },
                        label = { Text("Short Description") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCatName.isNotBlank()) {
                            onAddCategory(newCatName, newCatDesc)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Add Category")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CategoryGridItem(
    category: CategoryEntity,
    onClick: () -> Unit,
    onDelete: (() -> Unit)?
) {
    val icon: ImageVector = when (category.name.lowercase()) {
        "breakfast" -> Icons.Default.BreakfastDining
        "lunch" -> Icons.Default.LunchDining
        "dinner" -> Icons.Default.DinnerDining
        "desserts" -> Icons.Default.Cake
        "snacks" -> Icons.Default.Cookie
        "vegetarian" -> Icons.Default.Eco
        "beverages" -> Icons.Default.LocalCafe
        else -> Icons.Default.Restaurant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("category_grid_card_${category.name}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(ForestGreen.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(24.dp))
                }

                if (onDelete != null) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp).testTag("delete_category_${category.id}")
                    ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = category.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}
