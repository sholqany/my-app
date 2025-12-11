package com.sholqany.assistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sholqany.assistant.data.local.dao.FinanceDao
import com.sholqany.assistant.data.local.dao.TaskDao
import com.sholqany.assistant.data.local.entity.ExpenseEntity
import com.sholqany.assistant.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class, ExpenseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun financeDao(): FinanceDao
}
