package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ForestGreen

@Composable
fun SearchBarRow(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onFilterClick: () -> Unit,
    hasActiveFilters: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .testTag("recipe_search_input"),
            placeholder = {
                Text("Search dishes, ingredients, cuisines...", style = MaterialTheme.typography.bodyMedium)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    tint = ForestGreen
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onQueryChange("")
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.testTag("clear_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = ForestGreen,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchSubmit(query)
                focusManager.clearFocus()
            })
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Filter button with optional active badge
        Surface(
            shape = CircleShape,
            color = if (hasActiveFilters) ForestGreen else MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            shadowElevation = 1.dp,
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onFilterClick)
                .testTag("filter_button")
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Open filters",
                    tint = if (hasActiveFilters) Color.White else ForestGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
