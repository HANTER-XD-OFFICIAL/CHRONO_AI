package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiEngineType
import com.example.model.AiPersona
import com.example.ui.ChatViewModel
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoBackgroundDark
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoCardSurface
import com.example.ui.theme.ChronoPrimary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTextMuted
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary

@Composable
fun ChatScreen(
  viewModel: ChatViewModel,
  modifier: Modifier = Modifier
) {
  val messages by viewModel.messages.collectAsState()
  val isGenerating by viewModel.isGenerating.collectAsState()
  val engineType by viewModel.engineType.collectAsState()
  val persona by viewModel.persona.collectAsState()
  val isOnline by viewModel.isDeviceOnline.collectAsState()
  val selectedModel by viewModel.selectedModel.collectAsState()

  var inputText by remember { mutableStateOf("") }
  var showModelSelectorSheet by remember { mutableStateOf(false) }
  var sheetInitialTab by remember { mutableStateOf(0) }
  var showPersonaMenu by remember { mutableStateOf(false) }

  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  if (showModelSelectorSheet) {
    ModelSelectorBottomSheet(
      viewModel = viewModel,
      initialTab = sheetInitialTab,
      onDismiss = { showModelSelectorSheet = false }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ChronoBackgroundDark)
      .imePadding()
  ) {
    // Engine & Persona Control Header Bar
    Surface(
      color = ChronoSurfaceDark,
      border = BorderStroke(0.5.dp, ChronoCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Multi-Model Selector Pill
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = ChronoCardSurface,
          border = BorderStroke(1.dp, ChronoPrimary.copy(alpha = 0.6f)),
          modifier = Modifier
            .clickable {
              sheetInitialTab = 0
              showModelSelectorSheet = true
            }
            .testTag("btn_engine_selector")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (selectedModel.mode == com.example.model.ExecutionMode.ONLINE_CLOUD) Icons.Default.CloudDone
              else Icons.Default.Cable,
              contentDescription = null,
              tint = if (selectedModel.mode == com.example.model.ExecutionMode.ONLINE_CLOUD) ChronoAccent else Color(0xFFF59E0B),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = selectedModel.displayName,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = ChronoTextPrimary,
              fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "▾",
              fontSize = 11.sp,
              color = ChronoPrimary
            )
          }
        }

        // Persona Selector Pill
        Box {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = ChronoCardSurface,
            border = BorderStroke(1.dp, ChronoCardBorder),
            modifier = Modifier.clickable { showPersonaMenu = true }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = persona.emoji, fontSize = 12.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = persona.displayName,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = ChronoTextSecondary
              )
            }
          }

          DropdownMenu(
            expanded = showPersonaMenu,
            onDismissRequest = { showPersonaMenu = false }
          ) {
            AiPersona.values().forEach { item ->
              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${item.emoji} ${item.displayName}")
                    if (persona == item) {
                      Spacer(modifier = Modifier.width(8.dp))
                      Icon(Icons.Default.Check, contentDescription = null, tint = ChronoPrimary)
                    }
                  }
                },
                onClick = {
                  viewModel.setPersona(item)
                  showPersonaMenu = false
                }
              )
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Direct API Vault Quick Access Button
          IconButton(
            onClick = {
              sheetInitialTab = 2
              showModelSelectorSheet = true
            },
            modifier = Modifier
              .size(32.dp)
              .testTag("btn_quick_api_vault")
          ) {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = "API Key Vault",
              tint = ChronoPrimary,
              modifier = Modifier.size(18.dp)
            )
          }

          // Clear Chat Button
          IconButton(
            onClick = { viewModel.clearChat() },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = "Clear Chat",
              tint = ChronoTextMuted,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }

    // Messages List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      items(messages, key = { it.id }) { message ->
        ChatMessageItem(message = message)
      }

      if (isGenerating) {
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(18.dp),
              color = ChronoPrimary,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = if (engineType == AiEngineType.OFFLINE_CORE) "Thinking on-device..." else "Gemini is generating response & code...",
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              color = ChronoTextSecondary
            )
          }
        }
      }
    }

    // Quick Prompt Suggestions (Horizontal scroll)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF090E17))
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 10.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      val suggestions = listOf(
        "🤝 মানুষের মতো কথা বলো",
        "🐍 Python REST API স্ক্রিপ্ট",
        "📱 Kotlin Compose বাটন কোড",
        "⚡ C++ Merge Sort অ্যালগরিদম",
        "💡 কোডিং শুরু করার উপায় বলো",
        "💻 JavaScript Task Manager"
      )

      suggestions.forEach { prompt ->
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = ChronoSurfaceDark,
          border = BorderStroke(0.5.dp, ChronoCardBorder),
          modifier = Modifier.clickable {
            inputText = prompt
            viewModel.sendMessage(prompt)
            inputText = ""
          }
        ) {
          Text(
            text = prompt,
            fontSize = 11.sp,
            color = ChronoTextSecondary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }
      }
    }

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(ChronoSurfaceDark)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = inputText,
        onValueChange = { inputText = it },
        placeholder = {
          Text(
            text = if (engineType == AiEngineType.OFFLINE_CORE) "Type query or coding request (Offline)..." else "Ask anything or ask for code...",
            fontSize = 13.sp,
            color = ChronoTextMuted
          )
        },
        modifier = Modifier
          .weight(1f)
          .testTag("chat_input_field"),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = ChronoCardSurface,
          unfocusedContainerColor = ChronoCardSurface,
          focusedBorderColor = ChronoPrimary,
          unfocusedBorderColor = ChronoCardBorder,
          focusedTextColor = ChronoTextPrimary,
          unfocusedTextColor = ChronoTextPrimary
        ),
        maxLines = 4,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = {
          if (inputText.isNotBlank()) {
            val textToSend = inputText
            inputText = ""
            viewModel.sendMessage(textToSend)
          }
        })
      )

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = {
          if (inputText.isNotBlank()) {
            val textToSend = inputText
            inputText = ""
            viewModel.sendMessage(textToSend)
          }
        },
        enabled = inputText.isNotBlank() && !isGenerating,
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(if (inputText.isNotBlank() && !isGenerating) ChronoPrimary else ChronoCardBorder)
          .testTag("btn_send_chat")
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = "Send",
          tint = if (inputText.isNotBlank() && !isGenerating) Color(0xFF003544) else ChronoTextMuted,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
