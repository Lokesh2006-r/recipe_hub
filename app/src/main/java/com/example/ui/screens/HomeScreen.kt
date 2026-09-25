package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CategoryEntity
import com.example.data.model.RecipeEntity
import com.example.ui.components.RecipeCard
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.OchreAccent
import com.example.ui.theme.SageGreenLight
import com.example.ui.theme.WarmCream

@Composable
fun HomeScreen(
    featuredRecipes: List<RecipeEntity>,
    quickMeals: List<RecipeEntity>,
    categories: List<CategoryEntity>,
    onRecipeClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onExploreClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
    onFavoriteToggle: (RecipeEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. HERO BANNER SECTION
        item {
            HeroSection(
                onExploreClick = onExploreClick,
                onAddRecipeClick = onAddRecipeClick
            )
        }

        // 2. POPULAR CATEGORIES SECTION
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "Popular Categories",
                subtitle = "Browse dishes by meal type & dietary style",
                onSeeAllClick = onExploreClick
            )
            Spacer(modifier = Modifier.height(10.dp))
            CategoriesHorizontalList(
                categories = categories,
                onCategoryClick = onCategoryClick
            )
        }

        // 3. FEATURED RECIPES SECTION
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "Featured Recipes",
                subtitle = "Handpicked seasonal dishes to cook today",
                onSeeAllClick = onExploreClick
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(featuredRecipes) { recipe ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                RecipeCard(
                    recipe = recipe,
                    onRecipeClick = { onRecipeClick(recipe.id) },
                    onFavoriteToggle = { onFavoriteToggle(recipe) }
                )
            }
        }

        // 4. QUICK MEALS UNDER 30 MINS SECTION
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "Quick Meals Under 30 Mins",
                subtitle = "Fast, nutritious & delicious for busy days",
                onSeeAllClick = onExploreClick
            )
            Spacer(modifier = Modifier.height(10.dp))
            QuickMealsHorizontalList(
                recipes = quickMeals,
                onRecipeClick = onRecipeClick,
                onFavoriteToggle = onFavoriteToggle
            )
        }

        // 5. CALL TO ACTION BANNER
        item {
            Spacer(modifier = Modifier.height(24.dp))
            CtaBanner(
                onAddRecipeClick = onAddRecipeClick
            )
        }
    }
}

@Composable
private fun HeroSection(
    onExploreClick: () -> Unit,
    onAddRecipeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Recipe culinary spread",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay for pristine text contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                ForestGreenDark.copy(alpha = 0.85f),
                                ForestGreenDark.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Text & Actions
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = OchreAccent
                ) {
                    Text(
                        text = "RECIPEHUB KITCHEN",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Discover Your Next Favorite Recipe",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Explore 1,000+ curated dishes, organize favorites, and create smart shopping lists.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onExploreClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("hero_explore_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = ForestGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Explore Recipes",
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onAddRecipeClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("hero_add_recipe_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Add Recipe",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier
                .clickable(onClick = onSeeAllClick)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "See All",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreen
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "See all",
                tint = ForestGreen,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun CategoriesHorizontalList(
    categories: List<CategoryEntity>,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { cat ->
            CategoryPill(category = cat, onClick = { onCategoryClick(cat.name) })
        }
    }
}

@Composable
private fun CategoryPill(
    category: CategoryEntity,
    onClick: () -> Unit
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

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("category_pill_${category.name}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(ForestGreen.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category.name,
                    tint = ForestGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun QuickMealsHorizontalList(
    recipes: List<RecipeEntity>,
    onRecipeClick: (String) -> Unit,
    onFavoriteToggle: (RecipeEntity) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(recipes) { recipe ->
            Box(modifier = Modifier.width(280.dp)) {
                RecipeCard(
                    recipe = recipe,
                    onRecipeClick = { onRecipeClick(recipe.id) },
                    onFavoriteToggle = { onFavoriteToggle(recipe) }
                )
            }
        }
    }
}

@Composable
private fun CtaBanner(
    onAddRecipeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Have a signature family recipe?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Share your culinary creations with ingredients and step-by-step instructions.",
                style = MaterialTheme.typography.bodySmall,
                color = ForestGreenDark.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onAddRecipeClick,
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("cta_add_recipe_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Your Recipe Now")
            }
        }
    }
}
