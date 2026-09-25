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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCartCheckout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShoppingListEntity
import com.example.data.model.ShoppingListItemEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.ShoppingItemRow
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.OchreAccent
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmCream

@Composable
fun ShoppingListScreen(
    lists: List<ShoppingListEntity>,
    selectedListId: String?,
    items: List<ShoppingListItemEntity>,
    onSelectList: (String) -> Unit,
    onCreateList: (String) -> Unit,
    onRenameList: (String) -> Unit,
    onDeleteList: () -> Unit,
    onAddItem: (String, Double, String) -> Unit,
    onTogglePurchased: (ShoppingListItemEntity) -> Unit,
    onDeleteItem: (ShoppingListItemEntity) -> Unit,
    onClearCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    var newListName by remember { mutableStateOf("") }
    var renameListName by remember { mutableStateOf("") }

    // Quick Add Item Bar states
    var newItemName by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("1") }
    var newItemUnit by remember { mutableStateOf("pcs") }

    val currentList = lists.firstOrNull { it.id == selectedListId } ?: lists.firstOrNull()
    val purchasedCount = items.count { it.isPurchased }
    val totalCount = items.size
    val progress = if (totalCount > 0) purchasedCount.toFloat() / totalCount.toFloat() else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Smart Shopping Lists",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Consolidated groceries auto-merged by ingredient",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }, modifier = Modifier.testTag("list_options_menu")) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Rename List") },
                        onClick = {
                            showMenu = false
                            renameListName = currentList?.name ?: ""
                            showRenameDialog = true
                        },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete List") },
                        onClick = {
                            showMenu = false
                            showDeleteConfirmDialog = true
                        },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFC62828)) }
                    )
                    DropdownMenuItem(
                        text = { Text("Clear Completed Items") },
                        onClick = {
                            showMenu = false
                            onClearCompleted()
                        }
                    )
                }
            }
        }

        // List Selector Pills & "+ New List"
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lists) { list ->
                FilterChip(
                    selected = list.id == selectedListId,
                    onClick = { onSelectList(list.id) },
                    label = { Text(list.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ForestGreen,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("shopping_list_pill_${list.name}")
                )
            }

            item {
                OutlinedButton(
                    onClick = {
                        newListName = ""
                        showCreateDialog = true
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(32.dp).testTag("create_new_list_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New List", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentList?.name ?: "Grocery List",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$purchasedCount / $totalCount bought",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = ForestGreen,
                    trackColor = ForestGreen.copy(alpha = 0.15f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Add Item Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    placeholder = { Text("Add item...", style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier
                        .weight(2.5f)
                        .height(48.dp)
                        .testTag("new_item_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = newItemQty,
                    onValueChange = { newItemQty = it },
                    placeholder = { Text("Qty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("new_item_qty_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = newItemUnit,
                    onValueChange = { newItemUnit = it },
                    placeholder = { Text("Unit") },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(48.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            val qty = newItemQty.toDoubleOrNull() ?: 1.0
                            onAddItem(newItemName, qty, newItemUnit)
                            newItemName = ""
                            newItemQty = "1"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("add_grocery_item_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Items List or Empty State
        if (items.isEmpty()) {
            EmptyStateView(
                title = "Shopping List is Empty",
                subtitle = "Add items using the bar above or tap 'Add All' on any recipe page to automatically populate this list.",
                icon = Icons.Outlined.ShoppingCartCheckout,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    ShoppingItemRow(
                        item = item,
                        onTogglePurchased = { onTogglePurchased(item) },
                        onDelete = { onDeleteItem(item) }
                    )
                }

                if (purchasedCount > 0) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = onClearCompleted,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("clear_completed_button")
                        ) {
                            Text("Clear $purchasedCount Completed Items")
                        }
                    }
                }
            }
        }
    }

    // Create New List Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create Shopping List") },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("List Name") },
                    placeholder = { Text("e.g. Italian Feast Groceries") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("new_list_name_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newListName.isNotBlank()) {
                            onCreateList(newListName)
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.testTag("confirm_create_list_button")
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Rename List Dialog
    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename List") },
            text = {
                OutlinedTextField(
                    value = renameListName,
                    onValueChange = { renameListName = it },
                    label = { Text("New List Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rename_list_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (renameListName.isNotBlank()) {
                            onRenameList(renameListName)
                            showRenameDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete List Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete '${currentList?.name}'?") },
            text = { Text("This will remove the list and all its grocery items. Are you sure?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteList()
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
