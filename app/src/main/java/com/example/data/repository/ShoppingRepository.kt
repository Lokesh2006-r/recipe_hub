package com.example.data.repository

import com.example.data.local.ShoppingListDao
import com.example.data.model.Ingredient
import com.example.data.model.ShoppingListEntity
import com.example.data.model.ShoppingListItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

class ShoppingRepository(
    private val shoppingListDao: ShoppingListDao
) {
    fun getListsForUser(userId: String): Flow<List<ShoppingListEntity>> =
        shoppingListDao.getListsForUser(userId)

    fun getItemsForList(listId: String): Flow<List<ShoppingListItemEntity>> =
        shoppingListDao.getItemsForList(listId)

    suspend fun createDefaultListIfEmpty(userId: String): String = withContext(Dispatchers.IO) {
        val count = shoppingListDao.getListCount(userId)
        if (count == 0) {
            val defaultList = ShoppingListEntity(
                id = "list_default_$userId",
                userId = userId,
                name = "Weekly Groceries"
            )
            shoppingListDao.insertList(defaultList)
            defaultList.id
        } else {
            ""
        }
    }

    suspend fun createList(userId: String, name: String): String = withContext(Dispatchers.IO) {
        val newList = ShoppingListEntity(
            id = "list_" + UUID.randomUUID().toString().take(8),
            userId = userId,
            name = name.trim()
        )
        shoppingListDao.insertList(newList)
        newList.id
    }

    suspend fun updateListName(listId: String, newName: String, userId: String) = withContext(Dispatchers.IO) {
        val updated = ShoppingListEntity(
            id = listId,
            userId = userId,
            name = newName.trim(),
            updatedAt = System.currentTimeMillis()
        )
        shoppingListDao.updateList(updated)
    }

    suspend fun deleteList(listId: String) = withContext(Dispatchers.IO) {
        shoppingListDao.deleteAllItemsForList(listId)
        shoppingListDao.deleteList(listId)
    }

    suspend fun addItem(
        listId: String,
        name: String,
        quantity: Double = 1.0,
        unit: String = "",
        recipeTitle: String? = null,
        mergeIfExisting: Boolean = true
    ) = withContext(Dispatchers.IO) {
        val cleanName = name.trim()
        val cleanUnit = unit.trim().lowercase()

        if (mergeIfExisting) {
            val existingItems = shoppingListDao.getItemsForListSync(listId)
            val match = existingItems.firstOrNull {
                it.name.trim().equals(cleanName, ignoreCase = true) &&
                        it.unit.trim().lowercase() == cleanUnit &&
                        !it.isPurchased
            }

            if (match != null) {
                val merged = match.copy(
                    quantity = match.quantity + quantity
                )
                shoppingListDao.updateItem(merged)
                return@withContext
            }
        }

        val newItem = ShoppingListItemEntity(
            id = "item_" + UUID.randomUUID().toString(),
            listId = listId,
            name = cleanName,
            quantity = quantity,
            unit = unit.trim(),
            recipeTitle = recipeTitle
        )
        shoppingListDao.insertItem(newItem)
    }

    suspend fun addIngredientsFromRecipe(
        listId: String,
        ingredients: List<Ingredient>,
        recipeTitle: String,
        servingMultiplier: Double = 1.0
    ) = withContext(Dispatchers.IO) {
        val existingItems = shoppingListDao.getItemsForListSync(listId).toMutableList()

        for (ing in ingredients) {
            val cleanName = ing.name.trim()
            val cleanUnit = ing.unit.trim().lowercase()
            val scaledQty = ing.quantity * servingMultiplier

            val matchIndex = existingItems.indexOfFirst {
                it.name.trim().equals(cleanName, ignoreCase = true) &&
                        it.unit.trim().lowercase() == cleanUnit &&
                        !it.isPurchased
            }

            if (matchIndex >= 0) {
                val existing = existingItems[matchIndex]
                val updated = existing.copy(quantity = existing.quantity + scaledQty)
                shoppingListDao.updateItem(updated)
                existingItems[matchIndex] = updated
            } else {
                val newItem = ShoppingListItemEntity(
                    id = "item_" + UUID.randomUUID().toString(),
                    listId = listId,
                    name = cleanName,
                    quantity = scaledQty,
                    unit = ing.unit.trim(),
                    recipeTitle = recipeTitle
                )
                shoppingListDao.insertItem(newItem)
                existingItems.add(newItem)
            }
        }
    }

    suspend fun togglePurchased(itemId: String, isPurchased: Boolean) = withContext(Dispatchers.IO) {
        shoppingListDao.setPurchased(itemId, isPurchased)
    }

    suspend fun updateItem(item: ShoppingListItemEntity) = withContext(Dispatchers.IO) {
        shoppingListDao.updateItem(item)
    }

    suspend fun deleteItem(itemId: String) = withContext(Dispatchers.IO) {
        shoppingListDao.deleteItem(itemId)
    }

    suspend fun clearCompleted(listId: String) = withContext(Dispatchers.IO) {
        shoppingListDao.clearCompletedItems(listId)
    }
}
