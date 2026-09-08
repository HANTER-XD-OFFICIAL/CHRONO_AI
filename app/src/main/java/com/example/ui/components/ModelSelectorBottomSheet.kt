package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExecutionMode
import com.example.model.SupportedAiModel
import com.example.ui.ChatViewModel
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoBackgroundDark
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoCardSurface
import com.example.ui.theme.ChronoPrimary
import com.example.ui.theme.ChronoSecondary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTextMuted
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectorBottomSheet(
  viewModel: ChatViewModel,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val selectedModel by viewModel.selectedModel.collectAsState()
  val config by viewModel.modelConfig.collectAsState()

  var selectedTab by remember { mutableStateOf(0) } // 0: Online Cloud, 1: Offline Local, 2: API Keys

  var tempGeminiKey by remember(config.geminiApiKey) { mutableStateOf(config.geminiApiKey) }
  var tempOpenaiKey by remember(config.openaiApiKey) { mutableStateOf(config.openaiApiKey) }
  var tempAnthropicKey by remember(config.anthropicApiKey) { mutableStateOf(config.anthropicApiKey) }
  var tempDeepseekKey by remember(config.deepseekApiKey) { mutableStateOf(config.deepseekApiKey) }
  var tempOllamaUrl by remember(config.ollamaBaseUrl) { mutableStateOf(config.ollamaBaseUrl) }
  var saveSuccessMessage by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = ChronoSurfaceDark,
    tonalElevation = 8.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 18.dp)
        .padding(bottom = 32.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "AI MODEL & ENGINE MATRIX",
            fontSize = 15.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = ChronoPrimary
          )
          Text(
            text = "অনলাইন ক্লাউড ও অফলাইন লোকাল মডেল নির্বাচন",
            fontSize = 12.sp,
            color = ChronoTextSecondary
          )
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = ChronoPrimary.copy(alpha = 0.15f),
          border = BorderStroke(0.8.dp, ChronoPrimary)
        ) {
          Text(
            text = selectedModel.displayName,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = ChronoPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Tabs
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = ChronoBackgroundDark,
        contentColor = ChronoPrimary,
        edgePadding = 0.dp,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = {
            Text(
              text = "🌐 Online Cloud",
              fontSize = 12.sp,
              fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
            )
          }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = {
            Text(
              text = "💻 Offline / Ollama",
              fontSize = 12.sp,
              fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
            )
          }
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = {
            Text(
              text = "🔑 API Keys & Hub",
              fontSize = 12.sp,
              fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
            )
          }
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      when (selectedTab) {
        0 -> {
          // Online Cloud Models List
          LazyColumn(modifier = Modifier.fillMaxWidth().height(380.dp)) {
            items(SupportedAiModel.onlineCloudModels) { model ->
              ModelCardItem(
                model = model,
                isSelected = model == selectedModel,
                onClick = {
                  viewModel.setSelectedModel(model)
                  onDismiss()
                }
              )
              Spacer(modifier = Modifier.height(8.dp))
            }
          }
        }
        1 -> {
          // Offline Local Models List
          LazyColumn(modifier = Modifier.fillMaxWidth().height(380.dp)) {
            item {
              // Smart Autonomous Card
              ModelCardItem(
                model = SupportedAiModel.CHRONO_AUTO_HYBRID,
                isSelected = selectedModel == SupportedAiModel.CHRONO_AUTO_HYBRID,
                onClick = {
                  viewModel.setSelectedModel(SupportedAiModel.CHRONO_AUTO_HYBRID)
                  onDismiss()
                }
              )
              Spacer(modifier = Modifier.height(8.dp))
            }
            items(SupportedAiModel.offlineLocalModels) { model ->
              ModelCardItem(
                model = model,
                isSelected = model == selectedModel,
                onClick = {
                  viewModel.setSelectedModel(model)
                  onDismiss()
                }
              )
              Spacer(modifier = Modifier.height(8.dp))
            }
          }
        }
        2 -> {
          // API Keys & Configuration Tab
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .height(380.dp)
          ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
              item {
                Text(
                  text = "Custom Cloud Keys (Optional - On-Device fallback ready):",
                  fontSize = 11.sp,
                  color = ChronoTextSecondary,
                  fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                ApiKeyInputField(
                  label = "Google Gemini API Key",
                  value = tempGeminiKey,
                  onValueChange = { tempGeminiKey = it },
                  placeholder = "AIzaSy..."
                )
                Spacer(modifier = Modifier.height(8.dp))

                ApiKeyInputField(
                  label = "OpenAI API Key (GPT-4o)",
                  value = tempOpenaiKey,
                  onValueChange = { tempOpenaiKey = it },
                  placeholder = "sk-proj-..."
                )
                Spacer(modifier = Modifier.height(8.dp))

                ApiKeyInputField(
                  label = "Anthropic API Key (Claude 3.5)",
                  value = tempAnthropicKey,
                  onValueChange = { tempAnthropicKey = it },
                  placeholder = "sk-ant-..."
                )
                Spacer(modifier = Modifier.height(8.dp))

                ApiKeyInputField(
                  label = "DeepSeek API Key (V3 / R1)",
                  value = tempDeepseekKey,
                  onValueChange = { tempDeepseekKey = it },
                  placeholder = "sk-..."
                )
                Spacer(modifier = Modifier.height(8.dp))

                ApiKeyInputField(
                  label = "Ollama Host URL (Default: 10.0.2.2:11434)",
                  value = tempOllamaUrl,
                  onValueChange = { tempOllamaUrl = it },
                  placeholder = "http://10.0.2.2:11434",
                  isPassword = false
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (saveSuccessMessage) {
              Text(
                text = "✓ সেটিংস ও এপিআই কি সফলভাবে সংরক্ষিত হয়েছে!",
                fontSize = 12.sp,
                color = ChronoAccent,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
              )
            }

            Button(
              onClick = {
                viewModel.updateApiKeys(
                  geminiKey = tempGeminiKey,
                  openaiKey = tempOpenaiKey,
                  anthropicKey = tempAnthropicKey,
                  deepseekKey = tempDeepseekKey,
                  ollamaUrl = tempOllamaUrl
                )
                saveSuccessMessage = true
              },
              colors = ButtonDefaults.buttonColors(containerColor = ChronoPrimary),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth().testTag("save_api_keys_button")
            ) {
              Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color(0xFF003544))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Save Configuration", color = Color(0xFF003544), fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
fun ModelCardItem(
  model: SupportedAiModel,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) ChronoPrimary.copy(alpha = 0.12f) else ChronoCardSurface
    ),
    border = BorderStroke(
      width = if (isSelected) 1.5.dp else 0.8.dp,
      color = if (isSelected) ChronoPrimary else ChronoCardBorder
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("model_card_${model.id}")
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(
            if (model.mode == ExecutionMode.ONLINE_CLOUD) ChronoPrimary.copy(alpha = 0.15f)
            else Color(0xFFF59E0B).copy(alpha = 0.15f)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (model.mode == ExecutionMode.ONLINE_CLOUD) Icons.Default.Cloud else Icons.Default.Memory,
          contentDescription = null,
          tint = if (model.mode == ExecutionMode.ONLINE_CLOUD) ChronoPrimary else Color(0xFFF59E0B),
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = model.displayName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) ChronoPrimary else ChronoTextPrimary
          )

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (model.mode == ExecutionMode.ONLINE_CLOUD) ChronoPrimary.copy(alpha = 0.12f)
            else Color(0xFFF59E0B).copy(alpha = 0.12f)
          ) {
            Text(
              text = model.provider,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              color = if (model.mode == ExecutionMode.ONLINE_CLOUD) ChronoPrimary else Color(0xFFF59E0B),
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = model.description,
          fontSize = 11.sp,
          color = ChronoTextSecondary,
          lineHeight = 15.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Tag: ${model.modelTag}",
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoTextMuted
        )
      }

      if (isSelected) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Selected",
          tint = ChronoPrimary,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
fun ApiKeyInputField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  isPassword: Boolean = true
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, fontSize = 11.sp) },
    placeholder = { Text(placeholder, fontSize = 11.sp, color = ChronoTextMuted) },
    singleLine = true,
    visualTransformation = if (isPassword && value.isNotEmpty()) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = ChronoPrimary,
      unfocusedBorderColor = ChronoCardBorder,
      focusedLabelColor = ChronoPrimary,
      unfocusedLabelColor = ChronoTextSecondary,
      focusedTextColor = ChronoTextPrimary,
      unfocusedTextColor = ChronoTextPrimary
    ),
    modifier = Modifier.fillMaxWidth()
  )
}
