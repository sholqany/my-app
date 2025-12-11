package com.sholqany.assistant.data.repository

import com.sholqany.assistant.data.local.dao.FinanceDao
import com.sholqany.assistant.data.local.dao.TaskDao
import com.sholqany.assistant.data.local.entity.ExpenseEntity
import com.sholqany.assistant.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val pendingTasks: Flow<List<TaskEntity>> = taskDao.getPendingTasks()

    suspend fun addTask(title: String, description: String? = null) {
        taskDao.insertTask(TaskEntity(title = title, description = description))
    }

    suspend fun completeTask(task: TaskEntity) {
        taskDao.updateTask(task.copy(isCompleted = true))
    }
    
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}

@Singleton
class FinanceRepository @Inject constructor(
    private val financeDao: FinanceDao
) {
    val allExpenses: Flow<List<ExpenseEntity>> = financeDao.getAllExpenses()

    suspend fun addExpense(amount: Double, category: String, note: String?) {
        financeDao.insertExpense(ExpenseEntity(amount = amount, category = category, note = note))
    }
    
    fun getTotalExpensesSince(timestamp: Long): Flow<Double?> {
        return financeDao.getTotalExpensesSince(timestamp)
    }
}
