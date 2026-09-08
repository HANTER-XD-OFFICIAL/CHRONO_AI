package com.example.model

enum class NodeCategory {
  FOUNDATION,
  CORE_SERVICE,
  COMMUNICATION_HUB,
  ADAPTER_PLUG
}

data class BlueprintNode(
  val id: String,
  val title: String,
  val subtitle: String,
  val category: NodeCategory,
  val description: String,
  val specifications: List<String>,
  val decouplingFeature: String,
  val status: String,
  val subModules: List<String> = emptyList(),
  val iconType: String = "core"
)

data class StudioHotspot(
  val id: String,
  val title: String,
  val tag: String,
  val description: String,
  val xRatio: Float,
  val yRatio: Float
)

data class AdapterSimulationItem(
  val id: String,
  val adapterName: String,
  val clientType: String,
  val payloadSample: String,
  val targetCoreModule: String,
  val latency: String,
  val onlineOfflineStatus: String
)

object ChronoDataRepository {
  val foundationCore = BlueprintNode(
    id = "chrono_core_foundation",
    title = "CHRONO FRAMEWORK: MULTI-PLATFORM AI INTEGRATION CORE",
    subtitle = "Central Architectural Foundation Layer",
    category = NodeCategory.FOUNDATION,
    description = "The universal abstraction bedrock of CHRONO. Provides unified state scheduling, hardware acceleration bridges, and standardized protocol contracts that guarantee downstream clients can be added or swapped with zero modification to central core logic.",
    specifications = listOf(
      "Decoupled Inversion-of-Control (IoC) architecture",
      "Unified Tokenizer & Embeddings Pipeline",
      "Cross-Platform Thread Pool & Memory Virtualization",
      "Zero-core modification plugin registry"
    ),
    decouplingFeature = "Strict interface segregation: adapters only interact via contract schemas, isolating core engine updates from client-specific SDKs.",
    status = "Operational • 99.99% Core Stability",
    subModules = listOf(
      "Central Plugin Registry",
      "Event Dispatch Bus",
      "Security & Sandbox Enclave"
    )
  )

  val coreServices = BlueprintNode(
    id = "chrono_core_services",
    title = "CHRONO CORE SERVICES",
    subtitle = "Modular Core Logic Engine",
    category = NodeCategory.CORE_SERVICE,
    description = "The central cognitive engine housing high-order reasoning, analysis, and temporal persistence. Encapsulates three specialized sub-modules engineered for multi-modal processing.",
    specifications = listOf(
      "High-concurrency neural execution runtime",
      "Shared vector space embedding layer",
      "Sub-millisecond inter-process message bus"
    ),
    decouplingFeature = "Exposes uniform RPC & Reactive Stream contracts to the Hybrid Communication Layer.",
    status = "Active Runtime • Multi-Modal Ready",
    subModules = listOf(
      "CASUAL DIALOGUE MODEL (Natural Human Language)",
      "MULTI-LANGUAGE CODING ENGINE (Analysis & Generation)",
      "CONTEXTUAL MEMORY MANAGER"
    )
  )

  val casualDialogueSubModule = BlueprintNode(
    id = "sub_dialogue",
    title = "CASUAL DIALOGUE MODEL",
    subtitle = "Natural Human Language Sub-Module",
    category = NodeCategory.CORE_SERVICE,
    description = "Specialized LLM runtime fine-tuned for fluent natural conversation, emotional tone adaptation, intent extraction, and multi-turn persona consistency across any client adapter.",
    specifications = listOf(
      "Sub-20ms first-token streaming latency",
      "Adaptive tone modulation (Formal, Casual, Technical)",
      "Multilingual tokenization with phonetic alignment"
    ),
    decouplingFeature = "Outputs universal semantic tokens that adapt automatically to chat text, voice synthesis, or telegram formatters.",
    status = "Sub-Module Online",
    subModules = listOf("Intent Parser", "Tone Conditioner", "Safety Filter")
  )

  val codingEngineSubModule = BlueprintNode(
    id = "sub_coding",
    title = "MULTI-LANGUAGE CODING ENGINE",
    subtitle = "Analysis & Generation Sub-Module",
    category = NodeCategory.CORE_SERVICE,
    description = "Deep code synthesis, Abstract Syntax Tree (AST) parsing, automated vulnerability scanning, and multi-paradigm trans-compilation across Kotlin, Rust, Python, Go, and TypeScript.",
    specifications = listOf(
      "Grammar-constrained generation pipeline",
      "Multi-file repository context indexing",
      "Unit test synthesis & static verification"
    ),
    decouplingFeature = "Language-agnostic AST payload exchange; adapters receive structured syntax blocks without code parsing overhead.",
    status = "Sub-Module Online",
    subModules = listOf("AST Parser", "Code Synthesizer", "Vulnerability Verifier")
  )

  val memoryManagerSubModule = BlueprintNode(
    id = "sub_memory",
    title = "CONTEXTUAL MEMORY MANAGER",
    subtitle = "Temporal Storage & Retrieval Sub-Module",
    category = NodeCategory.CORE_SERVICE,
    description = "Hybrid episodic and semantic memory engine. Combines vector embeddings with graph knowledge networks to maintain user context over extended conversations and multi-session workflows.",
    specifications = listOf(
      "Hierarchical episodic decay & summarization",
      "Cross-session entity graph linking",
      "Local on-device cache with encrypted persistence"
    ),
    decouplingFeature = "Session tokens map client IDs securely to isolated memory vaults without exposing underlying database schemas.",
    status = "Sub-Module Online",
    subModules = listOf("Vector Store", "Knowledge Graph", "Decay Scheduler")
  )

  val hybridCommunicationLayer = BlueprintNode(
    id = "chrono_comm_hub",
    title = "HYBRID COMMUNICATION LAYER (Online/Offline)",
    subtitle = "Flexible API Central Hub Layer",
    category = NodeCategory.COMMUNICATION_HUB,
    description = "The bidirectional communication nexus connecting all peripheral adapter plugs with CHRONO Core Services. Seamlessly bridges cloud streaming endpoints and local offline edge models.",
    specifications = listOf(
      "Intelligent offline-to-cloud automatic fallback",
      "gRPC / WebSocket / ZeroMQ unified gateway",
      "Dynamic payload compression & encryption (E2E)",
      "Packet deduplication & offline queue sync"
    ),
    decouplingFeature = "Serves as the ultimate architectural decoupling buffer. Upstream plugs know only the Hybrid Hub interface; the core knows only the internal dispatch bus.",
    status = "Hub Active • Dual Mode (Online / Edge)",
    subModules = listOf(
      "Offline Sync Store",
      "Adaptive Route Selector",
      "Protocol Transcoder"
    )
  )

  val adapterPlugs = listOf(
    BlueprintNode(
      id = "adapter_telegram",
      title = "TELEGRAM BOT ADAPTER",
      subtitle = "Downstream Messaging Plug",
      category = NodeCategory.ADAPTER_PLUG,
      description = "Transforms incoming Telegram Webhook and Long-polling payloads into CHRONO unified input vectors, and formats rich inline buttons, MarkdownV2 text, and media responses.",
      specifications = listOf(
        "Telegram Bot API v7.0+ compliant",
        "Inline command mapping & callback handlers",
        "Rate-limiting token bucket with anti-flood"
      ),
      decouplingFeature = "Adding or updating Telegram API features requires editing only this adapter; CHRONO core services remain 100% untouched.",
      status = "Plugged In • Polling Active",
      iconType = "telegram"
    ),
    BlueprintNode(
      id = "adapter_webchat",
      title = "WEB-CHAT INTEGRATION",
      subtitle = "Streaming Web Client Plug",
      category = NodeCategory.ADAPTER_PLUG,
      description = "WebSocket and Server-Sent Events (SSE) connector providing ultra-low-latency bidirectional streaming directly into browser web apps and web-based studio dashboards.",
      specifications = listOf(
        "Standardized JSON-RPC 2.0 and SSE transport",
        "JWT session validation & CORS policy manager",
        "Stream chunking with typing indicators"
      ),
      decouplingFeature = "Handles browser socket lifecycle, reconnections, and token auth independently from backend inference.",
      status = "Plugged In • SSE Connected",
      iconType = "web"
    ),
    BlueprintNode(
      id = "adapter_custom_agent",
      title = "CUSTOM AGENT ADAPTER",
      subtitle = "Autonomous Tool & Orchestration Plug",
      category = NodeCategory.ADAPTER_PLUG,
      description = "Interface for secondary autonomous AI agents, enterprise pipelines, and external API microservices requiring automated function calling, tool execution, and chain-of-thought routing.",
      specifications = listOf(
        "OpenAPI 3.1 & Model Context Protocol (MCP) support",
        "Sandboxed tool execution invocation",
        "Granular permission verification engine"
      ),
      decouplingFeature = "Allows third-party agents to plug into CHRONO without exposing raw weights or modifying memory subsystems.",
      status = "Plugged In • MCP Ready",
      iconType = "agent"
    ),
    BlueprintNode(
      id = "adapter_mobile_app",
      title = "MOBILE APP (UI CLIENT)",
      subtitle = "Native Touch & Edge Client Plug",
      category = NodeCategory.ADAPTER_PLUG,
      description = "High-performance native client adapter optimized for Android, iOS, and foldable devices. Bridges touch gestures, voice inputs, and local edge acceleration directly with the Hybrid Hub.",
      specifications = listOf(
        "Zero-copy memory transfer with NDK support",
        "Offline local-model fallback handshake",
        "Adaptive window size & glassmorphism UI bridge"
      ),
      decouplingFeature = "Decoupled mobile UI design can iterate and update on its own release schedule without server dependency.",
      status = "Plugged In • Low Latency Edge",
      iconType = "mobile"
    )
  )

  val studioHotspots = listOf(
    StudioHotspot(
      id = "spot_display",
      title = "Wall-Mounted Architectural Display",
      tag = "Main Focus",
      description = "Large high-definition interactive wall screen presenting the CHRONO Framework Blueprint with real-time telemetry, plug modularity, and core status.",
      xRatio = 0.50f,
      yRatio = 0.38f
    ),
    StudioHotspot(
      id = "spot_peripheral_glass",
      title = "Peripheral Glassmorphic UI Workstations",
      tag = "Downstream Clients",
      description = "Collaborative design monitors and tablets running the translucent glassmorphism UI concept, demonstrating the user-facing experience powered by CHRONO.",
      xRatio = 0.78f,
      yRatio = 0.65f
    ),
    StudioHotspot(
      id = "spot_collaborators",
      title = "Collaborative Google Studio Team",
      tag = "Engineers & Designers",
      description = "Cross-functional team collaborating on modular adapter integration, testing plug-and-play contracts without modifying core code.",
      xRatio = 0.22f,
      yRatio = 0.68f
    )
  )

  val simulationEvents = listOf(
    AdapterSimulationItem(
      id = "sim_1",
      adapterName = "TELEGRAM BOT ADAPTER",
      clientType = "Mobile Messenger",
      payloadSample = "\"/code Refactor Kotlin coroutine dispatcher\"",
      targetCoreModule = "MULTI-LANGUAGE CODING ENGINE",
      latency = "14ms",
      onlineOfflineStatus = "Online (gRPC Cloud)"
    ),
    AdapterSimulationItem(
      id = "sim_2",
      adapterName = "WEB-CHAT INTEGRATION",
      clientType = "Browser Studio Session",
      payloadSample = "\"Summarize meeting action items from context\"",
      targetCoreModule = "CONTEXTUAL MEMORY MANAGER",
      latency = "18ms",
      onlineOfflineStatus = "Online (SSE Stream)"
    ),
    AdapterSimulationItem(
      id = "sim_3",
      adapterName = "MOBILE APP (UI CLIENT)",
      clientType = "Android On-Device Client",
      payloadSample = "\"What is the upcoming release timeline?\"",
      targetCoreModule = "CASUAL DIALOGUE MODEL",
      latency = "8ms",
      onlineOfflineStatus = "Offline (Local Edge)"
    ),
    AdapterSimulationItem(
      id = "sim_4",
      adapterName = "CUSTOM AGENT ADAPTER",
      clientType = "Autonomous MCP Agent",
      payloadSample = "\"Execute automated security audit on adapter contract\"",
      targetCoreModule = "MULTI-LANGUAGE CODING ENGINE",
      latency = "22ms",
      onlineOfflineStatus = "Online (ZeroMQ Hub)"
    )
  )
}
