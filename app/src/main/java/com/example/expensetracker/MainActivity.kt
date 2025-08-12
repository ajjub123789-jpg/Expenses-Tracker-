package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupCharts()
        setupAddButtons()

        viewModel.expenses.observe(this) { expenses ->
            updateCharts(expenses)
            (binding.recyclerView.adapter as ExpenseAdapter).submitList(expenses)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ExpenseAdapter()
    }

    private fun setupCharts() {
        binding.chartSpending.description.isEnabled = false
        binding.chartIncome.description.isEnabled = false
    }

    private fun setupAddButtons() {
        binding.btnAddExpense.setOnClickListener {
            AddExpenseDialogFragment(false).show(supportFragmentManager, "AddExpense")
        }
        binding.btnAddIncome.setOnClickListener {
            AddExpenseDialogFragment(true).show(supportFragmentManager, "AddIncome")
        }
    }

    private fun updateCharts(expenses: List<Expense>) {
        val monthlySpending = mutableMapOf<Int, Float>()
        val monthlyIncome = mutableMapOf<Int, Float>()

        expenses.forEach {
            val month = it.date.monthValue
            if (it.isIncome) {
                monthlyIncome[month] = (monthlyIncome[month] ?: 0f) + it.amount
            } else {
                monthlySpending[month] = (monthlySpending[month] ?: 0f) + it.amount
            }
        }

        val spendingEntries = monthlySpending.map { BarEntry(it.key.toFloat(), it.value) }
        val incomeEntries = monthlyIncome.map { BarEntry(it.key.toFloat(), it.value) }

        val spendingDataSet = BarDataSet(spendingEntries, "Spending").apply {
            colors = ColorTemplate.COLORFUL_COLORS.toList()
        }
        val incomeDataSet = BarDataSet(incomeEntries, "Income").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }

        binding.chartSpending.data = BarData(spendingDataSet)
        binding.chartIncome.data = BarData(incomeDataSet)
        binding.chartSpending.invalidate()
        binding.chartIncome.invalidate()
    }
}
