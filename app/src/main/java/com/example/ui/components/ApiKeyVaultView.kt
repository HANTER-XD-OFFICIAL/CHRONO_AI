package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ApiProvider
import com.example.model.CustomApiKeyEntry
import com.example.ui.ChatViewModel
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoCardSurface
import com.example.ui.theme.ChronoPrimary
import com.example.ui.theme.ChronoSecondary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTextMuted
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary
import kotlinx.coroutines.launch

@Composable
fun ApiKeyVaultView(
  viewModel: ChatViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val config by viewModel.modelConfig.collectAsState()
  val customKeys = config.customApiKeys

  var showAddDialog by remember { mutableStateOf(false) }
  var editingKey by remember { mutableStateOf<CustomApiKeyEntry?>(null) }
  var keyToDelete by remember { mutableStateOf<CustomApiKeyEntry?>(null) }

  // Status map for connection tests: keyId -> Result status string
  val testResults = remember { mutableStateMapOf<String, String>() }
  val testLoading = remember { mutableStateMapOf<String, Boolean>() }

  Column(modifier = modifier.fillMaxWidth()) {
    // Vault Header Summary & Action Button
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = ChronoCardSurface),
      border = BorderStroke(1.dp, ChronoPrimary.copy(alpha = 0.35f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = ChronoPrimary.copy(alpha = 0.15f),
              modifier = Modifier.size(34.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.Key,
                  contentDescription = null,
                  tint = ChronoPrimary,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "UNLIMITED API KEY VAULT",
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = ChronoPrimary
              )
              Text(
                text = "${customKeys.size} টি কাস্টম এপিআই কি সংরক্ষিত আছে",
                fontSize = 11.sp,
                color = ChronoTextSecondary
              )
            }
          }

          Button(
            onClick = {
              editingKey = null
              showAddDialog = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = ChronoPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.testTag("btn_add_api_key")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF003544), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Key", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF003544))
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "আপনি যত ইচ্ছা Google Gemini, OpenAI, Claude, DeepSeek, OpenRouter বা কাস্টম মডেলের এপিআই কি এখানে ম্যানুয়ালি যোগ করতে পারেন। যেকোনো কি এক ক্লিকে সক্রিয় (Active) করা যায়।",
          fontSize = 11.sp,
          color = ChronoTextMuted,
          lineHeight = 15.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Keys List or Empty State
    if (customKeys.isEmpty()) {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ChronoCardSurface.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, ChronoCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Default.Key,
            contentDescription = null,
            tint = ChronoTextMuted,
            modifier = Modifier.size(36.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "এখনও কোনো কাস্টম API কি যোগ করা হয়নি",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ChronoTextPrimary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "নিচের বাটনে চাপ দিয়ে আপনার Gemini বা OpenAI এর এপিআই কি ম্যানুয়ালি যোগ করুন।",
            fontSize = 11.sp,
            color = ChronoTextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedButton(
            onClick = {
              editingKey = null
              showAddDialog = true
            },
            border = BorderStroke(1.dp, ChronoPrimary),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = ChronoPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("প্রথম API কি যোগ করুন", color = ChronoPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    } else {
      val activeKeysCount = customKeys.count { it.isActive }
      if (activeKeysCount > 1) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = ChronoAccent.copy(alpha = 0.12f),
          border = BorderStroke(1.dp, ChronoAccent.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = ChronoAccent,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "⚡ আনলিমিটেড রেসপন্স পুল সক্রিয় ($activeKeysCount টি কি চালু)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ChronoAccent
              )
              Text(
                text = "একাধিক কি চালু থাকায় স্বয়ংক্রিয় রোটেশন ও ফেইলওভার হবে। একটি কি-র রেট লিমিট বা কোটা শেষ হলে পরবর্তী কি কোনো বাধা ছাড়াই সাথে সাথে রেসপন্স দেবে।",
                fontSize = 10.5.sp,
                color = ChronoTextSecondary,
                lineHeight = 14.sp
              )
            }
          }
        }
      }

      Text(
        text = "সংরক্ষিত এপিআই কি তালিকা (MANAGE & ROTATE):",
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        color = ChronoTextSecondary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        customKeys.forEach { entry ->
          ApiKeyItemCard(
            entry = entry,
            testStatus = testResults[entry.id],
            isTesting = testLoading[entry.id] == true,
            onToggleActive = {
              viewModel.configManager.toggleKeyActive(entry.id)
            },
            onTestConnection = {
              coroutineScope.launch {
                testLoading[entry.id] = true
                val res = viewModel.configManager.testApiKeyConnection(entry)
                testLoading[entry.id] = false
                testResults[entry.id] = if (res.isSuccess) {
                  "✓ সক্রিয় ও কানেক্টেড (${res.getOrNull()}ms)"
                } else {
                  "✕ সংযোগ ব্যর্থ: ${res.exceptionOrNull()?.message?.take(30)}"
                }
              }
            },
            onCopy = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("API Key", entry.apiKey)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "${entry.label} কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
            },
            onEdit = {
              editingKey = entry
              showAddDialog = true
            },
            onDelete = {
              keyToDelete = entry
            }
          )
        }
      }
    }
  }

  // Add / Edit Dialog
  if (showAddDialog) {
    AddEditApiKeyDialog(
      initialEntry = editingKey,
      onDismiss = { showAddDialog = false },
      onSave = { newEntry ->
        if (editingKey != null) {
          viewModel.configManager.updateCustomApiKey(newEntry)
          Toast.makeText(context, "API কি সফলভাবে আপডেট করা হয়েছে!", Toast.LENGTH_SHORT).show()
        } else {
          viewModel.configManager.addCustomApiKey(newEntry)
          Toast.makeText(context, "নতুন API কি সফলভাবে যুক্ত করা হয়েছে!", Toast.LENGTH_SHORT).show()
        }
        showAddDialog = false
      }
    )
  }

  // Delete Confirmation Dialog
  keyToDelete?.let { entry ->
    AlertDialog(
      onDismissRequest = { keyToDelete = null },
      containerColor = ChronoSurfaceDark,
      title = {
        Text("API কি মুছে ফেলতে চান?", color = ChronoTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
      },
      text = {
        Text(
          text = "\"${entry.label}\" (${entry.provider.displayName}) সম্পূর্ণ ডিলিট করা হবে। আপনি কি নিশ্চিত?",
          color = ChronoTextSecondary,
          fontSize = 13.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.configManager.deleteCustomApiKey(entry.id)
            keyToDelete = null
            Toast.makeText(context, "API কি মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { keyToDelete = null }) {
          Text("Cancel", color = ChronoTextSecondary)
        }
      }
    )
  }
}

@Composable
fun ApiKeyItemCard(
  entry: CustomApiKeyEntry,
  testStatus: String?,
  isTesting: Boolean,
  onToggleActive: () -> Unit,
  onTestConnection: () -> Unit,
  onCopy: () -> Unit,
  onEdit: () -> Unit,
  onDelete: () -> Unit
) {
  var isKeyRevealed by remember { mutableStateOf(false) }

  val borderColor = if (entry.isActive) ChronoPrimary else ChronoCardBorder
  val bgColor = if (entry.isActive) ChronoPrimary.copy(alpha = 0.08f) else ChronoCardSurface

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = bgColor),
    border = BorderStroke(if (entry.isActive) 1.5.dp else 1.dp, borderColor),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      // Top Row: Title, Provider Badge, Active Toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = entry.label,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = if (entry.isActive) ChronoPrimary else ChronoTextPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = ChronoPrimary.copy(alpha = 0.15f)
            ) {
              Text(
                text = entry.provider.displayName,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = ChronoPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          val modelDisplay = if (entry.customModel.isNotBlank()) entry.customModel else entry.provider.defaultModelTag
          Text(
            text = "Model: $modelDisplay",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = ChronoTextSecondary
          )
        }

        // Active State Toggle Switch
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (entry.isActive) "ACTIVE" else "INACTIVE",
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = if (entry.isActive) ChronoPrimary else ChronoTextMuted
          )
          Spacer(modifier = Modifier.width(6.dp))
          Switch(
            checked = entry.isActive,
            onCheckedChange = { onToggleActive() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color(0xFF003544),
              checkedTrackColor = ChronoPrimary,
              uncheckedThumbColor = ChronoTextMuted,
              uncheckedTrackColor = ChronoSurfaceDark
            ),
            modifier = Modifier.size(36.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Masked API Key Row with Reveal and Copy
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = ChronoSurfaceDark,
        border = BorderStroke(0.5.dp, ChronoCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          val maskedKey = if (entry.apiKey.length > 8) {
            "${entry.apiKey.take(6)}••••••••${entry.apiKey.takeLast(4)}"
          } else {
            "••••••••"
          }

          Text(
            text = if (isKeyRevealed) entry.apiKey else maskedKey,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = if (isKeyRevealed) ChronoAccent else ChronoTextSecondary,
            modifier = Modifier.weight(1f)
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
              onClick = { isKeyRevealed = !isKeyRevealed },
              modifier = Modifier.size(26.dp)
            ) {
              Icon(
                imageVector = if (isKeyRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Visibility",
                tint = ChronoTextSecondary,
                modifier = Modifier.size(16.dp)
              )
            }

            IconButton(
              onClick = onCopy,
              modifier = Modifier.size(26.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Key",
                tint = ChronoPrimary,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }

      // Test Connection Result feedback
      if (isTesting) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          CircularProgressIndicator(
            modifier = Modifier.size(12.dp),
            strokeWidth = 1.5.dp,
            color = ChronoPrimary
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("সার্ভারের সাথে সংযোগ পরীক্ষা করা হচ্ছে...", fontSize = 11.sp, color = ChronoPrimary)
        }
      } else if (testStatus != null) {
        Spacer(modifier = Modifier.height(6.dp))
        val isOk = testStatus.startsWith("✓")
        Text(
          text = testStatus,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = if (isOk) Color(0xFF10B981) else Color(0xFFEF4444)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Bottom Action Bar: Test, Edit, Delete
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Test Ping Button
        OutlinedButton(
          onClick = onTestConnection,
          shape = RoundedCornerShape(6.dp),
          border = BorderStroke(0.8.dp, ChronoPrimary.copy(alpha = 0.6f)),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
          modifier = Modifier.height(28.dp)
        ) {
          Icon(Icons.Default.NetworkCheck, contentDescription = null, tint = ChronoPrimary, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Test Ping", fontSize = 11.sp, color = ChronoPrimary)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ChronoTextSecondary, modifier = Modifier.size(15.dp))
          }
          IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(15.dp))
          }
        }
      }
    }
  }
}

@Composable
fun AddEditApiKeyDialog(
  initialEntry: CustomApiKeyEntry?,
  onDismiss: () -> Unit,
  onSave: (CustomApiKeyEntry) -> Unit
) {
  var selectedProvider by remember {
    mutableStateOf(initialEntry?.provider ?: ApiProvider.GOOGLE_GEMINI)
  }
  var label by remember {
    mutableStateOf(initialEntry?.label ?: "${selectedProvider.displayName} Key")
  }
  var apiKey by remember {
    mutableStateOf(initialEntry?.apiKey ?: "")
  }
  var customModel by remember {
    mutableStateOf(initialEntry?.customModel ?: "")
  }
  var customBaseUrl by remember {
    mutableStateOf(initialEntry?.customBaseUrl ?: "")
  }
  var isActive by remember {
    mutableStateOf(initialEntry?.isActive ?: true)
  }
  var isKeyVisible by remember {
    mutableStateOf(false)
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = ChronoSurfaceDark,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Key, contentDescription = null, tint = ChronoPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (initialEntry != null) "API কি সম্পাদনা করুন" else "নতুন API কি যোগ করুন",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary
        )
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(vertical = 4.dp)
      ) {
        // Provider Selection Chips
        Text(
          text = "১. মডেল প্রোভাইডার নির্বাচন করুন:",
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoPrimary,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          ApiProvider.values().forEach { provider ->
            val isSelected = selectedProvider == provider
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) ChronoPrimary else ChronoCardSurface,
              border = BorderStroke(1.dp, if (isSelected) ChronoPrimary else ChronoCardBorder),
              modifier = Modifier.clickable {
                selectedProvider = provider
                if (initialEntry == null) {
                  label = "${provider.displayName} Key"
                  customModel = provider.defaultModelTag
                }
              }
            ) {
              Text(
                text = provider.displayName,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF003544) else ChronoTextPrimary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Key Name / Label
        Text(
          text = "২. নাম / লেবেল (Label):",
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = label,
          onValueChange = { label = it },
          placeholder = { Text("যেমন: My Gemini Key 1", fontSize = 12.sp) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ChronoPrimary,
            unfocusedBorderColor = ChronoCardBorder,
            focusedTextColor = ChronoTextPrimary,
            unfocusedTextColor = ChronoTextPrimary
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // API Key Input Field
        Text(
          text = "৩. আপনার API Key:",
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = apiKey,
          onValueChange = { apiKey = it },
          placeholder = { Text(selectedProvider.placeholderKey, fontSize = 11.sp) },
          visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { isKeyVisible = !isKeyVisible }) {
              Icon(
                imageVector = if (isKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = null,
                tint = ChronoTextSecondary
              )
            }
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ChronoPrimary,
            unfocusedBorderColor = ChronoCardBorder,
            focusedTextColor = ChronoTextPrimary,
            unfocusedTextColor = ChronoTextPrimary
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Model Tag (Optional / Editable)
        Text(
          text = "৪. মডেল ট্যাগ (ঐচ্ছিক):",
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = customModel,
          onValueChange = { customModel = it },
          placeholder = { Text("ডিফল্ট: ${selectedProvider.defaultModelTag}", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ChronoPrimary,
            unfocusedBorderColor = ChronoCardBorder,
            focusedTextColor = ChronoTextPrimary,
            unfocusedTextColor = ChronoTextPrimary
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Base URL (Optional / Editable)
        Text(
          text = "৫. কাস্টম Base URL (ঐচ্ছিক / প্রক্সি):",
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = customBaseUrl,
          onValueChange = { customBaseUrl = it },
          placeholder = { Text("ডিফল্ট: ${selectedProvider.defaultBaseUrl}", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ChronoPrimary,
            unfocusedBorderColor = ChronoCardBorder,
            focusedTextColor = ChronoTextPrimary,
            unfocusedTextColor = ChronoTextPrimary
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Set as Active Switch
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "এই কি-কে সরাসরি Active হিসেবে নির্ধারণ করুন",
            fontSize = 12.sp,
            color = ChronoTextPrimary
          )
          Switch(
            checked = isActive,
            onCheckedChange = { isActive = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color(0xFF003544),
              checkedTrackColor = ChronoPrimary
            )
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (apiKey.isBlank() && selectedProvider != ApiProvider.OLLAMA_LOCAL_SERVER) {
            return@Button
          }
          val entry = CustomApiKeyEntry(
            id = initialEntry?.id ?: java.util.UUID.randomUUID().toString(),
            label = label.ifBlank { "${selectedProvider.displayName} Key" },
            provider = selectedProvider,
            apiKey = apiKey.trim(),
            customModel = customModel.trim(),
            customBaseUrl = customBaseUrl.trim(),
            isActive = isActive,
            createdAt = initialEntry?.createdAt ?: System.currentTimeMillis()
          )
          onSave(entry)
        },
        enabled = apiKey.isNotBlank() || selectedProvider == ApiProvider.OLLAMA_LOCAL_SERVER,
        colors = ButtonDefaults.buttonColors(containerColor = ChronoPrimary),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text("Save Key", color = Color(0xFF003544), fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = ChronoTextSecondary)
      }
    }
  )
}
