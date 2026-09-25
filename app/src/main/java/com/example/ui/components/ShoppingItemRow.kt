package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
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
import com.example.data.model.ShoppingListItemEntity
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.WarmBorder

@Composable
fun ShoppingItemRow(
    item: ShoppingListItemEntity,
    onTogglePurchased: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTogglePurchased)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (item.isPurchased) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isPurchased,
                onCheckedChange = { onTogglePurchased() },
                colors = CheckboxDefaults.colors(
                    checkedColor = ForestGreen,
                    uncheckedColor = ForestGreen
                ),
                modifier = Modifier
                    .size(24.dp)
                    .testTag("checkbox_${item.id}")
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.isPurchased) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (item.isPurchased) TextDecoration.LineThrough else TextDecoration.None
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val qtyFormatted = if (item.quantity == item.quantity.toLong().toDouble()) {
                        item.quantity.toLong().toString()
                    } else {
                        String.format("%.1f", item.quantity)
                    }
                    val unitStr = if (item.unit.isNotBlank()) "$qtyFormatted ${item.unit}" else qtyFormatted

                    Text(
                        text = unitStr,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = ForestGreen
                    )

                    if (!item.recipeTitle.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "from ${item.recipeTitle}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("delete_item_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete item",
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
