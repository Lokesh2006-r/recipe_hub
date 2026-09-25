package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.Converters
import com.example.data.model.CategoryEntity
import com.example.data.model.Ingredient
import com.example.data.model.RecipeEntity
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmCream
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    initialRecipe: RecipeEntity?,
    categories: List<CategoryEntity>,
    currentUserId: String,
    onBackClick: () -> Unit,
    onSaveRecipe: (RecipeEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val converters = remember { Converters() }

    var title by remember { mutableStateOf(initialRecipe?.title ?: "") }
    var description by remember { mutableStateOf(initialRecipe?.description ?: "") }
    var imageUrl by remember { mutableStateOf(initialRecipe?.imageUrl ?: "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=800&q=80") }
    var cuisine by remember { mutableStateOf(initialRecipe?.cuisine ?: "Indian") }
    var category by remember { mutableStateOf(initialRecipe?.category ?: "Dinner") }
    var dietary by remember { mutableStateOf(initialRecipe?.dietaryPreference ?: "Vegetarian") }
    var difficulty by remember { mutableStateOf(initialRecipe?.difficulty ?: "Medium") }
    var prepTime by remember { mutableStateOf(initialRecipe?.prepTimeMinutes?.toString() ?: "15") }
    var cookTime by remember { mutableStateOf(initialRecipe?.cookTimeMinutes?.toString() ?: "20") }
    var servings by remember { mutableStateOf(initialRecipe?.servings?.toString() ?: "4") }

    val ingredients = remember {
        mutableStateListOf<Ingredient>().apply {
            if (initialRecipe != null) {
                addAll(converters.toIngredientList(initialRecipe.ingredientsJson))
            } else {
                add(Ingredient("Flour / Rice / Pasta", 2.0, "cups"))
                add(Ingredient("Olive oil / Butter", 2.0, "tbsp"))
                add(Ingredient("Salt & Seasoning", 1.0, "tsp"))
            }
        }
    }

    val instructions = remember {
        mutableStateListOf<String>().apply {
            if (initialRecipe != null) {
                addAll(converters.toStringList(initialRecipe.instructionsJson))
            } else {
                add("Prep and measure all fresh ingredients according to list.")
                add("Heat pan or oven and cook following medium temperature.")
                add("Garnish with fresh herbs and serve warm.")
            }
        }
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialRecipe == null) "Create Recipe" else "Edit Recipe",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("add_recipe_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = "Recipe title cannot be empty."
                                return@Button
                            }
                            if (ingredients.isEmpty() || ingredients.all { it.name.isBlank() }) {
                                errorMessage = "Please add at least one valid ingredient."
                                return@Button
                            }
                            if (instructions.isEmpty() || instructions.all { it.isBlank() }) {
                                errorMessage = "Please add at least one instruction step."
                                return@Button
                            }

                            val cleanIngredients = ingredients.filter { it.name.isNotBlank() }
                            val cleanInstructions = instructions.filter { it.isNotBlank() }

                            val recipe = RecipeEntity(
                                id = initialRecipe?.id ?: ("user_rec_" + UUID.randomUUID().toString().take(8)),
                                userId = initialRecipe?.userId ?: currentUserId,
                                title = title.trim(),
                                description = description.trim().ifBlank { "Delicious homemade recipe." },
                                imageUrl = imageUrl.trim(),
                                cuisine = cuisine.trim().ifBlank { "General" },
                                category = category,
                                dietaryPreference = dietary,
                                difficulty = difficulty,
                                prepTimeMinutes = prepTime.toIntOrNull() ?: 15,
                                cookTimeMinutes = cookTime.toIntOrNull() ?: 20,
                                servings = servings.toIntOrNull() ?: 4,
                                ingredientsJson = converters.fromIngredientList(cleanIngredients),
                                instructionsJson = converters.fromStringList(cleanInstructions),
                                isFavorite = initialRecipe?.isFavorite ?: false,
                                isPublic = true
                            )
                            onSaveRecipe(recipe)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("save_recipe_button")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error banner
            if (errorMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFFEBEE),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF9A9A))
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = Color(0xFFC62828),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            // Image Preview & Presets
            item {
                Text("Recipe Photo Preview", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Quick Food Photo Presets:", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val presets = listOf(
                        "Fresh Salad" to "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=800&q=80",
                        "Hearty Curry" to "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?auto=format&fit=crop&w=800&q=80",
                        "Artisan Pizza" to "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&w=800&q=80",
                        "Dessert Cake" to "https://images.unsplash.com/photo-1578985545062-69928b1d9587?auto=format&fit=crop&w=800&q=80",
                        "Pancakes" to "https://images.unsplash.com/photo-1528207776546-365bb710ee93?auto=format&fit=crop&w=800&q=80"
                    )
                    items(presets) { (name, url) ->
                        FilterChip(
                            selected = imageUrl == url,
                            onClick = { imageUrl = url },
                            label = { Text(name, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recipe_image_url_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Basic Info: Title & Description
            item {
                Text("Basic Details", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMessage = null },
                    label = { Text("Recipe Title *") },
                    placeholder = { Text("e.g. Creamy Tuscan Garlic Chicken") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recipe_title_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Short Story") },
                    placeholder = { Text("Tell readers what makes this dish special...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recipe_desc_input"),
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Cuisine, Category, Dietary
            item {
                Text("Classification", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = cuisine,
                        onValueChange = { cuisine = it },
                        label = { Text("Cuisine") },
                        placeholder = { Text("Indian, Italian, etc.") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("recipe_cuisine_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        placeholder = { Text("Dinner, Lunch...") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("recipe_category_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Dietary Preference", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val dietOptions = listOf("Vegetarian", "Non-Vegetarian", "Vegan", "Gluten-Free")
                    dietOptions.forEach { d ->
                        FilterChip(
                            selected = dietary == d,
                            onClick = { dietary = d },
                            label = { Text(d) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Prep Time, Cook Time, Servings, Difficulty
            item {
                Text("Cooking Info", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = prepTime,
                        onValueChange = { prepTime = it },
                        label = { Text("Prep (mins)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = cookTime,
                        onValueChange = { cookTime = it },
                        label = { Text("Cook (mins)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = servings,
                        onValueChange = { servings = it },
                        label = { Text("Servings") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Difficulty", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Easy", "Medium", "Hard").forEach { diff ->
                        FilterChip(
                            selected = difficulty == diff,
                            onClick = { difficulty = diff },
                            label = { Text(diff) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Dynamic Ingredients
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ingredients (${ingredients.size})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    OutlinedButton(
                        onClick = { ingredients.add(Ingredient("", 1.0, "pcs")) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("add_ingredient_row_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontSize = 12.sp)
                    }
                }
            }

            itemsIndexed(ingredients) { index, ing ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ing.name,
                            onValueChange = { newName ->
                                ingredients[index] = ing.copy(name = newName)
                            },
                            placeholder = { Text("Ingredient name") },
                            modifier = Modifier.weight(2f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = if (ing.quantity == 0.0) "" else ing.quantity.toString(),
                            onValueChange = { newQty ->
                                val d = newQty.toDoubleOrNull() ?: 0.0
                                ingredients[index] = ing.copy(quantity = d)
                            },
                            placeholder = { Text("Qty") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = ing.unit,
                            onValueChange = { newUnit ->
                                ingredients[index] = ing.copy(unit = newUnit)
                            },
                            placeholder = { Text("Unit") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        IconButton(
                            onClick = { if (ingredients.size > 1) ingredients.removeAt(index) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
                        }
                    }
                }
            }

            // Dynamic Instructions
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Instructions (${instructions.size} Steps)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    OutlinedButton(
                        onClick = { instructions.add("") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("add_instruction_step_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Step", fontSize = 12.sp)
                    }
                }
            }

            itemsIndexed(instructions) { index, step ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(ForestGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        OutlinedTextField(
                            value = step,
                            onValueChange = { newStep -> instructions[index] = newStep },
                            placeholder = { Text("Describe this step...") },
                            modifier = Modifier.weight(1f),
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(8.dp)
                        )

                        IconButton(
                            onClick = { if (instructions.size > 1) instructions.removeAt(index) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        // Submit
                        if (title.isBlank()) {
                            errorMessage = "Recipe title cannot be empty."
                            return@Button
                        }
                        val cleanIngredients = ingredients.filter { it.name.isNotBlank() }
                        val cleanInstructions = instructions.filter { it.isNotBlank() }

                        val recipe = RecipeEntity(
                            id = initialRecipe?.id ?: ("user_rec_" + UUID.randomUUID().toString().take(8)),
                            userId = initialRecipe?.userId ?: currentUserId,
                            title = title.trim(),
                            description = description.trim().ifBlank { "Delicious homemade recipe." },
                            imageUrl = imageUrl.trim(),
                            cuisine = cuisine.trim().ifBlank { "General" },
                            category = category,
                            dietaryPreference = dietary,
                            difficulty = difficulty,
                            prepTimeMinutes = prepTime.toIntOrNull() ?: 15,
                            cookTimeMinutes = cookTime.toIntOrNull() ?: 20,
                            servings = servings.toIntOrNull() ?: 4,
                            ingredientsJson = converters.fromIngredientList(cleanIngredients),
                            instructionsJson = converters.fromStringList(cleanInstructions),
                            isFavorite = initialRecipe?.isFavorite ?: false,
                            isPublic = true
                        )
                        onSaveRecipe(recipe)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_recipe_bottom_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (initialRecipe == null) "Create & Publish Recipe" else "Update Recipe",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
