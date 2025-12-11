# Personal Android AI Assistant (Sholqany)

A voice-activated, personal AI assistant for Android with task tracking, finance management, and autonomous suggestions.

## Features
- **Voice Control**: "Hey Sholqany" w/ Continuous Listening (Foreground Service).
- **Commands**: 
  - "Open [App Name]"
  - "Call [Contact Name]"
  - "Add task [Task Title]"
  - "Spent [Amount] on [Category]"
- **AI Brain**: Integrated with Cloud LLM (e.g., OpenAI/Gemini) for fallback queries.
- **Autonomy**: Proactively suggests tasks if you have many pending.
- **Privacy**: Tasks and Finance data stored locally via Room Database.

## Setup & Build

1. **Prerequisites**
   - Android Studio Iguana or newer.
   - JDK 17+.
   - Android Device/Emulator (API 26+).

2. **Configuration**
   - **LLM API Key**: Open `LLMRepository.kt` and replace `YOUR_API_KEY_HERE` with your OpenAI or Gemini API key.
   - **Wake Word**: The current implementation uses a Mock Detector. To use real hotword detection:
     - Integrate Porcupine or Snowboy.
     - Update `WakeWordDetector.kt`.

3. **Build**
   - Open project in Android Studio.
   - Sync Gradle.
   - Run `app`.

4. **Permissions**
   - On first launch, grant **Microphone**, **Phone**, and **Overlay** permissions when prompted (or manually in Settings).
   - "Display over other apps" is needed for background startup in some Android versions.

## Architecture
- **MVVM** with Jetpack Compose.
- **Hilt** for Dependency Injection.
- **Room** for Local Database.
- **Retrofit** for Cloud API.
- **Foreground Service** for always-on lifecycle.
