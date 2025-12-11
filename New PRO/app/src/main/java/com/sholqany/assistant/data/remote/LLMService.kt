package com.sholqany.assistant.data.remote

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Simple generic structure for an LLM API (like OpenAI or Gemini via proxy)
interface LLMService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun generateResponse(@Body request: ChatRequest): ChatResponse
}

data class ChatRequest(
    val model: String = "gpt-3.5-turbo", // Or gemini-pro
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
