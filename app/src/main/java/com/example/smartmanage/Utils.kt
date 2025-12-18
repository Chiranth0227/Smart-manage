package com.example.smartmanage

import java.text.DecimalFormat

fun formatCurrency(amount: Double): String {
    val formatter = DecimalFormat("₹#,##,##0.00")
    return formatter.format(amount)
}
