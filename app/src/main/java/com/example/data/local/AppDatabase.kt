package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.CategoryEntity
import com.example.data.model.RecipeEntity
import com.example.data.model.ShoppingListEntity
import com.example.data.model.ShoppingListItemEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        RecipeEntity::class,
        CategoryEntity::class,
        ShoppingListEntity::class,
        ShoppingListItemEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipehub_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
