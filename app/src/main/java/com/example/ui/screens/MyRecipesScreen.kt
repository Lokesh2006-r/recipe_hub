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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.RecipeEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmCream

@Composable
fun MyRecipesScreen(
    myRecipes: List<RecipeEntity>,
    onRecipeClick: (String) -> Unit,
    onAddRecipeClick: () -> Unit,
    onEditRecipeClick: (RecipeEntity) -> Unit,
    onDeleteRecipeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var recipeToDelete by remember { mutableStateOf<RecipeEntity?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecipeClick,
                containerColor = ForestGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .testTag("my_recipes_fab_add")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Recipe")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        text = "My Personal Recipes",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${myRecipes.size} recipes created by you",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onAddRecipeClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("create_recipe_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Recipe", fontSize = 12.sp)
                }
            }

            if (myRecipes.isEmpty()) {
                EmptyStateView(
                    title = "No Custom Recipes Yet",
                    subtitle = "Create and organize your secret family recipes, custom notes, and cooking directions.",
                    icon = Icons.Outlined.MenuBook,
                    actionButtonText = "Create Your First Recipe",
                    onActionClick = onAddRecipeClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(myRecipes, key = { it.id }) { recipe ->
                        MyRecipeListItem(
                            recipe = recipe,
                            onRecipeClick = { onRecipeClick(recipe.id) },
                            onEditClick = { onEditRecipeClick(recipe) },
                            onDeleteClick = { recipeToDelete = recipe }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (recipeToDelete != null) {
        AlertDialog(
            onDismissRequest = { recipeToDelete = null },
            title = { Text("Delete Recipe?") },
            text = { Text("Are you sure you want to delete '${recipeToDelete?.title}'? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        recipeToDelete?.let { onDeleteRecipeClick(it.id) }
                        recipeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { recipeToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun MyRecipeListItem(
    recipe: RecipeEntity,
    onRecipeClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onRecipeClick)
            .testTag("my_recipe_card_${recipe.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.imageUrl.ifBlank { "https://images.unsplash.com/photo-1495521821757-a1efb6729352?auto=format&fit=crop&w=800&q=80" })
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ForestGreen.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = recipe.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "${recipe.totalTimeMinutes}m",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Edit & Delete Actions
            Row {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.testTag("edit_recipe_${recipe.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit recipe",
                        tint = ForestGreen
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.testTag("delete_recipe_${recipe.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete recipe",
                        tint = Color(0xFFC62828)
                    )
                }
            }
        }
    }
}
