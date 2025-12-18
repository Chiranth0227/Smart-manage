package com.example.smartmanage.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    // Transaction operations
    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE type = 'income'")
    fun getTotalIncome(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE type = 'expense'")
    fun getTotalExpense(): Flow<Double>

    // Template operations
    @Insert
    suspend fun insertTemplate(template: Template)

    @Update
    suspend fun updateTemplate(template: Template)

    @Delete
    suspend fun deleteTemplate(template: Template)

    @Query("SELECT * FROM templates ORDER BY id DESC")
    fun getAllTemplates(): Flow<List<Template>>

    // Budget operations
    @Insert
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("SELECT * FROM budgets ORDER BY id DESC")
    fun getAllBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM budgets WHERE category = :category")
    fun getBudgetByCategory(category: String): Flow<Budget?>
    
    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM transactions WHERE type = 'expense' AND category = :category")
    fun getSpentByCategory(category: String): Flow<Double>
}
