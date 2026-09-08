package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoCardSurface
import com.example.ui.theme.ChronoGlow
import com.example.ui.theme.ChronoPrimary
import com.example.ui.theme.ChronoSecondary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTertiary
import com.example.ui.theme.ChronoTextMuted
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface

data class ChatMessage(
  val sender: String,
  val text: String,
  val isUser: Boolean,
  val timestamp: String,
  val engineTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassmorphicClientPreview(
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var inputQuery by remember { mutableStateOf("") }
  val messages = remember {
    mutableStateListOf(
      ChatMessage(
        sender = "User (Mobile Client)",
        text = "How does CHRONO adapt to Telegram and Web-Chat simultaneously?",
        isUser = true,
        timestamp = "10:42 AM",
        engineTag = "Inbound Adapter"
      ),
      ChatMessage(
        sender = "CHRONO Assistant",
        text = "Through the Hybrid Communication Layer! The core dialogue and coding logic remains completely agnostic to the transport layer. Telegram webhooks and Web-Chat WebSockets both deserialize into universal CHRONO semantic vectors.",
        isUser = false,
        timestamp = "10:42 AM",
        engineTag = "CASUAL DIALOGUE MODEL"
      )
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .background(
        Brush.radialGradient(
          colors = listOf(
            Color(0xFF1E1B4B).copy(alpha = 0.4f),
            Color(0xFF0F172A).copy(alpha = 0.9f),
            Color(0xFF090D16)
          )
        )
      )
      .padding(horizontal = 16.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Glass Concept Header
    GlassCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("glass_concept_header")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = ChronoPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "DOWNSTREAM CLIENT CONCEPT",
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary
            )
          }

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color(0x3338BDF8),
            border = BorderStroke(0.5.dp, ChronoPrimary)
          ) {
            Text(
              text = "GLASSMORPHISM UI",
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Peripheral Device UI Prototype",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary
        )
        Text(
          text = "Demonstrating the intended final user experience running on peripheral studio tablets and laptops via CHRONO adapters.",
          fontSize = 12.sp,
          color = ChronoTextSecondary,
          lineHeight = 17.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Interactive Mode Selector Tabs
    val tabs = listOf("Casual Dialogue", "Coding Engine", "Memory Vault")
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color.Transparent,
      contentColor = ChronoPrimary,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = ChronoPrimary,
          height = 2.dp
        )
      },
      divider = {},
      modifier = Modifier.fillMaxWidth()
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontSize = 12.sp,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
              color = if (selectedTab == index) ChronoPrimary else ChronoTextSecondary
            )
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    when (selectedTab) {
      0 -> CasualDialogueGlassView(
        messages = messages,
        inputQuery = inputQuery,
        onInputChange = { inputQuery = it },
        onSendMessage = {
          if (inputQuery.isNotBlank()) {
            val userText = inputQuery
            messages.add(
              ChatMessage(
                sender = "Client User",
                text = userText,
                isUser = true,
                timestamp = "Just now",
                engineTag = "MOBILE CLIENT ADAPTER"
              )
            )
            inputQuery = ""

            // Automated response through Core Dialogue
            messages.add(
              ChatMessage(
                sender = "CHRONO Core",
                text = "Processed through Casual Dialogue Model! The unified memory manager indexed your query and verified multi-turn intent consistency.",
                isUser = false,
                timestamp = "Just now",
                engineTag = "CASUAL DIALOGUE MODEL"
              )
            )
          }
        }
      )
      1 -> CodingEngineGlassView()
      2 -> MemoryVaultGlassView()
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun GlassCard(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0x1F1E293B))
      .border(
        BorderStroke(1.dp, Color(0x33FFFFFF)),
        RoundedCornerShape(16.dp)
      )
  ) {
    content()
  }
}

@Composable
private fun CasualDialogueGlassView(
  messages: List<ChatMessage>,
  inputQuery: String,
  onInputChange: (String) -> Unit,
  onSendMessage: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    messages.forEach { msg ->
      GlassCard(
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = msg.sender,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (msg.isUser) ChronoSecondary else ChronoPrimary
            )
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color(0x1AFFFFFF),
              border = BorderStroke(0.5.dp, Color(0x33FFFFFF))
            ) {
              Text(
                text = msg.engineTag,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = ChronoAccent,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = msg.text,
            fontSize = 13.sp,
            color = ChronoTextPrimary,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Input Bar
    GlassCard(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = inputQuery,
          onValueChange = onInputChange,
          placeholder = {
            Text(
              text = "Ask CHRONO Casual Dialogue Model...",
              fontSize = 12.sp,
              color = ChronoTextMuted
            )
          },
          colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = ChronoTextPrimary,
            unfocusedTextColor = ChronoTextPrimary
          ),
          modifier = Modifier
            .weight(1f)
            .testTag("glass_input_query")
        )

        IconButton(
          onClick = onSendMessage,
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(ChronoPrimary)
            .testTag("glass_send_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = Color(0xFF003544),
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun CodingEngineGlassView() {
  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Code,
            contentDescription = null,
            tint = ChronoTertiary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "MULTI-LANGUAGE CODING ENGINE",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = ChronoTertiary
          )
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = ChronoTertiary.copy(alpha = 0.2f)
        ) {
          Text(
            text = "AST VERIFIED",
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            color = ChronoTertiary,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "Sub-module active: Performing real-time abstract syntax analysis, multi-language translation, and security linting across downstream clients.",
        fontSize = 12.sp,
        color = ChronoTextSecondary
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Code Preview Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF070B13))
          .border(BorderStroke(0.8.dp, Color(0x33FFFFFF)), RoundedCornerShape(8.dp))
          .padding(12.dp)
      ) {
        Text(
          text = "// CHRONO Decoupled Adapter Interface\ninterface ChronoAdapter {\n  suspend fun onInboundEvent(event: ClientPayload): CoreResponse\n  fun getAdapterCapabilities(): Set<Capability>\n}",
          fontFamily = FontFamily.Monospace,
          fontSize = 11.sp,
          color = ChronoAccent,
          lineHeight = 16.sp
        )
      }
    }
  }
}

@Composable
private fun MemoryVaultGlassView() {
  GlassCard(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Memory,
            contentDescription = null,
            tint = ChronoAccent,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "CONTEXTUAL MEMORY MANAGER",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = ChronoAccent
          )
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = ChronoAccent.copy(alpha = 0.2f)
        ) {
          Text(
            text = "GRAPH INDEXED",
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            color = ChronoAccent,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "Episodic and Semantic memory layer linking client requests across Telegram, Web, and Mobile into a coherent session context.",
        fontSize = 12.sp,
        color = ChronoTextSecondary
      )

      Spacer(modifier = Modifier.height(12.dp))

      val memoryItems = listOf(
        "Active Session: #CHRONO-STUDIO-SESSION-94",
        "Retained Entities: ['Telegram Bot', 'Web-Chat', 'AST Compiler']",
        "Memory Persistence: Encrypted Local Cache & Vector Index"
      )

      memoryItems.forEach { item ->
        Row(
          modifier = Modifier.padding(vertical = 3.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(ChronoAccent)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = item,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = ChronoTextPrimary
          )
        }
      }
    }
  }
}
