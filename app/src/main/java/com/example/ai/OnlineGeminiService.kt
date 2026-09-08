package com.example.ai

import android.util.Log
import com.example.BuildConfig
import com.example.model.AiPersona
import com.example.model.CodeSnippet
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

object OnlineGeminiService {
  private const val TAG = "OnlineGeminiService"
  // As per skill guidelines: gemini-3.5-flash for general/fast, or gemini-3.1-pro-preview for complex tasks
  private const val MODEL_NAME = "gemini-3.5-flash"
  private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

  private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .build()

  suspend fun generateContent(
    userPrompt: String,
    persona: AiPersona,
    conversationHistory: List<Pair<String, Boolean>> = emptyList()
  ): Result<Pair<String, List<CodeSnippet>>> = withContext(Dispatchers.IO) {
    try {
      val apiKey = BuildConfig.GEMINI_API_KEY

      if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
        return@withContext Result.failure(
          IllegalStateException("Gemini API key is not configured yet. Switching to Autonomous Offline Engine.")
        )
      }

      val systemPrompt = buildSystemPrompt(persona)

      val rootJson = JSONObject()

      // System instructions
      val systemInstructionObj = JSONObject().apply {
        put("parts", JSONArray().apply {
          put(JSONObject().put("text", systemPrompt))
        })
      }
      rootJson.put("systemInstruction", systemInstructionObj)

      // Contents array with history + current prompt
      val contentsArray = JSONArray()

      // Add recent history (up to last 4 turns)
      val recent = conversationHistory.takeLast(6)
      for ((msg, isUser) in recent) {
        val role = if (isUser) "user" else "model"
        val contentObj = JSONObject().apply {
          put("role", role)
          put("parts", JSONArray().apply {
            put(JSONObject().put("text", msg))
          })
        }
        contentsArray.put(contentObj)
      }

      // Add current user prompt
      val currentContentObj = JSONObject().apply {
        put("role", "user")
        put("parts", JSONArray().apply {
          put(JSONObject().put("text", userPrompt))
        })
      }
      contentsArray.put(currentContentObj)

      rootJson.put("contents", contentsArray)

      // Generation config
      val genConfig = JSONObject().apply {
        put("temperature", 0.7)
        put("topP", 0.95)
      }
      rootJson.put("generationConfig", genConfig)

      val requestBody = rootJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

      val request = Request.Builder()
        .url("$BASE_URL?key=$apiKey")
        .post(requestBody)
        .build()

      val response = client.newCall(request).execute()

      if (!response.isSuccessful) {
        val errorBody = response.body?.string() ?: "Unknown error"
        Log.e(TAG, "API Error: ${response.code} - $errorBody")
        return@withContext Result.failure(Exception("Cloud Gemini error (${response.code})"))
      }

      val responseText = response.body?.string() ?: ""
      val jsonResponse = JSONObject(responseText)

      val candidates = jsonResponse.optJSONArray("candidates")
      if (candidates != null && candidates.length() > 0) {
        val firstCandidate = candidates.getJSONObject(0)
        val content = firstCandidate.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        if (parts != null && parts.length() > 0) {
          val generatedText = parts.getJSONObject(0).optString("text", "")
          val (cleanedText, codeSnippets) = extractCodeSnippets(generatedText)
          return@withContext Result.success(Pair(cleanedText, codeSnippets))
        }
      }

      Result.failure(Exception("No valid text received from Gemini."))
    } catch (e: Exception) {
      Log.e(TAG, "Exception calling Gemini API", e)
      Result.failure(e)
    }
  }

  private fun buildSystemPrompt(persona: AiPersona): String {
    val base = """
      You are CHRONO AI, an advanced conversational and coding assistant with a warm, human-like voice, similar to ChatGPT and Google Gemini.
      You can communicate fluently in both Bengali (বাংলা) and English. If the user asks in Bengali, respond in natural, polite, engaging Bengali.
      When writing code:
      - Always wrap code in triple backticks with the language identifier (e.g. ```python ... ```, ```kotlin ... ```, ```javascript ... ```).
      - Provide clean, production-grade, well-commented code.
      - Explain the code clearly step-by-step so anyone can understand.
    """.trimIndent()

    val personaGuide = when (persona) {
      AiPersona.HUMAN_FRIEND -> "Adopt a friendly, empathetic, warm, conversational tone, like a brilliant human friend."
      AiPersona.SENIOR_CODER -> "Adopt the perspective of an elite software engineer: highly structured, clean architecture, best practices."
      AiPersona.FRIENDLY_MENTOR -> "Explain concepts gently, step-by-step, encouraging the learner with clear analogies."
      AiPersona.CONCISE_PRO -> "Give quick, direct, highly focused answers with minimal fluff."
    }

    return "$base\n$personaGuide"
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
    return Pair(if (finalText.isEmpty() && snippets.isNotEmpty()) "Here is the code solution:" else finalText, snippets)
  }
}
