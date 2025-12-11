package com.sholqany.assistant.feature.voice

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sholqany.assistant.data.local.dao.FinanceDao
import com.sholqany.assistant.data.local.dao.TaskDao
import com.sholqany.assistant.data.local.entity.ExpenseEntity
import com.sholqany.assistant.data.local.entity.TaskEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommandProcessor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskDao: TaskDao,
    private val financeDao: FinanceDao,
    private val llmRepository: com.sholqany.assistant.data.repository.LLMRepository,
    private val ttsManager: com.sholqany.assistant.feature.tts.TTSManager
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun processCommand(text: String) {
        val lowerText = text.lowercase()
        scope.launch {
            when {
                lowerText.startsWith("open ") -> {
                    openApp(lowerText.removePrefix("open "))
                    ttsManager.speak("Opening app")
                }
                lowerText.startsWith("call ") -> {
                    callContact(lowerText.removePrefix("call "))
                    ttsManager.speak("Calling")
                }
                lowerText.startsWith("search ") -> searchGoogle(lowerText.removePrefix("search "))
                lowerText.contains("add task") -> addTask(text)
                lowerText.contains("spent") || lowerText.contains("expense") -> addExpense(text)
                else -> {
                    // Fallback to LLM
                    val response = llmRepository.getResponse(text)
                    println("LLM Response: $response")
                    ttsManager.speak(response)
                }
            }
        }
    }

    private fun openApp(appName: String) {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(0)
        
        // Simple fuzzy match for package name
        // In real app, build a map of AppName -> PackageName
        val targetPkg = packages.firstOrNull { 
             it.applicationInfo.loadLabel(packageManager).toString().lowercase().contains(appName) 
        }

        if (targetPkg != null) {
            val intent = packageManager.getLaunchIntentForPackage(targetPkg.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            println("App not found: $appName")
        }
    }

    private fun callContact(name: String) {
        // Requires READ_CONTACTS. For now just generic dial
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun searchGoogle(query: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun addTask(text: String) {
        // "Add task buy milk"
        val taskTitle = text.substringAfter("task ").trim()
        scope.launch {
            taskDao.insertTask(TaskEntity(title = taskTitle))
            println("Task added: $taskTitle")
            ttsManager.speak("Added task: $taskTitle")
        }
    }

    private fun addExpense(text: String) {
        // "I spent 50 on food" -> Regex capture
        // Simple parser
        val amountRegex = Regex("(\\d+)")
        val amountMatch = amountRegex.find(text)
        val amount = amountMatch?.value?.toDoubleOrNull() ?: 0.0
        
        val category = if (text.contains("food")) "Food" else "General"
        
        scope.launch {
            financeDao.insertExpense(ExpenseEntity(amount = amount, category = category, note = text))
            println("Expense added: $amount to $category")
            ttsManager.speak("Recorded expense of $amount")
        }
    }
}
