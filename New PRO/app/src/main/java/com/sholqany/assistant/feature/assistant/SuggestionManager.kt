package com.sholqany.assistant.feature.assistant

import com.sholqany.assistant.data.repository.TaskRepository
import com.sholqany.assistant.feature.tts.TTSManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Singleton
class SuggestionManager @Inject constructor(
    private val taskRepository: TaskRepository,
    private val ttsManager: TTSManager
) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var isRunning = false

    fun startMonitoring() {
        if (isRunning) return
        isRunning = true
        scope.launch {
            while (isRunning) {
                // Check every hour (mocked to 1 minute for demo)
                delay(60 * 1000) 
                checkAndSuggest()
            }
        }
    }

    fun stopMonitoring() {
        isRunning = false
    }

    private suspend fun checkAndSuggest() {
        val pendingTasks = taskRepository.pendingTasks.first()
        if (pendingTasks.isNotEmpty()) {
            val count = pendingTasks.size
            val message = "You have $count pending tasks. The top one is ${pendingTasks[0].title}. Would you like to work on it?"
            ttsManager.speak(message)
        }
    }
}
