package com.example.model

import java.util.UUID

enum class AiEngineType(val displayName: String, val badge: String) {
  ONLINE_GEMINI("Gemini 3.5 Flash", "Cloud AI"),
  OFFLINE_CORE("CHRONO On-Device", "Offline Engine"),
  AUTO_HYBRID("Smart Hybrid", "Auto Sync")
}

enum class AiPersona(val displayName: String, val description: String, val emoji: String) {
  HUMAN_FRIEND("Human Friend", "কথোপকথন ও মানুষের মতো স্বাভাবিক আলাপ", "😊"),
  SENIOR_CODER("Senior Developer", "কোডিং, আর্কিটেকচার ও নিখুঁত অ্যালগরিদম", "💻"),
  FRIENDLY_MENTOR("Friendly Mentor", "ধাপে ধাপে সহজ ভাষায় ব্যাখ্যা ও গাইড", "🎓"),
  CONCISE_PRO("Fast & Concise", "একদম পয়েন্ট-টু-পয়েন্ট দ্রুত উত্তর", "⚡")
}

data class CodeSnippet(
  val language: String,
  val code: String,
  val explanation: String = ""
)

data class ChatMessage(
  val id: String = UUID.randomUUID().toString(),
  val text: String,
  val isUser: Boolean,
  val timestamp: Long = System.currentTimeMillis(),
  val engineUsed: String = "Offline Core",
  val codeBlocks: List<CodeSnippet> = emptyList(),
  val isError: Boolean = false,
  val latencyMs: Long = 0L
)

data class CodeLibraryItem(
  val id: String,
  val title: String,
  val category: String,
  val language: String,
  val description: String,
  val code: String,
  val complexity: String,
  val tags: List<String>
)
