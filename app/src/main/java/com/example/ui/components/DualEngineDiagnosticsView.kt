package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.theme.ChronoSecondary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTextMuted
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary

@Composable
fun DualEngineDiagnosticsView(
  viewModel: ChatViewModel,
  modifier: Modifier = Modifier
) {
  val engineType by viewModel.engineType.collectAsState()
  val persona by viewModel.persona.collectAsState()
  val isOnline by viewModel.isDeviceOnline.collectAsState()
  val lastEngine by viewModel.lastUsedEngine.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ChronoBackgroundDark)
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    // Header
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
      border = BorderStroke(1.dp, ChronoCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.DeviceHub,
              contentDescription = null,
              tint = ChronoPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "DUAL-ENGINE CONTROL",
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary
            )
          }

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (isOnline) ChronoAccent.copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f),
            border = BorderStroke(0.5.dp, if (isOnline) ChronoAccent else Color(0xFFF59E0B))
          ) {
            Text(
              text = if (isOnline) "INTERNET CONNECTED" else "OFFLINE MODE ACTIVE",
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = if (isOnline) ChronoAccent else Color(0xFFF59E0B),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Hybrid AI Architecture Hub",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary
        )
        Text(
          text = "Seamless switching between Cloud Google Gemini 3.5 Flash and Autonomous On-Device Neural Engine.",
          fontSize = 12.sp,
          color = ChronoTextSecondary,
          lineHeight = 17.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "ENGINE SWITCHER (অনলাইন ও অফলাইন)",
      fontSize = 11.sp,
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.Bold,
      color = ChronoPrimary
    )

    Spacer(modifier = Modifier.height(8.dp))

    // 3 Engine Cards
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      EngineSelectionCard(
        title = "Smart Auto-Hybrid (Recommended)",
        badge = "Adaptive",
        description = "Uses Google Gemini when online; automatically falls back to On-Device Engine if internet drops.",
        isSelected = engineType == AiEngineType.AUTO_HYBRID,
        icon = Icons.Default.Cable,
        onClick = { viewModel.setEngineType(AiEngineType.AUTO_HYBRID) }
      )

      EngineSelectionCard(
        title = "100% Offline On-Device Engine",
        badge = "Zero Internet",
        description = "Direct embedded intelligence. 0ms network lag, completely private, fully functional offline.",
        isSelected = engineType == AiEngineType.OFFLINE_CORE,
        icon = Icons.Default.CloudOff,
        onClick = { viewModel.setEngineType(AiEngineType.OFFLINE_CORE) }
      )

      EngineSelectionCard(
        title = "Google Gemini 3.5 Cloud Engine",
        badge = "Deep Reasoning",
        description = "Connects to Google Generative AI for maximum multi-turn context and large-scale code synthesis.",
        isSelected = engineType == AiEngineType.ONLINE_GEMINI,
        icon = Icons.Default.CloudDone,
        onClick = { viewModel.setEngineType(AiEngineType.ONLINE_GEMINI) }
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "CONVERSATION PERSONA (হিউম্যান স্টাইল)",
      fontSize = 11.sp,
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.Bold,
      color = ChronoPrimary
    )

    Spacer(modifier = Modifier.height(8.dp))

    // 4 Persona Selector Pills
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      AiPersona.values().forEach { item ->
        val isSelected = persona == item
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ChronoCardSurface else ChronoSurfaceDark
          ),
          border = BorderStroke(
            if (isSelected) 1.5.dp else 0.8.dp,
            if (isSelected) ChronoPrimary else ChronoCardBorder
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.setPersona(item) }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = item.emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = item.displayName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ChronoTextPrimary
              )
              Text(
                text = item.description,
                fontSize = 11.sp,
                color = ChronoTextSecondary
              )
            }
            if (isSelected) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ChronoPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Live Metrics Card
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
      border = BorderStroke(1.dp, ChronoCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "TELEMETRY & ACTIVE STATE",
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          color = ChronoSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Active Engine Route:", fontSize = 12.sp, color = ChronoTextSecondary)
          Text(text = lastEngine, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ChronoAccent)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Network Connectivity:", fontSize = 12.sp, color = ChronoTextSecondary)
          Text(
            text = if (isOnline) "Online (WiFi/Cellular)" else "Offline (Local Only)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isOnline) ChronoAccent else Color(0xFFF59E0B)
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Offline Code Templates:", fontSize = 12.sp, color = ChronoTextSecondary)
          Text(text = "20+ Ready (Python, Kotlin, JS, C++, SQL)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ChronoTextPrimary)
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Refresh Network Test Button
    Button(
      onClick = { viewModel.checkNetworkStatus() },
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(containerColor = ChronoPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .height(46.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = null,
        tint = Color(0xFF003544),
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "Test Network Connectivity",
        fontWeight = FontWeight.Bold,
        color = Color(0xFF003544)
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun EngineSelectionCard(
  title: String,
  badge: String,
  description: String,
  isSelected: Boolean,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) ChronoCardSurface else ChronoSurfaceDark
    ),
    border = BorderStroke(
      if (isSelected) 1.5.dp else 0.8.dp,
      if (isSelected) ChronoPrimary else ChronoCardBorder
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(
            if (isSelected) ChronoPrimary.copy(alpha = 0.2f)
            else ChronoCardBorder.copy(alpha = 0.3f)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (isSelected) ChronoPrimary else ChronoTextSecondary,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ChronoTextPrimary
          )
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (isSelected) ChronoPrimary.copy(alpha = 0.15f) else Color(0xFF0F172A),
            border = BorderStroke(0.5.dp, if (isSelected) ChronoPrimary else ChronoCardBorder)
          ) {
            Text(
              text = badge,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              color = if (isSelected) ChronoPrimary else ChronoTextSecondary,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = description,
          fontSize = 11.sp,
          color = ChronoTextSecondary,
          lineHeight = 15.sp
        )
      }
    }
  }
}
