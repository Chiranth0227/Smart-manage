package com.example.smartmanage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String, // e.g., "Food", matches Transaction 'note' usually
    val amount: Double,   // The budget limit
    val period: String = "Monthly"
)
