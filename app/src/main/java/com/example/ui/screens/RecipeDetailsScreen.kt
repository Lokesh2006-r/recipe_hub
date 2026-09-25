package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.Converters
import com.example.data.model.Ingredient
import com.example.data.model.RecipeEntity
import com.example.ui.components.IngredientRow
import com.example.ui.components.InstructionStepRow
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.OchreAccent
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmCream

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeEntity?,
    onBackClick: () -> Unit,
    onFavoriteToggle: (RecipeEntity) -> Unit,
    onAddAllIngredientsToShoppingList: (List<Ingredient>, Double) -> Unit,
    onAddSingleIngredientToShoppingList: (Ingredient, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    if (recipe == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Recipe not found")
        }
        return
    }

    val converters = remember { Converters() }
    val ingredients = remember(recipe.ingredientsJson) {
        converters.toIngredientList(recipe.ingredientsJson)
    }
    val instructions = remember(recipe.instructionsJson) {
        converters.toStringList(recipe.instructionsJson)
    }

    // Servings scaling
    var currentServings by remember { mutableIntStateOf(if (recipe.servings > 0) recipe.servings else 4) }
    val multiplier = if (recipe.servings > 0) currentServings.toDouble() / recipe.servings.toDouble() else 1.0

    // Interactive ingredient checkboxes while cooking
    val checkedIngredients = remember { mutableStateMapOf<String, Boolean>() }
    // Interactive step completion checkboxes
    val completedSteps = remember { mutableStateMapOf<Int, Boolean>() }

    var showPrintDialog by remember { mutableStateOf(false) }

    val heartColor by animateColorAsState(
        targetValue = if (recipe.isFavorite) Color(0xFFE53935) else Color.White,
        animationSpec = spring(),
        label = "heartDetail"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. HERO IMAGE WITH TOP BAR OVERLAY
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipe.imageUrl.ifBlank { "https://images.unsplash.com/photo-1495521821757-a1efb6729352?auto=format&fit=crop&w=800&q=80" })
                        .crossfade(true)
                        .build(),
                    contentDescription = recipe.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top gradient scrim
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                            )
                        )
                )

                // Top Bar Icons: Back, Print/Share, Favorite
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                            .testTag("recipe_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        IconButton(
                            onClick = { showPrintDialog = true },
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                                .testTag("recipe_detail_print_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = "Print recipe view",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = { onFavoriteToggle(recipe) },
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                                .testTag("recipe_detail_favorite_button")
                        ) {
                            Icon(
                                imageVector = if (recipe.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Toggle favorite",
                                tint = heartColor
                            )
                        }
                    }
                }
            }
        }

        // 2. RECIPE HEADER & METADATA
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = ForestGreen
                    ) {
                        Text(
                            text = recipe.cuisine,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = ForestGreen.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = recipe.category,
                            color = ForestGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (recipe.dietaryPreference.isNotBlank() && recipe.dietaryPreference != "All") {
                        val isVeg = recipe.dietaryPreference.contains("Veg", ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isVeg) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = recipe.dietaryPreference,
                                color = if (isVeg) Color(0xFF2E7D32) else Color(0xFFC62828),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Cards Row: Prep Time, Cook Time, Total Time, Difficulty
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatBox(
                        title = "Prep",
                        value = "${recipe.prepTimeMinutes}m",
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        title = "Cook",
                        value = "${recipe.cookTimeMinutes}m",
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        title = "Total",
                        value = "${recipe.totalTimeMinutes}m",
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        title = "Level",
                        value = recipe.difficulty,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. INTERACTIVE SERVINGS SCALER
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Serving Size Scaler",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Ingredients auto-adjust proportionally",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (currentServings > 1) currentServings--
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ForestGreen.copy(alpha = 0.1f), CircleShape)
                                    .testTag("servings_decrease_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease servings",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Text(
                                text = "$currentServings servings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ForestGreen
                            )

                            IconButton(
                                onClick = { currentServings++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ForestGreen.copy(alpha = 0.1f), CircleShape)
                                    .testTag("servings_increase_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase servings",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 4. INGREDIENTS SECTION HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ingredients (${ingredients.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Check items off while prepping",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { onAddAllIngredientsToShoppingList(ingredients, multiplier) },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("add_all_to_shopping_list_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Ingredients Checklist
        itemsIndexed(ingredients) { index, ing ->
            val isChecked = checkedIngredients[ing.name] ?: false
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                IngredientRow(
                    ingredient = ing,
                    isChecked = isChecked,
                    onCheckedChange = { checkedIngredients[ing.name] = it },
                    onAddToShoppingList = { onAddSingleIngredientToShoppingList(ing, multiplier) },
                    multiplier = multiplier
                )
            }
        }

        // 5. INSTRUCTIONS SECTION HEADER
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Cooking Instructions (${instructions.size} Steps)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap any step to mark as finished",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Instructions Steps
        itemsIndexed(instructions) { index, step ->
            val stepNumber = index + 1
            val isDone = completedSteps[stepNumber] ?: false
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                InstructionStepRow(
                    stepNumber = stepNumber,
                    instruction = step,
                    isCompleted = isDone,
                    onToggleCompleted = { completedSteps[stepNumber] = !isDone }
                )
            }
        }
    }

    // Print / Cook Mode Dialog
    if (showPrintDialog) {
        AlertDialog(
            onDismissRequest = { showPrintDialog = false },
            title = {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(modifier = Modifier.height(350.dp)) {
                    item {
                        Text(
                            text = "Servings: $currentServings  |  Prep: ${recipe.prepTimeMinutes}m  |  Cook: ${recipe.cookTimeMinutes}m",
                            style = MaterialTheme.typography.labelMedium,
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "INGREDIENTS:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        ingredients.forEach {
                            Text("• ${it.name}: ${it.displayQuantity(multiplier)}", style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "INSTRUCTIONS:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        instructions.forEachIndexed { i, s ->
                            Text("${i + 1}. $s", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrintDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun StatBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark,
                fontSize = 13.sp
            )
        }
    }
}
