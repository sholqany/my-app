package com.sholqany.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sholqany.assistant.data.local.entity.ExpenseEntity
import com.sholqany.assistant.data.local.entity.TaskEntity
import com.sholqany.assistant.data.repository.FinanceRepository
import com.sholqany.assistant.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val financeRepository: FinanceRepository
) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = taskRepository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseEntity>> = financeRepository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun completeTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.completeTask(task)
        }
    }
    
    fun deleteTask(task: TaskEntity) {
         viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}
