package com.example.ai

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.BuildConfig
import com.example.model.ApiProvider
import com.example.model.CustomApiKeyEntry
import com.example.model.ExecutionMode
import com.example.model.ModelFamily
import com.example.model.SupportedAiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ModelConfig(
  val selectedModel: SupportedAiModel = SupportedAiModel.CHRONO_AUTO_HYBRID,
  val geminiApiKey: String = "",
  val openaiApiKey: String = "",
  val anthropicApiKey: String = "",
  val deepseekApiKey: String = "",
  val ollamaBaseUrl: String = "http://10.0.2.2:11434",
  val autoFallbackToOffline: Boolean = true,
  val customApiKeys: List<CustomApiKeyEntry> = emptyList()
)

class ModelConfigManager(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("chrono_ai_model_prefs", Context.MODE_PRIVATE)

  private val _config = MutableStateFlow(loadConfig())
  val config: StateFlow<ModelConfig> = _config.asStateFlow()

  private val testClient = OkHttpClient.Builder()
    .connectTimeout(6, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .build()

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

    val customKeysJson = prefs.getString("custom_api_keys_json", "[]") ?: "[]"
    val customKeys = parseCustomKeys(customKeysJson)

    return ModelConfig(
      selectedModel = selected,
      geminiApiKey = geminiKey,
      openaiApiKey = openaiKey,
      anthropicApiKey = anthropicKey,
      deepseekApiKey = deepseekKey,
      ollamaBaseUrl = ollamaUrl,
      autoFallbackToOffline = autoFallback,
      customApiKeys = customKeys
    )
  }

  private fun parseCustomKeys(jsonStr: String): List<CustomApiKeyEntry> {
    val list = mutableListOf<CustomApiKeyEntry>()
    try {
      val arr = JSONArray(jsonStr)
      for (i in 0 until arr.length()) {
        val obj = arr.getJSONObject(i)
        val providerStr = obj.optString("provider", ApiProvider.GOOGLE_GEMINI.name)
        val provider = ApiProvider.fromString(providerStr)
        list.add(
          CustomApiKeyEntry(
            id = obj.optString("id"),
            label = obj.optString("label"),
            provider = provider,
            apiKey = obj.optString("apiKey"),
            customModel = obj.optString("customModel"),
            customBaseUrl = obj.optString("customBaseUrl"),
            isActive = obj.optBoolean("isActive", true),
            createdAt = obj.optLong("createdAt", System.currentTimeMillis())
          )
        )
      }
    } catch (e: Exception) {
      Log.e("ModelConfigManager", "Failed to parse custom api keys json", e)
    }
    return list
  }

  private fun saveCustomKeys(keys: List<CustomApiKeyEntry>) {
    val arr = JSONArray()
    for (k in keys) {
      val obj = JSONObject().apply {
        put("id", k.id)
        put("label", k.label)
        put("provider", k.provider.name)
        put("apiKey", k.apiKey)
        put("customModel", k.customModel)
        put("customBaseUrl", k.customBaseUrl)
        put("isActive", k.isActive)
        put("createdAt", k.createdAt)
      }
      arr.put(obj)
    }
    prefs.edit().putString("custom_api_keys_json", arr.toString()).apply()
  }

  fun addCustomApiKey(entry: CustomApiKeyEntry) {
    val current = _config.value.customApiKeys.toMutableList()
    current.add(0, entry) // Add at top, preserving existing keys in pool
    saveCustomKeys(current)
    _config.value = _config.value.copy(customApiKeys = current)
  }

  fun updateCustomApiKey(entry: CustomApiKeyEntry) {
    val current = _config.value.customApiKeys.map {
      if (it.id == entry.id) entry else it
    }
    saveCustomKeys(current)
    _config.value = _config.value.copy(customApiKeys = current)
  }

  fun deleteCustomApiKey(id: String) {
    val updated = _config.value.customApiKeys.filterNot { it.id == id }
    saveCustomKeys(updated)
    _config.value = _config.value.copy(customApiKeys = updated)
  }

  fun toggleKeyActive(id: String) {
    val updated = _config.value.customApiKeys.map {
      if (it.id == id) {
        it.copy(isActive = !it.isActive)
      } else it
    }
    saveCustomKeys(updated)
    _config.value = _config.value.copy(customApiKeys = updated)
  }

  fun getActiveKeyForProvider(provider: ApiProvider): CustomApiKeyEntry? {
    return _config.value.customApiKeys.firstOrNull { it.provider == provider && it.isActive }
  }

  fun getActiveKeysForFamily(family: ModelFamily): List<CustomApiKeyEntry> {
    return _config.value.customApiKeys.filter { it.provider.family == family && it.isActive && it.apiKey.isNotBlank() }
  }

  fun getActiveKeyForFamily(family: ModelFamily): CustomApiKeyEntry? {
    return _config.value.customApiKeys.firstOrNull { it.provider.family == family && it.isActive }
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
    // 1. Check custom vault for active Google Gemini key
    val custom = getActiveKeyForProvider(ApiProvider.GOOGLE_GEMINI)
    if (custom != null && custom.apiKey.isNotBlank()) {
      return custom.apiKey
    }
    // 2. Check legacy preference
    val configured = _config.value.geminiApiKey
    if (configured.isNotBlank()) return configured
    // 3. Check BuildConfig (if set via Secrets)
    val buildKey = BuildConfig.GEMINI_API_KEY
    return if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
  }

  fun getEffectiveKeyForModel(model: SupportedAiModel): Pair<String, CustomApiKeyEntry?> {
    val activeCustom = getActiveKeyForFamily(model.family)
    if (activeCustom != null && activeCustom.apiKey.isNotBlank()) {
      return Pair(activeCustom.apiKey, activeCustom)
    }

    val fallbackKey = when (model.family) {
      ModelFamily.GOOGLE -> getEffectiveGeminiKey()
      ModelFamily.OPENAI -> _config.value.openaiApiKey
      ModelFamily.ANTHROPIC -> _config.value.anthropicApiKey
      ModelFamily.DEEPSEEK_CLOUD -> _config.value.deepseekApiKey
      ModelFamily.OLLAMA_LOCAL, ModelFamily.CHRONO_EDGE -> ""
    }
    return Pair(fallbackKey, null)
  }

  fun isModelKeyAvailable(model: SupportedAiModel): Boolean {
    val (key, _) = getEffectiveKeyForModel(model)
    return when (model.family) {
      ModelFamily.OLLAMA_LOCAL, ModelFamily.CHRONO_EDGE -> true
      else -> key.isNotBlank()
    }
  }

  suspend fun testApiKeyConnection(entry: CustomApiKeyEntry): Result<Long> = withContext(Dispatchers.IO) {
    val start = System.currentTimeMillis()
    try {
      when (entry.provider) {
        ApiProvider.GOOGLE_GEMINI -> {
          val model = if (entry.customModel.isNotBlank()) entry.customModel else "gemini-1.5-flash"
          val baseUrl = if (entry.customBaseUrl.isNotBlank()) entry.customBaseUrl.trimEnd('/') else "https://generativelanguage.googleapis.com"
          val url = "$baseUrl/v1beta/models/$model?key=${entry.apiKey.trim()}"
          val req = Request.Builder().url(url).get().build()
          val res = testClient.newCall(req).execute()
          if (!res.isSuccessful && res.code != 200) {
            return@withContext Result.failure(Exception("HTTP ${res.code}: ${res.message}"))
          }
          Result.success(System.currentTimeMillis() - start)
        }

        ApiProvider.OPENAI, ApiProvider.OPENROUTER, ApiProvider.CUSTOM_ENDPOINT -> {
          val baseUrl = if (entry.customBaseUrl.isNotBlank()) entry.customBaseUrl.trimEnd('/') else entry.provider.defaultBaseUrl
          val url = "$baseUrl/models"
          val req = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer ${entry.apiKey.trim()}")
            .get()
            .build()
          val res = testClient.newCall(req).execute()
          if (!res.isSuccessful && res.code != 200 && res.code != 404) {
            return@withContext Result.failure(Exception("HTTP ${res.code}"))
          }
          Result.success(System.currentTimeMillis() - start)
        }

        ApiProvider.ANTHROPIC -> {
          val baseUrl = if (entry.customBaseUrl.isNotBlank()) entry.customBaseUrl.trimEnd('/') else "https://api.anthropic.com/v1"
          val url = "$baseUrl/messages"
          val json = JSONObject().apply {
            put("model", if (entry.customModel.isNotBlank()) entry.customModel else "claude-3-5-sonnet-20241022")
            put("max_tokens", 10)
            put("messages", JSONArray().put(JSONObject().put("role", "user").put("content", "ping")))
          }
          val req = Request.Builder()
            .url(url)
            .header("x-api-key", entry.apiKey.trim())
            .header("anthropic-version", "2023-06-01")
            .header("content-type", "application/json")
            .post(json.toString().toRequestBody("application/json".toMediaType()))
            .build()
          val res = testClient.newCall(req).execute()
          if (res.code in listOf(200, 400)) { // 400 means auth passed
            Result.success(System.currentTimeMillis() - start)
          } else {
            Result.failure(Exception("HTTP ${res.code}"))
          }
        }

        ApiProvider.DEEPSEEK -> {
          val baseUrl = if (entry.customBaseUrl.isNotBlank()) entry.customBaseUrl.trimEnd('/') else "https://api.deepseek.com"
          val url = "$baseUrl/models"
          val req = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer ${entry.apiKey.trim()}")
            .get()
            .build()
          val res = testClient.newCall(req).execute()
          if (res.isSuccessful || res.code == 200) {
            Result.success(System.currentTimeMillis() - start)
          } else {
            Result.failure(Exception("HTTP ${res.code}"))
          }
        }

        ApiProvider.OLLAMA_LOCAL_SERVER -> {
          val baseUrl = if (entry.customBaseUrl.isNotBlank()) entry.customBaseUrl.trimEnd('/') else "http://10.0.2.2:11434"
          val url = "$baseUrl/api/tags"
          val req = Request.Builder().url(url).get().build()
          val res = testClient.newCall(req).execute()
          if (res.isSuccessful) {
            Result.success(System.currentTimeMillis() - start)
          } else {
            Result.failure(Exception("Ollama HTTP ${res.code}"))
          }
        }
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
