package com.example.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.ModelConfig
import com.example.ai.ModelConfigManager
import com.example.ai.MultiModelAiRouter
import com.example.model.AiEngineType
import com.example.model.AiPersona
import com.example.model.ChatMessage
import com.example.model.CodeSnippet
import com.example.model.ExecutionMode
import com.example.model.SupportedAiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

  val configManager = ModelConfigManager(application)
  val modelConfig: StateFlow<ModelConfig> = configManager.config

  private val _selectedModel = MutableStateFlow(configManager.config.value.selectedModel)
  val selectedModel: StateFlow<SupportedAiModel> = _selectedModel.asStateFlow()

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

  private val _lastUsedEngine = MutableStateFlow("CHRONO Multi-Model Matrix Ready")
  val lastUsedEngine: StateFlow<String> = _lastUsedEngine.asStateFlow()

  init {
    _isDeviceOnline.value = isNetworkAvailable()
    // Initial friendly, human-like greeting in Bengali and English
    val initialMessage = ChatMessage(
      text = """
        আসসালামু আলাইকুম / হ্যালো! 👋
        আমি **CHRONO AI**—একটি পূর্ণাঙ্গ মাল্টি-মডেল সুপার এআই আর্কিটেকচার প্ল্যাটফর্ম।
        
        🌐 **অনলাইন ক্লাউড এপিআই সমর্থিত:**
        • **Claude 3.5 Sonnet** (Anthropic)
        • **GPT-4o** (OpenAI)
        • **Gemini 1.5 Pro / Flash** (Google Cloud)
        • **DeepSeek-V3 / DeepSeek-R1** (DeepSeek Cloud)
        
        💻 **অফলাইন লোকাল ও অন-ডিভাইস ইঞ্জিন:**
        • **Qwen 2.5 Coder** (7B / 14B)
        • **DeepSeek-R1 Distill** (8B / Qwen)
        • **Llama 3.1 8B** & **Mistral 7B / Codestral 22B**
        • **CHRONO Autonomous Edge Core** (ইন্টারনেট বা সার্ভার ছাড়াই ১০০% অফলাইন)
        
        উপরের মডেল মেনু থেকে যেকোনো মডেল বেছে নিন বা বাংলায়/ইংরেজিতে প্রশ্ন করুন!
      """.trimIndent(),
      isUser = false,
      engineUsed = "CHRONO Multi-Model Hub",
      codeBlocks = listOf(
        CodeSnippet(
          language = "kotlin",
          code = """
            // CHRONO Multi-Model Unified Execution
            val selectedAi = SupportedAiModel.CLAUDE_35_SONNET
            println("Active Engine: " + selectedAi.displayName + " [" + selectedAi.provider + "]")
          """.trimIndent(),
          explanation = "মাল্টি-মডেল আর্কিটেকচার: ক্লাউড এবং অন-ডিভাইস লোকাল মডেলগুলোর সমন্বয়।"
        )
      )
    )
    _messages.value = listOf(initialMessage)
  }

  fun setSelectedModel(model: SupportedAiModel) {
    _selectedModel.value = model
    configManager.setSelectedModel(model)

    // Sync legacy engineType for backwards compatibility
    _engineType.value = when (model.mode) {
      ExecutionMode.ONLINE_CLOUD -> AiEngineType.ONLINE_GEMINI
      ExecutionMode.LOCAL_OLLAMA, ExecutionMode.ON_DEVICE_EDGE -> AiEngineType.OFFLINE_CORE
    }
  }

  fun updateApiKeys(
    geminiKey: String? = null,
    openaiKey: String? = null,
    anthropicKey: String? = null,
    deepseekKey: String? = null,
    ollamaUrl: String? = null,
    autoFallback: Boolean? = null
  ) {
    configManager.updateApiKeys(geminiKey, openaiKey, anthropicKey, deepseekKey, ollamaUrl, autoFallback)
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
      val currentModel = _selectedModel.value
      val currentPersona = _persona.value
      val currentConfig = configManager.config.value

      // Collect past dialogue turns for conversational context
      val history = _messages.value.dropLast(1).map { Pair(it.text, it.isUser) }

      val executionResult = MultiModelAiRouter.execute(
        prompt = query,
        model = currentModel,
        persona = currentPersona,
        config = currentConfig,
        conversationHistory = history
      )

      _lastUsedEngine.value = "${executionResult.modelUsedTag} (${executionResult.latencyMs}ms)"

      val aiMessage = ChatMessage(
        text = executionResult.text,
        isUser = false,
        engineUsed = executionResult.modelUsedTag,
        codeBlocks = executionResult.codeSnippets,
        latencyMs = executionResult.latencyMs,
        thoughtProcess = executionResult.thoughtProcess
      )

      _messages.value = _messages.value + aiMessage
      _isGenerating.value = false
    }
  }

  fun clearChat() {
    _messages.value = emptyList()
    sendMessage("কেমন আছো? তোমার সকল মডেলের ক্ষমতা কী?")
  }
}

