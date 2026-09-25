package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ShoppingListEntity
import com.example.data.model.ShoppingListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists WHERE userId = :userId ORDER BY createdAt DESC")
    fun getListsForUser(userId: String): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    fun getListById(id: String): Flow<ShoppingListEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingListEntity)

    @Update
    suspend fun updateList(list: ShoppingListEntity)

    @Query("DELETE FROM shopping_lists WHERE id = :id")
    suspend fun deleteList(id: String)

    @Query("SELECT * FROM shopping_list_items WHERE listId = :listId ORDER BY isPurchased ASC, createdAt DESC")
    fun getItemsForList(listId: String): Flow<List<ShoppingListItemEntity>>

    @Query("SELECT * FROM shopping_list_items WHERE listId = :listId")
    suspend fun getItemsForListSync(listId: String): List<ShoppingListItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingListItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingListItemEntity>)

    @Update
    suspend fun updateItem(item: ShoppingListItemEntity)

    @Query("UPDATE shopping_list_items SET isPurchased = :isPurchased WHERE id = :itemId")
    suspend fun setPurchased(itemId: String, isPurchased: Boolean)

    @Query("DELETE FROM shopping_list_items WHERE id = :itemId")
    suspend fun deleteItem(itemId: String)

    @Query("DELETE FROM shopping_list_items WHERE listId = :listId AND isPurchased = 1")
    suspend fun clearCompletedItems(listId: String)

    @Query("DELETE FROM shopping_list_items WHERE listId = :listId")
    suspend fun deleteAllItemsForList(listId: String)

    @Query("SELECT COUNT(*) FROM shopping_lists WHERE userId = :userId")
    suspend fun getListCount(userId: String): Int
}
