package com.sholqany.assistant.feature.voice

import javax.inject.Inject
import javax.inject.Singleton

interface WakeWordDetector {
    suspend fun startListening(onWakeWordDetected: () -> Unit)
    fun stopListening()
}

@Singleton
class MockWakeWordDetector @Inject constructor() : WakeWordDetector {
    private var isListening = false

    override suspend fun startListening(onWakeWordDetected: () -> Unit) {
        isListening = true
        // In a real implementation, this would loop or listen to AudioRecord
        // For now, we just log.
        println("MockWakeWordDetector: Started listening...")
    }

    override fun stopListening() {
        isListening = false
        println("MockWakeWordDetector: Stopped listening.")
    }
}
