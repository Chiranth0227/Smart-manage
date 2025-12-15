package com.example.smartmanage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class Template(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val type: String, // Storing enum as String for Room simplicity
    val category: String,
    val isRecurring: Boolean,
    val frequency: String
)

enum class TransactionType {
    INCOME, EXPENSE
}
