package com.example.smartmanage

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount)
}
