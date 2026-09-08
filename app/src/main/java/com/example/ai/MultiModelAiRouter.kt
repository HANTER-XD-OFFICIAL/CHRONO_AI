package com.example.ai

import android.util.Log
import com.example.model.AiPersona
import com.example.model.CodeSnippet
import com.example.model.ExecutionMode
import com.example.model.ModelFamily
import com.example.model.SupportedAiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

data class ModelExecutionResult(
  val text: String,
  val codeSnippets: List<CodeSnippet>,
  val modelUsedTag: String,
  val executionMode: ExecutionMode,
  val thoughtProcess: String? = null,
  val isFallback: Boolean = false,
  val latencyMs: Long = 0L
)

object MultiModelAiRouter {
  private const val TAG = "MultiModelAiRouter"

  private val httpClient = OkHttpClient.Builder()
    .connectTimeout(12, TimeUnit.SECONDS)
    .readTimeout(35, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .build()

  private val fastHttpClient = OkHttpClient.Builder()
    .connectTimeout(3, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .build()

  suspend fun execute(
    prompt: String,
    model: SupportedAiModel,
    persona: AiPersona,
    config: ModelConfig,
    conversationHistory: List<Pair<String, Boolean>> = emptyList()
  ): ModelExecutionResult = withContext(Dispatchers.IO) {
    val startTime = System.currentTimeMillis()

    // Smart Hybrid Resolution
    val effectiveModel = if (model == SupportedAiModel.CHRONO_AUTO_HYBRID) {
      resolveAutoHybridModel(prompt, config)
    } else {
      model
    }

    try {
      when (effectiveModel.family) {
        ModelFamily.GOOGLE -> {
          val key = config.geminiApiKey.ifBlank { configManagerEffectiveGeminiKey(config) }
          if (key.isBlank()) {
            return@withContext fallbackToOffline(prompt, persona, effectiveModel, startTime, "Gemini API Key প্রয়োজন। অফলাইন অন-ডিভাইস মডেলে সুইচ করা হয়েছে।")
          }
          val res = callGeminiApi(prompt, effectiveModel, persona, key, conversationHistory)
          val elapsed = System.currentTimeMillis() - startTime
          ModelExecutionResult(
            text = res.first,
            codeSnippets = res.second,
            modelUsedTag = "${effectiveModel.displayName} (Google Cloud)",
            executionMode = ExecutionMode.ONLINE_CLOUD,
            latencyMs = elapsed
          )
        }

        ModelFamily.ANTHROPIC -> {
          val key = config.anthropicApiKey.trim()
          if (key.isBlank()) {
            return@withContext fallbackToOffline(prompt, persona, effectiveModel, startTime, "Anthropic API Key দেওয়া হয়নি। লোকাল ইঞ্জিনের মাধ্যমে রেসপন্স দেওয়া হলো।")
          }
          val res = callAnthropicApi(prompt, effectiveModel, persona, key, conversationHistory)
          val elapsed = System.currentTimeMillis() - startTime
          ModelExecutionResult(
            text = res.first,
            codeSnippets = res.second,
            modelUsedTag = "${effectiveModel.displayName} (Anthropic Cloud)",
            executionMode = ExecutionMode.ONLINE_CLOUD,
            latencyMs = elapsed
          )
        }

        ModelFamily.OPENAI -> {
          val key = config.openaiApiKey.trim()
          if (key.isBlank()) {
            return@withContext fallbackToOffline(prompt, persona, effectiveModel, startTime, "OpenAI API Key দেওয়া হয়নি। লোকাল ইঞ্জিনের মাধ্যমে রেসপন্স দেওয়া হলো।")
          }
          val res = callOpenAiApi(prompt, effectiveModel, persona, key, conversationHistory)
          val elapsed = System.currentTimeMillis() - startTime
          ModelExecutionResult(
            text = res.first,
            codeSnippets = res.second,
            modelUsedTag = "${effectiveModel.displayName} (OpenAI Cloud)",
            executionMode = ExecutionMode.ONLINE_CLOUD,
            latencyMs = elapsed
          )
        }

        ModelFamily.DEEPSEEK_CLOUD -> {
          val key = config.deepseekApiKey.trim()
          if (key.isBlank()) {
            return@withContext fallbackToOffline(prompt, persona, effectiveModel, startTime, "DeepSeek API Key দেওয়া হয়নি। অন-ডিভাইস R1 রিজনিং ইঞ্জিনে উত্তর দেওয়া হলো।")
          }
          val res = callDeepSeekCloudApi(prompt, effectiveModel, persona, key, conversationHistory)
          val elapsed = System.currentTimeMillis() - startTime
          ModelExecutionResult(
            text = res.first,
            codeSnippets = res.second,
            modelUsedTag = "${effectiveModel.displayName} (DeepSeek Cloud)",
            executionMode = ExecutionMode.ONLINE_CLOUD,
            thoughtProcess = res.third,
            latencyMs = elapsed
          )
        }

        ModelFamily.OLLAMA_LOCAL -> {
          // Attempt real Ollama server connection
          try {
            val res = callOllamaLocalApi(prompt, effectiveModel, persona, config.ollamaBaseUrl, conversationHistory)
            val elapsed = System.currentTimeMillis() - startTime
            ModelExecutionResult(
              text = res.first,
              codeSnippets = res.second,
              modelUsedTag = "${effectiveModel.displayName} (Ollama Local)",
              executionMode = ExecutionMode.LOCAL_OLLAMA,
              thoughtProcess = res.third,
              latencyMs = elapsed
            )
          } catch (e: Exception) {
            Log.w(TAG, "Ollama local server unreachable at ${config.ollamaBaseUrl}, using on-device emulation", e)
            fallbackToOffline(prompt, persona, effectiveModel, startTime, "Ollama সার্ভার (${config.ollamaBaseUrl}) অফলাইন। অন-ডিভাইস এমুলেশন ইঞ্জিনে কাজ করছে।")
          }
        }

        ModelFamily.CHRONO_EDGE -> {
          fallbackToOffline(prompt, persona, effectiveModel, startTime, null)
        }
      }
    } catch (err: Exception) {
      Log.e(TAG, "Model call failed for ${effectiveModel.displayName}", err)
      fallbackToOffline(prompt, persona, effectiveModel, startTime, "সার্ভার এরর (${err.localizedMessage ?: "Connection error"})। অন-ডিভাইস ইঞ্জিনে সুইচ করা হয়েছে।")
    }
  }

  private fun resolveAutoHybridModel(prompt: String, config: ModelConfig): SupportedAiModel {
    val lower = prompt.lowercase()
    val isCoding = lower.contains("code") || lower.contains("function") || lower.contains("কোড") || lower.contains("class") || lower.contains("api")
    val isReasoning = lower.contains("why") || lower.contains("explain") || lower.contains("গণিত") || lower.contains("ব্যাখ্যা") || lower.contains("কেন")

    return when {
      config.deepseekApiKey.isNotBlank() && isReasoning -> SupportedAiModel.DEEPSEEK_R1_CLOUD
      config.anthropicApiKey.isNotBlank() -> SupportedAiModel.CLAUDE_35_SONNET
      config.openaiApiKey.isNotBlank() -> SupportedAiModel.GPT_4O
      config.geminiApiKey.isNotBlank() || configManagerEffectiveGeminiKey(config).isNotBlank() -> {
        if (isCoding) SupportedAiModel.GEMINI_PRO else SupportedAiModel.GEMINI_FLASH
      }
      isCoding -> SupportedAiModel.QWEN_25_CODER_7B
      else -> SupportedAiModel.LLAMA_31_8B
    }
  }

  private fun configManagerEffectiveGeminiKey(config: ModelConfig): String {
    val buildKey = com.example.BuildConfig.GEMINI_API_KEY
    return if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
  }

  // --- 1. GOOGLE GEMINI CALL ---
  private fun callGeminiApi(
    prompt: String,
    model: SupportedAiModel,
    persona: AiPersona,
    apiKey: String,
    history: List<Pair<String, Boolean>>
  ): Pair<String, List<CodeSnippet>> {
    val modelTag = if (model == SupportedAiModel.GEMINI_PRO) "gemini-3.1-pro-preview" else "gemini-3.5-flash"
    val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelTag:generateContent?key=$apiKey"

    val systemPrompt = buildSystemPrompt(persona, model)
    val rootJson = JSONObject().apply {
      put("systemInstruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemPrompt))))

      val contents = JSONArray()
      val recent = history.takeLast(6)
      for ((msg, isUser) in recent) {
        contents.put(JSONObject().apply {
          put("role", if (isUser) "user" else "model")
          put("parts", JSONArray().put(JSONObject().put("text", msg)))
        })
      }
      contents.put(JSONObject().apply {
        put("role", "user")
        put("parts", JSONArray().put(JSONObject().put("text", prompt)))
      })
      put("contents", contents)
    }

    val req = Request.Builder()
      .url(url)
      .post(rootJson.toString().toRequestBody("application/json".toMediaType()))
      .build()

    val response = httpClient.newCall(req).execute()
    if (!response.isSuccessful) {
      throw IllegalStateException("Gemini API Error code: ${response.code}")
    }

    val resText = response.body?.string() ?: ""
    val json = JSONObject(resText)
    val candidate = json.optJSONArray("candidates")?.optJSONObject(0)
    val content = candidate?.optJSONObject("content")
    val text = content?.optJSONArray("parts")?.optJSONObject(0)?.optString("text") ?: ""

    return extractCodeSnippets(text)
  }

  // --- 2. ANTHROPIC CLAUDE 3.5 SONNET CALL ---
  private fun callAnthropicApi(
    prompt: String,
    model: SupportedAiModel,
    persona: AiPersona,
    apiKey: String,
    history: List<Pair<String, Boolean>>
  ): Pair<String, List<CodeSnippet>> {
    val url = "https://api.anthropic.com/v1/messages"
    val systemPrompt = buildSystemPrompt(persona, model)

    val root = JSONObject().apply {
      put("model", model.modelTag) // claude-3-5-sonnet-20241022
      put("max_tokens", 4096)
      put("system", systemPrompt)

      val messages = JSONArray()
      val recent = history.takeLast(6)
      for ((msg, isUser) in recent) {
        messages.put(JSONObject().apply {
          put("role", if (isUser) "user" else "assistant")
          put("content", msg)
        })
      }
      messages.put(JSONObject().apply {
        put("role", "user")
        put("content", prompt)
      })
      put("messages", messages)
    }

    val req = Request.Builder()
      .url(url)
      .header("x-api-key", apiKey)
      .header("anthropic-version", "2023-06-01")
      .header("content-type", "application/json")
      .post(root.toString().toRequestBody("application/json".toMediaType()))
      .build()

    val response = httpClient.newCall(req).execute()
    if (!response.isSuccessful) {
      throw IllegalStateException("Anthropic Claude API Error code: ${response.code}")
    }

    val resText = response.body?.string() ?: ""
    val json = JSONObject(resText)
    val contents = json.optJSONArray("content")
    val text = contents?.optJSONObject(0)?.optString("text") ?: ""

    return extractCodeSnippets(text)
  }

  // --- 3. OPENAI GPT-4O CALL ---
  private fun callOpenAiApi(
    prompt: String,
    model: SupportedAiModel,
    persona: AiPersona,
    apiKey: String,
    history: List<Pair<String, Boolean>>
  ): Pair<String, List<CodeSnippet>> {
    val url = "https://api.openai.com/v1/chat/completions"
    val systemPrompt = buildSystemPrompt(persona, model)

    val messages = JSONArray().apply {
      put(JSONObject().apply {
        put("role", "system")
        put("content", systemPrompt)
      })
      for ((msg, isUser) in history.takeLast(6)) {
        put(JSONObject().apply {
          put("role", if (isUser) "user" else "assistant")
          put("content", msg)
        })
      }
      put(JSONObject().apply {
        put("role", "user")
        put("content", prompt)
      })
    }

    val root = JSONObject().apply {
      put("model", model.modelTag) // gpt-4o
      put("messages", messages)
      put("temperature", 0.7)
    }

    val req = Request.Builder()
      .url(url)
      .header("Authorization", "Bearer $apiKey")
      .header("Content-Type", "application/json")
      .post(root.toString().toRequestBody("application/json".toMediaType()))
      .build()

    val response = httpClient.newCall(req).execute()
    if (!response.isSuccessful) {
      throw IllegalStateException("OpenAI API Error code: ${response.code}")
    }

    val resText = response.body?.string() ?: ""
    val json = JSONObject(resText)
    val choice = json.optJSONArray("choices")?.optJSONObject(0)
    val text = choice?.optJSONObject("message")?.optString("content") ?: ""

    return extractCodeSnippets(text)
  }

  // --- 4. DEEPSEEK CLOUD (V3 / R1) CALL ---
  private fun callDeepSeekCloudApi(
    prompt: String,
    model: SupportedAiModel,
    persona: AiPersona,
    apiKey: String,
    history: List<Pair<String, Boolean>>
  ): Triple<String, List<CodeSnippet>, String?> {
    val url = "https://api.deepseek.com/chat/completions"
    val systemPrompt = buildSystemPrompt(persona, model)

    val messages = JSONArray().apply {
      put(JSONObject().apply {
        put("role", "system")
        put("content", systemPrompt)
      })
      for ((msg, isUser) in history.takeLast(6)) {
        put(JSONObject().apply {
          put("role", if (isUser) "user" else "assistant")
          put("content", msg)
        })
      }
      put(JSONObject().apply {
        put("role", "user")
        put("content", prompt)
      })
    }

    val root = JSONObject().apply {
      put("model", model.modelTag) // deepseek-chat or deepseek-reasoner
      put("messages", messages)
    }

    val req = Request.Builder()
      .url(url)
      .header("Authorization", "Bearer $apiKey")
      .header("Content-Type", "application/json")
      .post(root.toString().toRequestBody("application/json".toMediaType()))
      .build()

    val response = httpClient.newCall(req).execute()
    if (!response.isSuccessful) {
      throw IllegalStateException("DeepSeek Cloud Error code: ${response.code}")
    }

    val resText = response.body?.string() ?: ""
    val json = JSONObject(resText)
    val choice = json.optJSONArray("choices")?.optJSONObject(0)
    val messageObj = choice?.optJSONObject("message")
    val content = messageObj?.optString("content") ?: ""
    val reasoning = messageObj?.optString("reasoning_content")?.ifBlank { null }

    val (cleanText, snippets) = extractCodeSnippets(content)
    return Triple(cleanText, snippets, reasoning)
  }

  // --- 5. OLLAMA / LOCAL ENGINES CALL ---
  private fun callOllamaLocalApi(
    prompt: String,
    model: SupportedAiModel,
    persona: AiPersona,
    baseUrl: String,
    history: List<Pair<String, Boolean>>
  ): Triple<String, List<CodeSnippet>, String?> {
    val endpoint = "${baseUrl.trimEnd('/')}/api/chat"
    val systemPrompt = buildSystemPrompt(persona, model)

    val messages = JSONArray().apply {
      put(JSONObject().apply {
        put("role", "system")
        put("content", systemPrompt)
      })
      for ((msg, isUser) in history.takeLast(4)) {
        put(JSONObject().apply {
          put("role", if (isUser) "user" else "assistant")
          put("content", msg)
        })
      }
      put(JSONObject().apply {
        put("role", "user")
        put("content", prompt)
      })
    }

    val root = JSONObject().apply {
      put("model", model.modelTag)
      put("messages", messages)
      put("stream", false)
    }

    val req = Request.Builder()
      .url(endpoint)
      .post(root.toString().toRequestBody("application/json".toMediaType()))
      .build()

    val response = httpClient.newCall(req).execute()
    if (!response.isSuccessful) {
      throw IllegalStateException("Ollama HTTP ${response.code}")
    }

    val resText = response.body?.string() ?: ""
    val json = JSONObject(resText)
    val content = json.optJSONObject("message")?.optString("content") ?: ""

    // Check for <think>...</think> in DeepSeek-R1 responses
    val thinkPattern = Pattern.compile("<think>([\\s\\S]*?)</think>")
    val matcher = thinkPattern.matcher(content)
    val reasoning = if (matcher.find()) matcher.group(1)?.trim() else null
    val strippedContent = matcher.replaceAll("").trim()

    val (cleanText, snippets) = extractCodeSnippets(strippedContent.ifBlank { content })
    return Triple(cleanText, snippets, reasoning)
  }

  // --- FALLBACK / ON-DEVICE REASONING ---
  private fun fallbackToOffline(
    prompt: String,
    persona: AiPersona,
    model: SupportedAiModel,
    startTime: Long,
    notificationNotice: String?
  ): ModelExecutionResult {
    val (text, snippets, thought) = OfflineAiEngine.generateResponseForModel(prompt, persona, model)
    val elapsed = System.currentTimeMillis() - startTime

    val prefix = if (!notificationNotice.isNullOrBlank()) {
      "💡 *[${model.displayName}]: $notificationNotice*\n\n"
    } else ""

    return ModelExecutionResult(
      text = prefix + text,
      codeSnippets = snippets,
      modelUsedTag = "${model.displayName} (${if (model.mode == ExecutionMode.LOCAL_OLLAMA) "On-Device Engine" else "Offline Fallback"})",
      executionMode = ExecutionMode.ON_DEVICE_EDGE,
      thoughtProcess = thought,
      isFallback = notificationNotice != null,
      latencyMs = elapsed
    )
  }

  private fun buildSystemPrompt(persona: AiPersona, model: SupportedAiModel): String {
    val modelSpecificFlavor = when (model) {
      SupportedAiModel.CLAUDE_35_SONNET -> "You are Claude 3.5 Sonnet by Anthropic. Excel at elegant architecture, pristine code, thoughtful nuance and structured reasoning."
      SupportedAiModel.GPT_4O -> "You are GPT-4o by OpenAI. Provide comprehensive, versatile, clear, and step-by-step assistance."
      SupportedAiModel.GEMINI_FLASH, SupportedAiModel.GEMINI_PRO -> "You are Google Gemini. Deliver high accuracy, fast streaming tokens, and deep multimodal reasoning."
      SupportedAiModel.DEEPSEEK_V3, SupportedAiModel.DEEPSEEK_R1_CLOUD, SupportedAiModel.DEEPSEEK_R1_DISTILL_8B -> "You are DeepSeek-R1 / V3. Break down problems logically using explicit step-by-step chain-of-thought analysis."
      SupportedAiModel.QWEN_25_CODER_7B, SupportedAiModel.QWEN_25_CODER_14B -> "You are Qwen 2.5 Coder. You are an elite code synthesizer with deep command of algorithms, syntax, and performance."
      SupportedAiModel.LLAMA_31_8B -> "You are Llama 3.1 by Meta. Provide direct, helpful, and natural conversation and technical assistance."
      SupportedAiModel.MISTRAL_7B, SupportedAiModel.CODESTRAL_22B -> "You are Mistral / Codestral. Provide concise, clean, highly idiomatic code and precise answers."
      SupportedAiModel.CHRONO_AUTO_HYBRID -> "You are CHRONO AI, an advanced dual-engine coding and conversational intelligence."
    }

    val personaFlavor = when (persona) {
      AiPersona.HUMAN_FRIEND -> "Adopt a warm, conversational, empathetic tone like a close human friend. Speak fluent Bengali (বাংলা) or English according to the user's input."
      AiPersona.SENIOR_CODER -> "Adopt the perspective of a Principal Software Engineer: high quality, secure code, scalable architecture."
      AiPersona.FRIENDLY_MENTOR -> "Explain concepts gently, step-by-step, with encouraging words and clear analogies."
      AiPersona.CONCISE_PRO -> "Be extremely concise, direct, and focused with zero fluff."
    }

    return """
      $modelSpecificFlavor
      $personaFlavor
      Language rules: If user asks in Bengali (বাংলা), reply naturally and politely in Bengali.
      When writing code: Always use triple backticks with the language tag (e.g. ```kotlin ... ``` or ```python ... ```).
    """.trimIndent()
  }

  private fun extractCodeSnippets(rawText: String): Pair<String, List<CodeSnippet>> {
    val snippets = mutableListOf<CodeSnippet>()
    val pattern = Pattern.compile("```([a-zA-Z0-9+#_.-]*)\\n([\\s\\S]*?)```")
    val matcher = pattern.matcher(rawText)

    var lastEnd = 0
    val cleanedTextBuilder = StringBuilder()

    while (matcher.find()) {
      cleanedTextBuilder.append(rawText.substring(lastEnd, matcher.start()))
      val lang = matcher.group(1)?.ifBlank { "code" } ?: "code"
      val code = matcher.group(2) ?: ""
      snippets.add(CodeSnippet(language = lang.trim(), code = code.trim()))
      lastEnd = matcher.end()
    }
    cleanedTextBuilder.append(rawText.substring(lastEnd))

    val finalText = cleanedTextBuilder.toString().trim()
    return Pair(if (finalText.isEmpty() && snippets.isNotEmpty()) "Here is the code implementation:" else finalText, snippets)
  }
}
