package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.AdapterSimulationItem
import com.example.model.ChronoDataRepository
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoCardSurface
import com.example.ui.theme.ChronoPrimary
import com.example.ui.theme.ChronoSecondary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTertiary
import com.example.ui.theme.ChronoTextMuted
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary

@Composable
fun AdapterRoutingSimulatorView(
  modifier: Modifier = Modifier
) {
  var selectedIndex by remember { mutableIntStateOf(0) }
  var isDispatched by remember { mutableStateOf(false) }
  val activeItem = ChronoDataRepository.simulationEvents[selectedIndex]

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Simulator Header
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
      border = BorderStroke(1.dp, ChronoCardBorder),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("simulator_header_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Cable,
              contentDescription = null,
              tint = ChronoPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ADAPTER DISPATCH PIPELINE",
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary
            )
          }

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = ChronoAccent.copy(alpha = 0.15f),
            border = BorderStroke(0.5.dp, ChronoAccent)
          ) {
            Text(
              text = "DECOUPLED ROUTING",
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoAccent,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Zero-Core Modification Simulator",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary
        )
        Text(
          text = "Simulate how client requests route from modular adapter plugs to CHRONO Core Services without modifying core logic.",
          fontSize = 12.sp,
          color = ChronoTextSecondary,
          lineHeight = 17.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Select Active Client Adapter
    Text(
      text = "SELECT CLIENT ADAPTER TO TEST",
      fontSize = 11.sp,
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.Bold,
      color = ChronoPrimary,
      modifier = Modifier.align(Alignment.Start)
    )

    Spacer(modifier = Modifier.height(8.dp))

    // 4 Adapter selector pills
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      ChronoDataRepository.simulationEvents.forEachIndexed { index, item ->
        val isSelected = selectedIndex == index
        val icon = when (item.adapterName) {
          "TELEGRAM BOT ADAPTER" -> Icons.Default.Message
          "WEB-CHAT INTEGRATION" -> Icons.Default.Computer
          "CUSTOM AGENT ADAPTER" -> Icons.Default.SmartToy
          else -> Icons.Default.PhoneAndroid
        }

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
            .clickable {
              selectedIndex = index
              isDispatched = false
            }
            .testTag("sim_select_${item.id}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
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
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = item.adapterName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ChronoTextPrimary
              )
              Text(
                text = "${item.clientType} • ${item.onlineOfflineStatus}",
                fontSize = 10.sp,
                color = ChronoTextSecondary
              )
            }
            if (isSelected) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected",
                tint = ChronoPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Inbound Request Payload Card
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
      border = BorderStroke(1.dp, ChronoCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "INBOUND CLIENT EVENT PAYLOAD",
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          color = ChronoSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF090D16))
            .border(BorderStroke(0.5.dp, ChronoCardBorder), RoundedCornerShape(8.dp))
            .padding(10.dp)
        ) {
          Text(
            text = activeItem.payloadSample,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = ChronoAccent
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Dispatch Button
    Button(
      onClick = { isDispatched = true },
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(containerColor = ChronoPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("btn_dispatch_simulation")
    ) {
      Icon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = null,
        tint = Color(0xFF003544),
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "Dispatch Through Hybrid Layer",
        fontWeight = FontWeight.Bold,
        color = Color(0xFF003544)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Routing Execution Flow Details (Visible when dispatched)
    AnimatedVisibility(
      visible = isDispatched,
      enter = fadeIn() + slideInVertically()
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // STEP 1: Adapter Normalization
        SimulationStepCard(
          stepNumber = "1",
          title = "Adapter Normalization (Modular Plug)",
          detail = "${activeItem.adapterName} deserialized native payload into canonical CHRONO Event Struct.",
          status = "Adapter Isolated",
          color = ChronoSecondary
        )

        Icon(
          imageVector = Icons.Default.ArrowDownward,
          contentDescription = null,
          tint = ChronoCardBorder,
          modifier = Modifier
            .size(20.dp)
            .align(Alignment.CenterHorizontally)
        )

        // STEP 2: Hybrid Communication Layer
        SimulationStepCard(
          stepNumber = "2",
          title = "HYBRID COMMUNICATION LAYER (Online/Offline)",
          detail = "Routed via ${activeItem.onlineOfflineStatus}. Latency: ${activeItem.latency}.",
          status = "Decoupling Hub Passed",
          color = ChronoPrimary
        )

        Icon(
          imageVector = Icons.Default.ArrowDownward,
          contentDescription = null,
          tint = ChronoCardBorder,
          modifier = Modifier
            .size(20.dp)
            .align(Alignment.CenterHorizontally)
        )

        // STEP 3: Core Service Execution
        SimulationStepCard(
          stepNumber = "3",
          title = "CHRONO CORE: ${activeItem.targetCoreModule}",
          detail = "Sub-module processed tensor vectors and generated response. Central foundation logic remained 100% untouched.",
          status = "Core Engine Stable",
          color = ChronoAccent
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Decoupling Certification Box
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = ChronoCardSurface),
          border = BorderStroke(1.dp, ChronoAccent.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = ChronoAccent,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "ZERO-CORE MODIFICATION CERTIFIED",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = ChronoAccent
              )
              Text(
                text = "The CHRONO Framework Core and internal algorithms did not require any modification to service this ${activeItem.adapterName} transaction.",
                fontSize = 12.sp,
                color = ChronoTextSecondary,
                lineHeight = 16.sp
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun SimulationStepCard(
  stepNumber: String,
  title: String,
  detail: String,
  status: String,
  color: Color
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
    border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.2f))
          .border(1.dp, color, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stepNumber,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = color,
          fontFamily = FontFamily.Monospace
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ChronoTextPrimary
          )
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = color.copy(alpha = 0.15f)
          ) {
            Text(
              text = status,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              color = color,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = detail,
          fontSize = 11.sp,
          color = ChronoTextSecondary,
          lineHeight = 15.sp
        )
      }
    }
  }
}
