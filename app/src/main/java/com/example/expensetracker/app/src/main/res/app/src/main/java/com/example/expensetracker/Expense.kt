package com.example.expensetracker

data class Expense(
    val id: Long = 0L,
    val amount: Double,
    val category: String,
    val type: ExpenseType, // INCOME or EXPENSE
    val date: Long
)

enum class ExpenseType {
    INCOME, EXPENSE
}
