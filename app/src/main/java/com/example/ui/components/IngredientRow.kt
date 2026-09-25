package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Ingredient
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmBorder

@Composable
fun IngredientRow(
    ingredient: Ingredient,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onAddToShoppingList: () -> Unit,
    multiplier: Double = 1.0,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        color = if (isChecked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = ForestGreen,
                    uncheckedColor = ForestGreen
                ),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            ColumnText(
                ingredient = ingredient,
                multiplier = multiplier,
                isChecked = isChecked,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onAddToShoppingList,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("add_ingredient_to_cart_${ingredient.name}")
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Add ${ingredient.name} to shopping list",
                    tint = ForestGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ColumnText(
    ingredient: Ingredient,
    multiplier: Double,
    isChecked: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.weight(1f, fill = false)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = ForestGreen.copy(alpha = 0.1f)
        ) {
            Text(
                text = ingredient.displayQuantity(multiplier),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}
