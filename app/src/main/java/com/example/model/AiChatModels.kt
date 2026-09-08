package com.example.model

import java.util.UUID

enum class ExecutionMode(val label: String, val badge: String) {
  ONLINE_CLOUD("Online Cloud API", "Cloud"),
  LOCAL_OLLAMA("Offline via Ollama/Local", "Local/Edge"),
  ON_DEVICE_EDGE("On-Device Embedded", "Edge Core")
}

enum class ModelFamily(val title: String) {
  ANTHROPIC("Anthropic"),
  OPENAI("OpenAI"),
  GOOGLE("Google"),
  DEEPSEEK_CLOUD("DeepSeek Cloud"),
  OLLAMA_LOCAL("Ollama / Local Engine"),
  CHRONO_EDGE("CHRONO Autonomous")
}

enum class ApiProvider(
  val displayName: String,
  val family: ModelFamily,
  val defaultBaseUrl: String,
  val placeholderKey: String,
  val defaultModelTag: String,
  val iconKey: String
) {
  GOOGLE_GEMINI(
    displayName = "Google Gemini",
    family = ModelFamily.GOOGLE,
    defaultBaseUrl = "https://generativelanguage.googleapis.com",
    placeholderKey = "AQ.Ab8... or AIzaSy...",
    defaultModelTag = "gemini-1.5-flash",
    iconKey = "google"
  ),
  OPENAI(
    displayName = "OpenAI",
    family = ModelFamily.OPENAI,
    defaultBaseUrl = "https://api.openai.com/v1",
    placeholderKey = "sk-proj-...",
    defaultModelTag = "gpt-4o",
    iconKey = "openai"
  ),
  ANTHROPIC(
    displayName = "Anthropic Claude",
    family = ModelFamily.ANTHROPIC,
    defaultBaseUrl = "https://api.anthropic.com/v1",
    placeholderKey = "sk-ant-...",
    defaultModelTag = "claude-3-5-sonnet-20241022",
    iconKey = "claude"
  ),
  DEEPSEEK(
    displayName = "DeepSeek Cloud",
    family = ModelFamily.DEEPSEEK_CLOUD,
    defaultBaseUrl = "https://api.deepseek.com",
    placeholderKey = "sk-...",
    defaultModelTag = "deepseek-chat",
    iconKey = "deepseek"
  ),
  OPENROUTER(
    displayName = "OpenRouter",
    family = ModelFamily.OPENAI,
    defaultBaseUrl = "https://openrouter.ai/api/v1",
    placeholderKey = "sk-or-...",
    defaultModelTag = "auto",
    iconKey = "openrouter"
  ),
  OLLAMA_LOCAL_SERVER(
    displayName = "Ollama Local Engine",
    family = ModelFamily.OLLAMA_LOCAL,
    defaultBaseUrl = "http://10.0.2.2:11434",
    placeholderKey = "None / Custom Token",
    defaultModelTag = "qwen2.5-coder:7b",
    iconKey = "ollama"
  ),
  CUSTOM_ENDPOINT(
    displayName = "Custom OpenAI Compatible",
    family = ModelFamily.OPENAI,
    defaultBaseUrl = "https://api.yourdomain.com/v1",
    placeholderKey = "Custom API Bearer Token",
    defaultModelTag = "custom-model",
    iconKey = "custom"
  );

  companion object {
    fun fromString(value: String): ApiProvider {
      return values().firstOrNull { it.name.equals(value, ignoreCase = true) } ?: GOOGLE_GEMINI
    }
  }
}

data class CustomApiKeyEntry(
  val id: String = UUID.randomUUID().toString(),
  val label: String,
  val provider: ApiProvider,
  val apiKey: String,
  val customModel: String = "",
  val customBaseUrl: String = "",
  val isActive: Boolean = true,
  val createdAt: Long = System.currentTimeMillis()
)

enum class SupportedAiModel(
  val id: String,
  val displayName: String,
  val provider: String,
  val modelTag: String,
  val mode: ExecutionMode,
  val family: ModelFamily,
  val description: String,
  val categoryLabel: String,
  val iconKey: String
) {
  // --- ONLINE CLOUD APIS ---
  CLAUDE_35_SONNET(
    id = "claude-3-5-sonnet",
    displayName = "Claude 3.5 Sonnet",
    provider = "Anthropic",
    modelTag = "claude-3-5-sonnet-20241022",
    mode = ExecutionMode.ONLINE_CLOUD,
    family = ModelFamily.ANTHROPIC,
    description = "শীর্ষস্থানীয় কোডিং, সিস্টেম ডিজাইন ও জটিল রিজনিং সক্ষমতা",
    categoryLabel = "Online Cloud API",
    iconKey = "claude"
  ),
  GPT_4O(
    id = "gpt-4o",
    displayName = "GPT-4o",
    provider = "OpenAI",
    modelTag = "gpt-4o",
    mode = ExecutionMode.ONLINE_CLOUD,
    family = ModelFamily.OPENAI,
    description = "অমনি-মাল্টিমোডাল মডেল, দ্রুত রেসপন্স ও কথোপকথনে পারদর্শী",
    categoryLabel = "Online Cloud API",
    iconKey = "openai"
  ),
  GEMINI_FLASH(
    id = "gemini-flash",
    displayName = "Gemini 1.5/3.5 Flash",
    provider = "Google Gemini",
    modelTag = "gemini-3.5-flash",
    mode = ExecutionMode.ONLINE_CLOUD,
    family = ModelFamily.GOOGLE,
    description = "তড়িৎ গতি, সাশ্রয়ী টোকেন ও নির্ভরযোগ্য রিয়েল-টাইম বুদ্ধি",
    categoryLabel = "Online Cloud API",
    iconKey = "google"
  ),
  GEMINI_PRO(
    id = "gemini-pro",
    displayName = "Gemini 1.5/3.1 Pro",
    provider = "Google Gemini",
    modelTag = "gemini-3.1-pro-preview",
    mode = ExecutionMode.ONLINE_CLOUD,
    family = ModelFamily.GOOGLE,
    description = "গভীর যুক্তি, গণিত ও সম্পূর্ণ সফটওয়্যার ডেভেলপমেন্ট",
    categoryLabel = "Online Cloud API",
    iconKey = "google"
  ),
  DEEPSEEK_V3(
    id = "deepseek-v3",
    displayName = "DeepSeek-V3",
    provider = "DeepSeek Cloud",
    modelTag = "deepseek-chat",
    mode = ExecutionMode.ONLINE_CLOUD,
    family = ModelFamily.DEEPSEEK_CLOUD,
    description = "৬৭১ বিলিয়ন প্যারামিটারের স্টেট-অফ-দ্য-আর্ট ক্লাউড এআই",
    categoryLabel = "Online Cloud API",
    iconKey = "deepseek"
  ),
  DEEPSEEK_R1_CLOUD(
    id = "deepseek-r1-cloud",
    displayName = "DeepSeek-R1 (Cloud)",
    provider = "DeepSeek Cloud",
    modelTag = "deepseek-reasoner",
    mode = ExecutionMode.ONLINE_CLOUD,
    family = ModelFamily.DEEPSEEK_CLOUD,
    description = "অ্যাডভান্সড চেইন-অব-থট (<think>) যুক্ত বিশুদ্ধ রিজনিং মডেল",
    categoryLabel = "Online Cloud API",
    iconKey = "deepseek"
  ),

  // --- OFFLINE LOCAL MODELS (OLLAMA / LOCAL ENGINES) ---
  QWEN_25_CODER_7B(
    id = "qwen-2.5-coder-7b",
    displayName = "Qwen 2.5 Coder 7B",
    provider = "Alibaba / Ollama",
    modelTag = "qwen2.5-coder:7b",
    mode = ExecutionMode.LOCAL_OLLAMA,
    family = ModelFamily.OLLAMA_LOCAL,
    description = "অফলাইন লোকাল কোডিং স্পেশালিস্ট, দ্রুত অ্যালগরিদম ও সিনট্যাক্স",
    categoryLabel = "Offline via Ollama",
    iconKey = "qwen"
  ),
  QWEN_25_CODER_14B(
    id = "qwen-2.5-coder-14b",
    displayName = "Qwen 2.5 Coder 14B",
    provider = "Alibaba / Ollama",
    modelTag = "qwen2.5-coder:14b",
    mode = ExecutionMode.LOCAL_OLLAMA,
    family = ModelFamily.OLLAMA_LOCAL,
    description = "উচ্চ প্যারামিটারের অফলাইন সফটওয়্যার আর্কিটেকচার ইঞ্জিন",
    categoryLabel = "Offline via Ollama",
    iconKey = "qwen"
  ),
  DEEPSEEK_R1_DISTILL_8B(
    id = "deepseek-r1-distill-8b",
    displayName = "DeepSeek-R1 Distill 8B",
    provider = "DeepSeek / Ollama",
    modelTag = "deepseek-r1:8b",
    mode = ExecutionMode.LOCAL_OLLAMA,
    family = ModelFamily.OLLAMA_LOCAL,
    description = "লোকাল ডিভাইসে ধাপে ধাপে চিন্তা করে সমাধান বের করার মডেল",
    categoryLabel = "Offline via Ollama",
    iconKey = "deepseek"
  ),
  LLAMA_31_8B(
    id = "llama-3.1-8b",
    displayName = "Llama 3.1 8B",
    provider = "Meta / Ollama",
    modelTag = "llama3.1:8b",
    mode = ExecutionMode.LOCAL_OLLAMA,
    family = ModelFamily.OLLAMA_LOCAL,
    description = "বহুমুখী কথোপকথন, বিশ্বকোষ জ্ঞান ও নির্দেশিকা অনুসরণ",
    categoryLabel = "Offline via Ollama",
    iconKey = "meta"
  ),
  MISTRAL_7B(
    id = "mistral-7b",
    displayName = "Mistral 7B",
    provider = "Mistral AI / Ollama",
    modelTag = "mistral:7b",
    mode = ExecutionMode.LOCAL_OLLAMA,
    family = ModelFamily.OLLAMA_LOCAL,
    description = "হাই স্পিড ইউরোপীয় ওপেন সোর্স জেনারেল ইন্টেলিজেন্স",
    categoryLabel = "Offline via Ollama",
    iconKey = "mistral"
  ),
  CODESTRAL_22B(
    id = "codestral-22b",
    displayName = "Codestral 22B",
    provider = "Mistral AI / Ollama",
    modelTag = "codestral:22b",
    mode = ExecutionMode.LOCAL_OLLAMA,
    family = ModelFamily.OLLAMA_LOCAL,
    description = "অ্যাডভান্সড কোড কমপ্লিশন, টেস্ট জেনারেশন ও রিফ্যাক্টরিং",
    categoryLabel = "Offline via Ollama",
    iconKey = "mistral"
  ),
  CHRONO_AUTO_HYBRID(
    id = "chrono-hybrid",
    displayName = "CHRONO Smart Hybrid",
    provider = "CHRONO System",
    modelTag = "smart-hybrid-v1",
    mode = ExecutionMode.ON_DEVICE_EDGE,
    family = ModelFamily.CHRONO_EDGE,
    description = "নেটওয়ার্ক ও কাজের ধরণের উপর ভিত্তি করে স্বয়ংক্রিয় মডেল রাউটিং",
    categoryLabel = "Smart Autonomous",
    iconKey = "chrono"
  );

  companion object {
    fun fromId(id: String): SupportedAiModel {
      return values().firstOrNull { it.id == id } ?: CHRONO_AUTO_HYBRID
    }

    val onlineCloudModels = listOf(
      CLAUDE_35_SONNET,
      GPT_4O,
      GEMINI_FLASH,
      GEMINI_PRO,
      DEEPSEEK_V3,
      DEEPSEEK_R1_CLOUD
    )

    val offlineLocalModels = listOf(
      QWEN_25_CODER_7B,
      QWEN_25_CODER_14B,
      DEEPSEEK_R1_DISTILL_8B,
      LLAMA_31_8B,
      MISTRAL_7B,
      CODESTRAL_22B
    )
  }
}

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
  val latencyMs: Long = 0L,
  val thoughtProcess: String? = null
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
