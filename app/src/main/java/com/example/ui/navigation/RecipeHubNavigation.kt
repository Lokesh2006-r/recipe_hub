package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.repository.ThemeMode
import com.example.ui.screens.AddEditRecipeScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyRecipesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RecipeDetailsScreen
import com.example.ui.screens.ShoppingListScreen
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.WarmCream
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.RecipeViewModel
import com.example.ui.viewmodel.ShoppingViewModel
import kotlinx.coroutines.flow.collectLatest

sealed class Screen(val route: String, val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Explore : Screen("explore", "Explore", Icons.Filled.Explore, Icons.Outlined.Explore)
    data object Favorites : Screen("favorites", "Saved", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    data object Shopping : Screen("shopping", "Groceries", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)
    data object MyRecipes : Screen("my_recipes", "My Recipes", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
    data object Categories : Screen("categories", "Categories", Icons.Filled.Category, Icons.Outlined.Category)
    data object Profile : Screen("profile", "Profile", Icons.Filled.AccountCircle, Icons.Filled.AccountCircle)
    data object RecipeDetails : Screen("recipe_detail/{recipeId}", "Recipe Details", Icons.Filled.RestaurantMenu, Icons.Filled.RestaurantMenu) {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    data object AddEditRecipe : Screen("add_edit_recipe?recipeId={recipeId}", "Add / Edit Recipe", Icons.Filled.RestaurantMenu, Icons.Filled.RestaurantMenu) {
        fun createRoute(recipeId: String? = null) = if (recipeId != null) "add_edit_recipe?recipeId=$recipeId" else "add_edit_recipe"
    }
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Explore,
    Screen.Favorites,
    Screen.Shopping,
    Screen.MyRecipes
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeHubApp(
    recipeViewModel: RecipeViewModel,
    shoppingViewModel: ShoppingViewModel,
    authViewModel: AuthViewModel,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onSetThemeMode: (ThemeMode) -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }

    // Collect snackbar messages from ViewModels
    LaunchedEffect(Unit) {
        recipeViewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }
    LaunchedEffect(Unit) {
        shoppingViewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }
    LaunchedEffect(Unit) {
        authViewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val currentUser by authViewModel.currentUser.collectAsState()
    val categories by recipeViewModel.allCategories.collectAsState()
    val featuredRecipes by recipeViewModel.featuredRecipes.collectAsState()
    val quickMeals by recipeViewModel.quickMeals.collectAsState()
    val favoriteRecipes by recipeViewModel.favoriteRecipes.collectAsState()
    val filteredRecipes by recipeViewModel.filteredRecipes.collectAsState()
    val filterState by recipeViewModel.filterState.collectAsState()
    val isSearchingExternal by recipeViewModel.isSearchingExternal.collectAsState()

    val myRecipes by recipeViewModel.getUserRecipes(currentUser?.id ?: "user_julian_1").collectAsState()

    val shoppingLists by shoppingViewModel.userLists.collectAsState()
    val selectedListId by shoppingViewModel.selectedListId.collectAsState()
    val shoppingItems by shoppingViewModel.currentItems.collectAsState()
    val unpurchasedShoppingCount = shoppingItems.count { !it.isPurchased }

    val showBottomBar = currentRoute in bottomNavScreens.map { it.route }
    val showTopBar = currentRoute in listOf(Screen.Home.route, Screen.Explore.route, Screen.Favorites.route)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ForestGreen,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.RestaurantMenu,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "RecipeHub",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onToggleTheme,
                            modifier = Modifier.testTag("top_bar_theme_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = if (isDarkTheme) "Switch to Light Mode" else "Switch to Dark Mode",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { navController.navigate(Screen.Categories.route) },
                            modifier = Modifier.testTag("top_bar_categories_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Category,
                                contentDescription = "Categories",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { navController.navigate(Screen.Profile.route) },
                            modifier = Modifier.testTag("top_bar_profile_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.height(72.dp)
                ) {
                    bottomNavScreens.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                if (screen == Screen.Shopping && unpurchasedShoppingCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = ForestGreen,
                                                contentColor = Color.White
                                            ) {
                                                Text("$unpurchasedShoppingCount")
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                            contentDescription = screen.label
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                        contentDescription = screen.label
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = screen.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ForestGreen,
                                selectedTextColor = ForestGreen,
                                indicatorColor = ForestGreen.copy(alpha = 0.15f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_${screen.route}")
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. HOME SCREEN
            composable(Screen.Home.route) {
                HomeScreen(
                    featuredRecipes = featuredRecipes,
                    quickMeals = quickMeals,
                    categories = categories,
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) },
                    onCategoryClick = { catName ->
                        recipeViewModel.setCategory(catName)
                        navController.navigate(Screen.Explore.route)
                    },
                    onExploreClick = { navController.navigate(Screen.Explore.route) },
                    onAddRecipeClick = { navController.navigate(Screen.AddEditRecipe.createRoute()) },
                    onFavoriteToggle = { recipe -> recipeViewModel.toggleFavorite(recipe) }
                )
            }

            // 2. EXPLORE SCREEN
            composable(Screen.Explore.route) {
                ExploreScreen(
                    recipes = filteredRecipes,
                    categories = categories,
                    filterState = filterState,
                    isSearchingExternal = isSearchingExternal,
                    onQueryChange = { recipeViewModel.setQuery(it) },
                    onCategoryChange = { recipeViewModel.setCategory(it) },
                    onApplyFilter = { newFilter ->
                        recipeViewModel.setCategory(newFilter.category)
                        recipeViewModel.setDietary(newFilter.dietary)
                        recipeViewModel.setDifficulty(newFilter.difficulty)
                        recipeViewModel.setMaxTime(newFilter.maxTime)
                        recipeViewModel.setSortBy(newFilter.sortBy)
                    },
                    onResetFilters = { recipeViewModel.clearFilters() },
                    onSearchExternal = { query -> recipeViewModel.searchExternalRecipesOnline(query) },
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) },
                    onFavoriteToggle = { recipe -> recipeViewModel.toggleFavorite(recipe) }
                )
            }

            // 3. FAVORITES SCREEN
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    favoriteRecipes = favoriteRecipes,
                    categories = categories,
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) },
                    onFavoriteToggle = { recipe -> recipeViewModel.toggleFavorite(recipe) },
                    onExploreClick = { navController.navigate(Screen.Explore.route) }
                )
            }

            // 4. SMART SHOPPING LIST SCREEN
            composable(Screen.Shopping.route) {
                ShoppingListScreen(
                    lists = shoppingLists,
                    selectedListId = selectedListId,
                    items = shoppingItems,
                    onSelectList = { id -> shoppingViewModel.selectList(id) },
                    onCreateList = { name -> shoppingViewModel.createNewList(name) },
                    onRenameList = { newName -> shoppingViewModel.renameCurrentList(newName) },
                    onDeleteList = { shoppingViewModel.deleteCurrentList() },
                    onAddItem = { name, qty, unit -> shoppingViewModel.addItem(name, qty, unit) },
                    onTogglePurchased = { item -> shoppingViewModel.togglePurchased(item) },
                    onDeleteItem = { item -> shoppingViewModel.deleteItem(item) },
                    onClearCompleted = { shoppingViewModel.clearCompleted() }
                )
            }

            // 5. MY RECIPES SCREEN
            composable(Screen.MyRecipes.route) {
                MyRecipesScreen(
                    myRecipes = myRecipes,
                    onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) },
                    onAddRecipeClick = { navController.navigate(Screen.AddEditRecipe.createRoute()) },
                    onEditRecipeClick = { rec -> navController.navigate(Screen.AddEditRecipe.createRoute(rec.id)) },
                    onDeleteRecipeClick = { id -> recipeViewModel.deleteRecipe(id) }
                )
            }

            // 6. CATEGORIES SCREEN
            composable(Screen.Categories.route) {
                CategoriesScreen(
                    categories = categories,
                    onCategoryClick = { catName ->
                        recipeViewModel.setCategory(catName)
                        navController.navigate(Screen.Explore.route)
                    },
                    onAddCategory = { name, desc -> recipeViewModel.addCustomCategory(name, desc) },
                    onDeleteCategory = { id -> recipeViewModel.deleteCategory(id) }
                )
            }

            // 7. PROFILE SCREEN
            composable(Screen.Profile.route) {
                ProfileScreen(
                    user = currentUser,
                    recipeCount = myRecipes.size,
                    favoriteCount = favoriteRecipes.size,
                    shoppingListsCount = shoppingLists.size,
                    themeMode = themeMode,
                    isDarkTheme = isDarkTheme,
                    onSetThemeMode = onSetThemeMode,
                    onToggleTheme = onToggleTheme,
                    onLogin = { email, pass -> authViewModel.login(email, pass) },
                    onRegister = { name, email, pass -> authViewModel.register(name, email, pass) },
                    onUpdateProfile = { name, avatar -> authViewModel.updateProfile(name, avatar) },
                    onLogout = { authViewModel.logout() }
                )
            }

            // 8. RECIPE DETAILS SCREEN
            composable(
                route = Screen.RecipeDetails.route,
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                val recipe = filteredRecipes.firstOrNull { it.id == recipeId }
                    ?: favoriteRecipes.firstOrNull { it.id == recipeId }
                    ?: myRecipes.firstOrNull { it.id == recipeId }
                    ?: featuredRecipes.firstOrNull { it.id == recipeId }
                    ?: quickMeals.firstOrNull { it.id == recipeId }

                RecipeDetailsScreen(
                    recipe = recipe,
                    onBackClick = { navController.popBackStack() },
                    onFavoriteToggle = { rec -> recipeViewModel.toggleFavorite(rec) },
                    onAddAllIngredientsToShoppingList = { ingredients, mult ->
                        shoppingViewModel.addIngredientsFromRecipe(
                            ingredients = ingredients,
                            recipeTitle = recipe?.title ?: "Recipe",
                            multiplier = mult
                        )
                    },
                    onAddSingleIngredientToShoppingList = { ingredient, mult ->
                        shoppingViewModel.addIngredientsFromRecipe(
                            ingredients = listOf(ingredient),
                            recipeTitle = recipe?.title ?: "Recipe",
                            multiplier = mult
                        )
                    }
                )
            }

            // 9. ADD / EDIT RECIPE SCREEN
            composable(
                route = Screen.AddEditRecipe.route,
                arguments = listOf(navArgument("recipeId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId")
                val recipeToEdit = if (recipeId != null) {
                    myRecipes.firstOrNull { it.id == recipeId }
                        ?: filteredRecipes.firstOrNull { it.id == recipeId }
                } else null

                AddEditRecipeScreen(
                    initialRecipe = recipeToEdit,
                    categories = categories,
                    currentUserId = currentUser?.id ?: "user_julian_1",
                    onBackClick = { navController.popBackStack() },
                    onSaveRecipe = { savedRecipe ->
                        recipeViewModel.saveRecipe(savedRecipe) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}
