package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Ingredient
import com.example.data.model.ShoppingListEntity
import com.example.data.model.ShoppingListItemEntity
import com.example.data.repository.ShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingViewModel(
    private val shoppingRepository: ShoppingRepository,
    private val userId: String
) : ViewModel() {

    val userLists: StateFlow<List<ShoppingListEntity>> = shoppingRepository.getListsForUser(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedListId = MutableStateFlow<String?>(null)
    val selectedListId: StateFlow<String?> = _selectedListId.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    val currentItems: StateFlow<List<ShoppingListItemEntity>> = _selectedListId.flatMapLatest { listId ->
        if (listId != null) {
            shoppingRepository.getItemsForList(listId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            shoppingRepository.createDefaultListIfEmpty(userId)
            // Collect userLists once to pick first list if none selected
            userLists.collect { lists ->
                if (_selectedListId.value == null && lists.isNotEmpty()) {
                    _selectedListId.value = lists.first().id
                }
            }
        }
    }

    fun selectList(listId: String) {
        _selectedListId.value = listId
    }

    fun createNewList(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val id = shoppingRepository.createList(userId, name)
            _selectedListId.value = id
            _userMessage.emit("Created list '$name'")
        }
    }

    fun renameCurrentList(newName: String) {
        val currentId = _selectedListId.value ?: return
        if (newName.isBlank()) return
        viewModelScope.launch {
            shoppingRepository.updateListName(currentId, newName, userId)
            _userMessage.emit("List renamed to '$newName'")
        }
    }

    fun deleteCurrentList() {
        val currentId = _selectedListId.value ?: return
        viewModelScope.launch {
            shoppingRepository.deleteList(currentId)
            _selectedListId.value = null
            _userMessage.emit("List deleted")
        }
    }

    fun addItem(name: String, quantity: Double, unit: String) {
        val currentId = _selectedListId.value ?: return
        if (name.isBlank()) return
        viewModelScope.launch {
            shoppingRepository.addItem(
                listId = currentId,
                name = name,
                quantity = if (quantity <= 0) 1.0 else quantity,
                unit = unit,
                mergeIfExisting = true
            )
            _userMessage.emit("Added '$name'")
        }
    }

    fun addIngredientsFromRecipe(
        ingredients: List<Ingredient>,
        recipeTitle: String,
        multiplier: Double = 1.0,
        targetListId: String? = null
    ) {
        val listId = targetListId ?: _selectedListId.value
        if (listId == null) {
            viewModelScope.launch {
                // Create a list if none exists
                val newId = shoppingRepository.createList(userId, "Groceries for $recipeTitle")
                _selectedListId.value = newId
                shoppingRepository.addIngredientsFromRecipe(newId, ingredients, recipeTitle, multiplier)
                _userMessage.emit("Added ${ingredients.size} ingredients to shopping list!")
            }
        } else {
            viewModelScope.launch {
                shoppingRepository.addIngredientsFromRecipe(listId, ingredients, recipeTitle, multiplier)
                _userMessage.emit("Added ${ingredients.size} ingredients to shopping list!")
            }
        }
    }

    fun togglePurchased(item: ShoppingListItemEntity) {
        viewModelScope.launch {
            shoppingRepository.togglePurchased(item.id, !item.isPurchased)
        }
    }

    fun updateItem(item: ShoppingListItemEntity) {
        viewModelScope.launch {
            shoppingRepository.updateItem(item)
        }
    }

    fun deleteItem(item: ShoppingListItemEntity) {
        viewModelScope.launch {
            shoppingRepository.deleteItem(item.id)
        }
    }

    fun clearCompleted() {
        val currentId = _selectedListId.value ?: return
        viewModelScope.launch {
            shoppingRepository.clearCompleted(currentId)
            _userMessage.emit("Completed items cleared")
        }
    }

    class Factory(
        private val shoppingRepository: ShoppingRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShoppingViewModel(shoppingRepository, userId) as T
        }
    }
}
