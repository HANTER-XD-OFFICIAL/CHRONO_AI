package com.example.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.OfflineAiEngine
import com.example.ai.OnlineGeminiService
import com.example.model.AiEngineType
import com.example.model.AiPersona
import com.example.model.ChatMessage
import com.example.model.CodeSnippet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

  private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
  val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

  private val _isGenerating = MutableStateFlow(false)
  val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

  private val _engineType = MutableStateFlow(AiEngineType.AUTO_HYBRID)
  val engineType: StateFlow<AiEngineType> = _engineType.asStateFlow()

  private val _persona = MutableStateFlow(AiPersona.HUMAN_FRIEND)
  val persona: StateFlow<AiPersona> = _persona.asStateFlow()

  private val _isDeviceOnline = MutableStateFlow(false)
  val isDeviceOnline: StateFlow<Boolean> = _isDeviceOnline.asStateFlow()

  private val _lastUsedEngine = MutableStateFlow("Offline Engine Ready")
  val lastUsedEngine: StateFlow<String> = _lastUsedEngine.asStateFlow()

  init {
    _isDeviceOnline.value = isNetworkAvailable()
    // Initial friendly, human-like greeting in Bengali and English
    val initialMessage = ChatMessage(
      text = """
        আসসালামু আলাইকুম / হ্যালো! 👋
        আমি **CHRONO AI**—একটি নতুন প্রজন্মের এআই মডেল ও কোডিং অ্যাসিস্ট্যান্ট।
        
        আমি আপনার সাথে সম্পূর্ণ **মানুষের মতো স্বাভাবিক ভাষায়** কথা বলতে পারি এবং যেকোনো বিষয়ের **কোড লিখে ও বুঝিয়ে দিতে পারি**।
        
        ⚡ **আমার অন্যতম বিশেষত্ব:**
        • 🌐 **অনলাইন মোড:** Google Gemini 3.5 দিয়ে লাইভ ক্লাউড ইন্টেলিজেন্স।
        • 📱 **অফলাইন মোড:** ইন্টারনেট ছাড়া সম্পূর্ণ অফলাইনে নিজস্ব অন-ডিভাইস ইঞ্জিনের মাধ্যমে ঝড়ের গতিতে উত্তর ও কোড জেনারেশন!
        
        নিচের যেকোনো প্রম্পটে ক্লিক করুন অথবা বাংলায়/ইংরেজিতে যেকোনো প্রশ্ন লিখুন!
      """.trimIndent(),
      isUser = false,
      engineUsed = "CHRONO Dual-Engine",
      codeBlocks = listOf(
        CodeSnippet(
          language = "python",
          code = "# Welcome to CHRONO AI\ndef greet_human(name: str):\n    return f'Hello {name}! Ready to write clean code today?'\n\nprint(greet_human('Friend'))",
          explanation = "ওয়েলকাম কোড স্নিপেট: পাইথনে ফাংশন দিয়ে বন্ধুসুলভ সম্ভাষণ।"
        )
      )
    )
    _messages.value = listOf(initialMessage)
  }

  fun setEngineType(type: AiEngineType) {
    _engineType.value = type
  }

  fun setPersona(persona: AiPersona) {
    _persona.value = persona
  }

  fun checkNetworkStatus(): Boolean {
    val online = isNetworkAvailable()
    _isDeviceOnline.value = online
    return online
  }

  private fun isNetworkAvailable(): Boolean {
    return try {
      val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
      val network = cm?.activeNetwork ?: return false
      val capabilities = cm.getNetworkCapabilities(network) ?: return false
      capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } catch (e: Throwable) {
      false
    }
  }

  fun sendMessage(promptText: String) {
    val query = promptText.trim()
    if (query.isBlank() || _isGenerating.value) return

    val userMessage = ChatMessage(
      text = query,
      isUser = true
    )
    _messages.value = _messages.value + userMessage
    _isGenerating.value = true

    viewModelScope.launch {
      val startTime = System.currentTimeMillis()
      val isOnline = checkNetworkStatus()
      val selectedEngine = _engineType.value
      val currentPersona = _persona.value

      val shouldUseOnline = when (selectedEngine) {
        AiEngineType.ONLINE_GEMINI -> true
        AiEngineType.OFFLINE_CORE -> false
        AiEngineType.AUTO_HYBRID -> isOnline
      }

      var engineTag = if (shouldUseOnline) "Google Gemini 3.5" else "On-Device Offline Engine"

      if (shouldUseOnline) {
        // Collect past dialogue turns for conversational context
        val history = _messages.value.dropLast(1).map { Pair(it.text, it.isUser) }
        val onlineResult = OnlineGeminiService.generateContent(query, currentPersona, history)

        if (onlineResult.isSuccess) {
          val (responseText, snippets) = onlineResult.getOrThrow()
          val latency = System.currentTimeMillis() - startTime
          _lastUsedEngine.value = "Gemini 3.5 (${latency}ms)"

          val aiMessage = ChatMessage(
            text = responseText,
            isUser = false,
            engineUsed = "Gemini 3.5 Flash (Online)",
            codeBlocks = snippets,
            latencyMs = latency
          )
          _messages.value = _messages.value + aiMessage
          _isGenerating.value = false
          return@launch
        } else {
          // Graceful fallback to Offline Engine
          engineTag = "Offline Engine (Fallback: Online Unavailable)"
        }
      }

      // Execute on Offline AI Engine
      val (offlineText, offlineSnippets) = OfflineAiEngine.generateResponse(query, currentPersona)
      val latency = System.currentTimeMillis() - startTime
      _lastUsedEngine.value = "CHRONO Offline Core (${latency}ms)"

      val aiMessage = ChatMessage(
        text = offlineText,
        isUser = false,
        engineUsed = if (shouldUseOnline) "Offline Core (Auto-Fallback)" else "CHRONO On-Device (Offline)",
        codeBlocks = offlineSnippets,
        latencyMs = latency
      )
      _messages.value = _messages.value + aiMessage
      _isGenerating.value = false
    }
  }

  fun clearChat() {
    _messages.value = emptyList()
    sendMessage("কেমন আছো? তোমার কাজ কী?")
  }
}
