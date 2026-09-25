package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.UserEntity
import com.example.data.repository.ThemeMode
import com.example.ui.theme.ForestGreen

@Composable
fun ProfileScreen(
    user: UserEntity?,
    recipeCount: Int,
    favoriteCount: Int,
    shoppingListsCount: Int,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    isDarkTheme: Boolean = false,
    onSetThemeMode: (ThemeMode) -> Unit = {},
    onToggleTheme: () -> Unit = {},
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String) -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(user?.name ?: "") }
    var editAvatar by remember { mutableStateOf(user?.avatarUrl ?: "") }

    // Auth tab state
    var authTab by remember { mutableIntStateOf(0) } // 0 = Login, 1 = Register
    var authEmail by remember { mutableStateOf("") }
    var authPassword by remember { mutableStateOf("") }
    var authName by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(ForestGreen.copy(alpha = 0.1f))
                    ) {
                        if (!user?.avatarUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.avatarUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Profile avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user?.name ?: "Guest Cook",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = user?.email ?: "guest@recipehub.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                editName = user?.name ?: ""
                                editAvatar = user?.avatarUrl ?: ""
                                showEditProfileDialog = true
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("edit_profile_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Edit Profile")
                        }

                        Button(
                            onClick = {
                                authEmail = ""
                                authPassword = ""
                                authName = ""
                                showAuthDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("switch_account_button")
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Switch / Auth")
                        }
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(
                    title = "My Recipes",
                    count = "$recipeCount",
                    icon = Icons.Default.RestaurantMenu,
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    title = "Favorites",
                    count = "$favoriteCount",
                    icon = Icons.Default.Bookmark,
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    title = "Lists",
                    count = "$shoppingListsCount",
                    icon = Icons.Default.ShoppingCart,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Appearance & Theme Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().testTag("theme_settings_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = "Appearance & Theme",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Switch between light, dark, or system mode",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Theme Mode Selector Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // System default chip
                        FilterChip(
                            selected = themeMode == ThemeMode.SYSTEM,
                            onClick = { onSetThemeMode(ThemeMode.SYSTEM) },
                            label = { Text("System", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.BrightnessAuto,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.weight(1f).testTag("theme_chip_system")
                        )

                        // Light chip
                        FilterChip(
                            selected = themeMode == ThemeMode.LIGHT,
                            onClick = { onSetThemeMode(ThemeMode.LIGHT) },
                            label = { Text("Light", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LightMode,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.weight(1f).testTag("theme_chip_light")
                        )

                        // Dark chip
                        FilterChip(
                            selected = themeMode == ThemeMode.DARK,
                            onClick = { onSetThemeMode(ThemeMode.DARK) },
                            label = { Text("Dark", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DarkMode,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.weight(1f).testTag("theme_chip_dark")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Direct Dark Mode Switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = if (isDarkTheme) MaterialTheme.colorScheme.primary else Color(0xFFC77728),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isDarkTheme) "Dark Theme Active" else "Light Theme Active",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (themeMode == ThemeMode.SYSTEM) "Following device system settings" else "Custom override applied",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onToggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("dark_theme_switch")
                        )
                    }
                }
            }
        }

        // Quick Account Actions
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Account Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth().testTag("logout_button")
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFC62828))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Out / Reset to Guest", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_name_input")
                    )
                    OutlinedTextField(
                        value = editAvatar,
                        onValueChange = { editAvatar = it },
                        label = { Text("Avatar Photo URL") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank()) {
                            onUpdateProfile(editName, editAvatar)
                            showEditProfileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Login / Register Dialog
    if (showAuthDialog) {
        AlertDialog(
            onDismissRequest = { showAuthDialog = false },
            title = {
                TabRow(selectedTabIndex = authTab) {
                    Tab(
                        selected = authTab == 0,
                        onClick = { authTab = 0 },
                        text = { Text("Log In") }
                    )
                    Tab(
                        selected = authTab == 1,
                        onClick = { authTab = 1 },
                        text = { Text("Register") }
                    )
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    if (authTab == 1) {
                        OutlinedTextField(
                            value = authName,
                            onValueChange = { authName = it },
                            label = { Text("Full Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("auth_name_input")
                        )
                    }

                    OutlinedTextField(
                        value = authEmail,
                        onValueChange = { authEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_email_input")
                    )

                    OutlinedTextField(
                        value = authPassword,
                        onValueChange = { authPassword = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_password_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (authTab == 0) {
                            onLogin(authEmail, authPassword)
                        } else {
                            onRegister(authName, authEmail, authPassword)
                        }
                        showAuthDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.testTag("auth_submit_button")
                ) {
                    Text(if (authTab == 0) "Log In" else "Sign Up")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAuthDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileStatCard(
    title: String,
    count: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = count, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
