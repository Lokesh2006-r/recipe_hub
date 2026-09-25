package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.OchreAccent
import com.example.ui.theme.WarmBorder

@Composable
fun RecipeCard(
    recipe: RecipeEntity,
    onRecipeClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heartColor by animateColorAsState(
        targetValue = if (recipe.isFavorite) Color(0xFFE53935) else Color.White,
        animationSpec = spring(),
        label = "heartColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onRecipeClick)
            .testTag("recipe_card_${recipe.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Recipe Image with Badge and Favorite Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
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

                // Cuisine and Dietary Badges
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = ForestGreen.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = recipe.cuisine,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (recipe.dietaryPreference.isNotBlank() && recipe.dietaryPreference != "All") {
                        val isVeg = recipe.dietaryPreference.contains("Veg", ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isVeg) Color(0xFF2E7D32).copy(alpha = 0.9f) else Color(0xFFC62828).copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = recipe.dietaryPreference,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Favorite Circle Button
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(38.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                        .testTag("favorite_button_${recipe.id}")
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (recipe.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = heartColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Card Body Details
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stats Row: Total Time, Difficulty, Servings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = "Cooking time",
                            tint = ForestGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${recipe.totalTimeMinutes} mins",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (recipe.difficulty.lowercase()) {
                            "easy" -> Color(0xFFE8F5E9)
                            "hard" -> Color(0xFFFFEBEE)
                            else -> Color(0xFFFFF8E1)
                        }
                    ) {
                        Text(
                            text = recipe.difficulty,
                            color = when (recipe.difficulty.lowercase()) {
                                "easy" -> Color(0xFF2E7D32)
                                "hard" -> Color(0xFFC62828)
                                else -> OchreAccent
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Restaurant,
                            contentDescription = "Servings",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${recipe.servings} serv",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
