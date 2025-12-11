package com.sholqany.assistant

import android.content.Intent
import com.sholqany.assistant.service.AssistantService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Auto-start service for testing
        startContextService()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DashboardScreen(
                        onStartService = { startContextService() },
                        onStopService = { stopService(Intent(this, AssistantService::class.java)) }
                    )
                }
            }
        }
    }

    private fun startContextService() {
        val intent = Intent(this, AssistantService::class.java)
        startForegroundService(intent)
    }
}

@Composable
fun DashboardScreen(
    onStartService: () -> Unit, 
    onStopService: () -> Unit,
    viewModel: com.sholqany.assistant.ui.DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val expenses by viewModel.expenses.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Sholqany Assistant", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onStartService) { Text("Start Service") }
            Button(onClick = onStopService) { Text("Stop Service") }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Recent Tasks:", style = MaterialTheme.typography.titleMedium)
        androidx.compose.foundation.lazy.LazyColumn(
             modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            items(tasks.size) { i ->
                val task = tasks[i]
                TaskItem(task, onChecked = { viewModel.completeTask(task) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Recent Expenses:", style = MaterialTheme.typography.titleMedium)
        androidx.compose.foundation.lazy.LazyColumn(
             modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            items(expenses.size) { i ->
                val expense = expenses[i]
                Text("- $${expense.amount} (${expense.category})")
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskEntity, onChecked: () -> Unit) {
    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    ) {
        Checkbox(checked = task.isCompleted, onCheckedChange = { onChecked() })
        Text(
            text = task.title,
            style = if (task.isCompleted) androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough) else androidx.compose.ui.text.TextStyle()
        )
    }
}
