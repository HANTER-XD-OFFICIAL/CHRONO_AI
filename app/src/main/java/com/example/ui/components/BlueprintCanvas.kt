package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BlueprintNode
import com.example.model.ChronoDataRepository
import com.example.model.NodeCategory
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoBackgroundDark
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

@Composable
fun BlueprintCanvas(
  onNodeSelected: (BlueprintNode) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedNodeId by remember { mutableStateOf<String?>(null) }
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.35f,
    targetValue = 0.95f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Header Banner
    BlueprintHeader(pulseAlpha = pulseAlpha)

    Spacer(modifier = Modifier.height(24.dp))

    // SECTION 1: MODULAR INTEGRATION ADAPTERS (PLUGS)
    Text(
      text = "MODULAR INTEGRATION ADAPTERS (PLUGS)",
      color = ChronoPrimary,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.8.sp,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.testTag("adapters_section_header")
    )
    Text(
      text = "Downstream Client Plugs • Decoupled from Core Logic",
      color = ChronoTextMuted,
      fontSize = 12.sp,
      modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
    )

    // Adapter Plugs Grid (2x2 or row)
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val adapter1 = ChronoDataRepository.adapterPlugs[0]
        val adapter2 = ChronoDataRepository.adapterPlugs[1]
        AdapterPlugCard(
          node = adapter1,
          isSelected = selectedNodeId == adapter1.id,
          icon = Icons.Default.Message,
          plugColor = Color(0xFF229ED9), // Telegram cyan
          onClick = {
            selectedNodeId = adapter1.id
            onNodeSelected(adapter1)
          },
          modifier = Modifier.weight(1f)
        )
        AdapterPlugCard(
          node = adapter2,
          isSelected = selectedNodeId == adapter2.id,
          icon = Icons.Default.Computer,
          plugColor = Color(0xFF10B981), // Web green
          onClick = {
            selectedNodeId = adapter2.id
            onNodeSelected(adapter2)
          },
          modifier = Modifier.weight(1f)
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val adapter3 = ChronoDataRepository.adapterPlugs[2]
        val adapter4 = ChronoDataRepository.adapterPlugs[3]
        AdapterPlugCard(
          node = adapter3,
          isSelected = selectedNodeId == adapter3.id,
          icon = Icons.Default.SmartToy,
          plugColor = Color(0xFFA855F7), // Agent purple
          onClick = {
            selectedNodeId = adapter3.id
            onNodeSelected(adapter3)
          },
          modifier = Modifier.weight(1f)
        )
        AdapterPlugCard(
          node = adapter4,
          isSelected = selectedNodeId == adapter4.id,
          icon = Icons.Default.PhoneAndroid,
          plugColor = Color(0xFFF59E0B), // Mobile amber
          onClick = {
            selectedNodeId = adapter4.id
            onNodeSelected(adapter4)
          },
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Bus Connectors flow to Hybrid Hub
    ConnectorFlow(
      label = "Standardized Adapter Contracts ⇄ Bidirectional Dispatch",
      pulseAlpha = pulseAlpha
    )

    // SECTION 2: FLEXIBLE API LAYER (CENTRAL HUB BOX)
    val hub = ChronoDataRepository.hybridCommunicationLayer
    HybridHubCard(
      node = hub,
      isSelected = selectedNodeId == hub.id,
      pulseAlpha = pulseAlpha,
      onClick = {
        selectedNodeId = hub.id
        onNodeSelected(hub)
      }
    )

    // Bus Connectors flow to Core Services
    ConnectorFlow(
      label = "Unified Internal IPC / Micro-Kernel Dispatch Bus",
      pulseAlpha = pulseAlpha
    )

    // SECTION 3: MODULAR CORE LOGIC (CHRONO CORE SERVICES BOX)
    val coreServices = ChronoDataRepository.coreServices
    ChronoCoreServicesCard(
      coreNode = coreServices,
      selectedNodeId = selectedNodeId,
      onSubModuleClick = { subNode ->
        selectedNodeId = subNode.id
        onNodeSelected(subNode)
      },
      onCoreClick = {
        selectedNodeId = coreServices.id
        onNodeSelected(coreServices)
      }
    )

    // Base Support Connectors
    ConnectorFlow(
      label = "Strict Interface Boundary • Zero Core Modification",
      pulseAlpha = pulseAlpha
    )

    // SECTION 4: CENTRAL FOUNDATION LAYER
    val foundation = ChronoDataRepository.foundationCore
    FoundationLayerCard(
      node = foundation,
      isSelected = selectedNodeId == foundation.id,
      onClick = {
        selectedNodeId = foundation.id
        onNodeSelected(foundation)
      }
    )

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun BlueprintHeader(pulseAlpha: Float) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
    border = BorderStroke(1.dp, ChronoCardBorder),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("blueprint_header_card")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(ChronoAccent.copy(alpha = pulseAlpha))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "CHRONO ARCHITECTURAL BLUEPRINT",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ChronoPrimary
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Multi-Modal AI Platform Framework",
          fontSize = 17.sp,
          fontWeight = FontWeight.SemiBold,
          color = ChronoTextPrimary
        )
        Text(
          text = "Wall Display Schematic • Adaptable Client System",
          fontSize = 12.sp,
          color = ChronoTextSecondary
        )
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = ChronoCardSurface,
        border = BorderStroke(1.dp, ChronoCardBorder)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Cable,
            contentDescription = null,
            tint = ChronoPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "MODULAR PLUGS",
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = ChronoTextPrimary
          )
        }
      }
    }
  }
}

@Composable
private fun AdapterPlugCard(
  node: BlueprintNode,
  isSelected: Boolean,
  icon: ImageVector,
  plugColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val borderColor by animateColorAsState(
    targetValue = if (isSelected) ChronoPrimary else ChronoCardBorder,
    label = "borderColor"
  )

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) ChronoCardSurface else ChronoSurfaceDark
    ),
    border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
    modifier = modifier
      .height(130.dp)
      .clickable(onClick = onClick)
      .testTag("adapter_plug_${node.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(plugColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = node.title,
            tint = plugColor,
            modifier = Modifier.size(20.dp)
          )
        }

        // Plug indicator tag
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = plugColor.copy(alpha = 0.2f),
          border = BorderStroke(0.5.dp, plugColor)
        ) {
          Text(
            text = "PLUG",
            color = plugColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
          )
        }
      }

      Column {
        Text(
          text = node.title,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary,
          lineHeight = 15.sp,
          maxLines = 2
        )
        Text(
          text = node.subtitle,
          fontSize = 10.sp,
          color = ChronoTextSecondary,
          maxLines = 1
        )
      }
    }
  }
}

@Composable
private fun HybridHubCard(
  node: BlueprintNode,
  isSelected: Boolean,
  pulseAlpha: Float,
  onClick: () -> Unit
) {
  val borderColor by animateColorAsState(
    targetValue = if (isSelected) ChronoPrimary else ChronoSecondary.copy(alpha = 0.8f),
    label = "hubBorder"
  )

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
    border = BorderStroke(1.5.dp, borderColor),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("hybrid_hub_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(
              ChronoSecondary.copy(alpha = 0.08f),
              Color.Transparent
            )
          )
        )
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(ChronoSecondary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Router,
              contentDescription = null,
              tint = ChronoSecondary,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "FLEXIBLE API LAYER",
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoSecondary
            )
            Text(
              text = node.title,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = ChronoTextPrimary
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = ChronoAccent.copy(alpha = 0.15f),
          border = BorderStroke(0.5.dp, ChronoAccent)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(ChronoAccent.copy(alpha = pulseAlpha))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "DUAL MODE",
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoAccent
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "Central Hub routing between online cloud endpoints and local edge execution. Isolates external client contracts from core cognitive logic.",
        fontSize = 12.sp,
        color = ChronoTextSecondary,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        node.subModules.forEach { module ->
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = ChronoCardSurface,
            border = BorderStroke(0.5.dp, ChronoCardBorder),
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = module,
              fontSize = 10.sp,
              color = ChronoTextPrimary,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
              maxLines = 1
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ChronoCoreServicesCard(
  coreNode: BlueprintNode,
  selectedNodeId: String?,
  onSubModuleClick: (BlueprintNode) -> Unit,
  onCoreClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
    border = BorderStroke(1.5.dp, ChronoPrimary.copy(alpha = 0.8f)),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("chrono_core_services_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(
              ChronoPrimary.copy(alpha = 0.08f),
              Color.Transparent
            )
          )
        )
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onCoreClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(ChronoPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = ChronoPrimary,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "MODULAR CORE LOGIC",
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary
            )
            Text(
              text = coreNode.title,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = ChronoTextPrimary
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = ChronoPrimary.copy(alpha = 0.15f),
          border = BorderStroke(0.5.dp, ChronoPrimary)
        ) {
          Text(
            text = "3 SUB-MODULES",
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = ChronoPrimary,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // SUB-MODULE 1: CASUAL DIALOGUE MODEL
      val sub1 = ChronoDataRepository.casualDialogueSubModule
      SubModuleItemCard(
        node = sub1,
        isSelected = selectedNodeId == sub1.id,
        icon = Icons.Default.Forum,
        accent = Color(0xFF38BDF8),
        onClick = { onSubModuleClick(sub1) }
      )

      Spacer(modifier = Modifier.height(8.dp))

      // SUB-MODULE 2: MULTI-LANGUAGE CODING ENGINE
      val sub2 = ChronoDataRepository.codingEngineSubModule
      SubModuleItemCard(
        node = sub2,
        isSelected = selectedNodeId == sub2.id,
        icon = Icons.Default.Code,
        accent = Color(0xFFA78BFA),
        onClick = { onSubModuleClick(sub2) }
      )

      Spacer(modifier = Modifier.height(8.dp))

      // SUB-MODULE 3: CONTEXTUAL MEMORY MANAGER
      val sub3 = ChronoDataRepository.memoryManagerSubModule
      SubModuleItemCard(
        node = sub3,
        isSelected = selectedNodeId == sub3.id,
        icon = Icons.Default.Memory,
        accent = Color(0xFF34D399),
        onClick = { onSubModuleClick(sub3) }
      )
    }
  }
}

@Composable
private fun SubModuleItemCard(
  node: BlueprintNode,
  isSelected: Boolean,
  icon: ImageVector,
  accent: Color,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) ChronoCardSurface else ChronoCardSurface.copy(alpha = 0.6f)
    ),
    border = BorderStroke(
      if (isSelected) 1.5.dp else 0.8.dp,
      if (isSelected) accent else ChronoCardBorder
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("submodule_${node.id}")
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
          .background(accent.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = accent,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = node.title,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary
        )
        Text(
          text = node.subtitle,
          fontSize = 10.sp,
          color = ChronoTextSecondary
        )
      }
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Active",
        tint = accent,
        modifier = Modifier.size(16.dp)
      )
    }
  }
}

@Composable
private fun FoundationLayerCard(
  node: BlueprintNode,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val borderColor by animateColorAsState(
    targetValue = if (isSelected) ChronoPrimary else ChronoCardBorder,
    label = "foundationBorder"
  )

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
    border = BorderStroke(1.5.dp, borderColor),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("foundation_layer_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(
              ChronoTertiary.copy(alpha = 0.08f),
              Color.Transparent
            )
          )
        )
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(ChronoTertiary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Layers,
              contentDescription = null,
              tint = ChronoTertiary,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "CENTRAL FOUNDATION LAYER",
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoTertiary
            )
            Text(
              text = node.title,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = ChronoTextPrimary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Guarantees that downstream clients adapt freely without ever touching internal core engine logic or shared memory runtime.",
        fontSize = 12.sp,
        color = ChronoTextSecondary,
        lineHeight = 16.sp
      )
    }
  }
}

@Composable
private fun ConnectorFlow(label: String, pulseAlpha: Float) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .width(2.dp)
        .height(14.dp)
        .background(
          Brush.verticalGradient(
            listOf(
              ChronoPrimary.copy(alpha = pulseAlpha),
              ChronoSecondary.copy(alpha = pulseAlpha)
            )
          )
        )
    )

    Surface(
      shape = RoundedCornerShape(12.dp),
      color = ChronoBackgroundDark,
      border = BorderStroke(0.5.dp, ChronoCardBorder)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Sensors,
          contentDescription = null,
          tint = ChronoPrimary.copy(alpha = pulseAlpha),
          modifier = Modifier.size(11.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = label,
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoTextMuted
        )
      }
    }

    Box(
      modifier = Modifier
        .width(2.dp)
        .height(14.dp)
        .background(
          Brush.verticalGradient(
            listOf(
              ChronoSecondary.copy(alpha = pulseAlpha),
              ChronoPrimary.copy(alpha = pulseAlpha)
            )
          )
        )
    )
  }
}
