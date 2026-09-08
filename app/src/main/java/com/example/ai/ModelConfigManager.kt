package com.example.ai

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.model.ExecutionMode
import com.example.model.ModelFamily
import com.example.model.SupportedAiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ModelConfig(
  val selectedModel: SupportedAiModel = SupportedAiModel.CHRONO_AUTO_HYBRID,
  val geminiApiKey: String = "",
  val openaiApiKey: String = "",
  val anthropicApiKey: String = "",
  val deepseekApiKey: String = "",
  val ollamaBaseUrl: String = "http://10.0.2.2:11434",
  val autoFallbackToOffline: Boolean = true
)

class ModelConfigManager(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("chrono_ai_model_prefs", Context.MODE_PRIVATE)

  private val _config = MutableStateFlow(loadConfig())
  val config: StateFlow<ModelConfig> = _config.asStateFlow()

  private fun loadConfig(): ModelConfig {
    val modelId = prefs.getString("selected_model_id", SupportedAiModel.CHRONO_AUTO_HYBRID.id)
      ?: SupportedAiModel.CHRONO_AUTO_HYBRID.id
    val selected = SupportedAiModel.fromId(modelId)

    val geminiKey = prefs.getString("gemini_api_key", "") ?: ""
    val openaiKey = prefs.getString("openai_api_key", "") ?: ""
    val anthropicKey = prefs.getString("anthropic_api_key", "") ?: ""
    val deepseekKey = prefs.getString("deepseek_api_key", "") ?: ""
    val ollamaUrl = prefs.getString("ollama_base_url", "http://10.0.2.2:11434") ?: "http://10.0.2.2:11434"
    val autoFallback = prefs.getBoolean("auto_fallback", true)

    return ModelConfig(
      selectedModel = selected,
      geminiApiKey = geminiKey,
      openaiApiKey = openaiKey,
      anthropicApiKey = anthropicKey,
      deepseekApiKey = deepseekKey,
      ollamaBaseUrl = ollamaUrl,
      autoFallbackToOffline = autoFallback
    )
  }

  fun setSelectedModel(model: SupportedAiModel) {
    prefs.edit().putString("selected_model_id", model.id).apply()
    _config.value = _config.value.copy(selectedModel = model)
  }

  fun updateApiKeys(
    geminiKey: String? = null,
    openaiKey: String? = null,
    anthropicKey: String? = null,
    deepseekKey: String? = null,
    ollamaUrl: String? = null,
    autoFallback: Boolean? = null
  ) {
    val editor = prefs.edit()
    var current = _config.value

    geminiKey?.let {
      editor.putString("gemini_api_key", it.trim())
      current = current.copy(geminiApiKey = it.trim())
    }
    openaiKey?.let {
      editor.putString("openai_api_key", it.trim())
      current = current.copy(openaiApiKey = it.trim())
    }
    anthropicKey?.let {
      editor.putString("anthropic_api_key", it.trim())
      current = current.copy(anthropicApiKey = it.trim())
    }
    deepseekKey?.let {
      editor.putString("deepseek_api_key", it.trim())
      current = current.copy(deepseekApiKey = it.trim())
    }
    ollamaUrl?.let {
      val cleanUrl = it.trim().trimEnd('/')
      editor.putString("ollama_base_url", cleanUrl)
      current = current.copy(ollamaBaseUrl = cleanUrl)
    }
    autoFallback?.let {
      editor.putBoolean("auto_fallback", it)
      current = current.copy(autoFallbackToOffline = it)
    }

    editor.apply()
    _config.value = current
  }

  fun getEffectiveGeminiKey(): String {
    val configured = _config.value.geminiApiKey
    if (configured.isNotBlank()) return configured
    val buildKey = BuildConfig.GEMINI_API_KEY
    return if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
  }

  fun isModelKeyAvailable(model: SupportedAiModel): Boolean {
    return when (model.family) {
      ModelFamily.GOOGLE -> getEffectiveGeminiKey().isNotBlank()
      ModelFamily.OPENAI -> _config.value.openaiApiKey.isNotBlank()
      ModelFamily.ANTHROPIC -> _config.value.anthropicApiKey.isNotBlank()
      ModelFamily.DEEPSEEK_CLOUD -> _config.value.deepseekApiKey.isNotBlank()
      ModelFamily.OLLAMA_LOCAL -> true // network/server check
      ModelFamily.CHRONO_EDGE -> true // always available
    }
  }
}
