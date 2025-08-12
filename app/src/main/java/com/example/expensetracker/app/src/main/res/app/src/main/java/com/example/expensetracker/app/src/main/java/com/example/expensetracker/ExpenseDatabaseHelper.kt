package com.example.expensetracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExpenseDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_EXPENSES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AMOUNT REAL,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_DATE INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSES")
        onCreate(db)
    }

    fun insertExpense(expense: Expense): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, expense.amount)
            put(COLUMN_CATEGORY, expense.category)
            put(COLUMN_TYPE, expense.type.name)
            put(COLUMN_DATE, expense.date)
        }
        return db.insert(TABLE_EXPENSES, null, values)
    }

    fun getAllExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val db = readableDatabase
        val cursor = db.query(TABLE_EXPENSES, null, null, null, null, null, "$COLUMN_DATE DESC")
        while (cursor.moveToNext()) {
            expenses.add(
                Expense(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    type = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))),
                    date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                )
            )
        }
        cursor.close()
        return expenses
    }

    companion object {
        private const val DATABASE_NAME = "expenses.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_EXPENSES = "expenses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_AMOUNT = "amount"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_DATE = "date"
    }
    }
