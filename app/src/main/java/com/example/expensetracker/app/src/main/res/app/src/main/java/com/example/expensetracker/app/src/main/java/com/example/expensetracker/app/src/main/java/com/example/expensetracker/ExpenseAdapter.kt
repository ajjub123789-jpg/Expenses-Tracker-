package com.example.expensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.amountText.text = "₹${expense.amount}"
        holder.categoryText.text = expense.category
        holder.typeText.text = expense.type.name
        holder.dateText.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(expense.date))
    }

    override fun getItemCount(): Int = expenses.size

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amountText: TextView = view.findViewById(R.id.amountText)
        val categoryText: TextView = view.findViewById(R.id.categoryText)
        val typeText: TextView = view.findViewById(R.id.typeText)
        val dateText: TextView = view.findViewById(R.id.dateText)
    }
    }
