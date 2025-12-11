package com.sholqany.assistant.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sholqany.assistant.feature.voice.WakeWordDetector
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AssistantService : Service() {

    @Inject lateinit var wakeWordDetector: WakeWordDetector
    @Inject lateinit var speechManager: SpeechManager
    @Inject lateinit var commandProcessor: CommandProcessor
    @Inject lateinit var suggestionManager: com.sholqany.assistant.feature.assistant.SuggestionManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val CHANNEL_ID = "AssistantServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, createNotification())
        
        suggestionManager.startMonitoring()

        serviceScope.launch {
            wakeWordDetector.startListening {
                // Wake word detected! Now listen for command
                println("Wake word detected! Listening for command...")
                listenForCommand()
            }
        }
    }
    
    private fun listenForCommand() {
        serviceScope.launch(Dispatchers.Main) {
            speechManager.startListening().collect { command ->
                 println("Command received: $command")
                 commandProcessor.processCommand(command)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeWordDetector.stopListening()
        speechManager.destroy()
        suggestionManager.stopMonitoring()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sholqany Assistant")
            .setContentText("Listening for 'Hey Sholqany'...")
            //.setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: Add icon
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Assistant Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
