package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.AiEngineType
import com.example.model.BlueprintNode
import com.example.ui.components.BlueprintCanvas
import com.example.ui.components.ChatScreen
import com.example.ui.components.CodeLibraryView
import com.example.ui.components.DualEngineDiagnosticsView
import com.example.ui.components.NodeDetailBottomSheet
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoBackgroundDark
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoCardSurface
import com.example.ui.theme.ChronoPrimary
import com.example.ui.theme.ChronoSecondary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary

enum class ChronoNavScreen(val title: String, val icon: ImageVector) {
  CHAT("CHRONO AI", Icons.Default.AutoAwesome),
  CODE_VAULT("Code Vault", Icons.Default.Code),
  DUAL_ENGINE("Dual Engine", Icons.Default.DeviceHub),
  BLUEPRINT("Architecture", Icons.Default.Layers)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  modifier: Modifier = Modifier,
  chatViewModel: ChatViewModel = viewModel()
) {
  var currentScreen by remember { mutableStateOf(ChronoNavScreen.CHAT) }
  var selectedNodeForDetail by remember { mutableStateOf<BlueprintNode?>(null) }

  val isOnline by chatViewModel.isDeviceOnline.collectAsState()
  val engineType by chatViewModel.engineType.collectAsState()
  val selectedModel by chatViewModel.selectedModel.collectAsState()

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = ChronoBackgroundDark,
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      TopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.chrono_icon),
              contentDescription = "CHRONO Logo",
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .border(1.2.dp, ChronoPrimary, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "CHRONO AI",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = ChronoTextPrimary,
                  fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = if (selectedModel.mode == com.example.model.ExecutionMode.ONLINE_CLOUD) ChronoPrimary.copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f),
                  border = BorderStroke(0.5.dp, if (selectedModel.mode == com.example.model.ExecutionMode.ONLINE_CLOUD) ChronoPrimary else Color(0xFFF59E0B))
                ) {
                  Text(
                    text = selectedModel.displayName.take(15),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedModel.mode == com.example.model.ExecutionMode.ONLINE_CLOUD) ChronoPrimary else Color(0xFFF59E0B),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = "${selectedModel.provider} • ${selectedModel.mode.badge}",
                fontSize = 10.sp,
                color = ChronoTextSecondary
              )
            }
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = ChronoSurfaceDark,
            border = BorderStroke(0.8.dp, ChronoCardBorder),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .clip(CircleShape)
                  .background(if (isOnline) ChronoAccent else Color(0xFFF59E0B))
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = if (isOnline) "ONLINE" else "OFFLINE",
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (isOnline) ChronoAccent else Color(0xFFF59E0B)
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = ChronoBackgroundDark,
          titleContentColor = ChronoTextPrimary
        )
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = ChronoSurfaceDark,
        contentColor = ChronoTextPrimary,
        tonalElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("chrono_bottom_navigation")
      ) {
        ChronoNavScreen.values().forEach { screen ->
          val isSelected = currentScreen == screen
          NavigationBarItem(
            selected = isSelected,
            onClick = { currentScreen = screen },
            icon = {
              Icon(
                imageVector = screen.icon,
                contentDescription = screen.title
              )
            },
            label = {
              Text(
                text = screen.title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color(0xFF003544),
              selectedTextColor = ChronoPrimary,
              indicatorColor = ChronoPrimary,
              unselectedIconColor = ChronoTextSecondary,
              unselectedTextColor = ChronoTextSecondary
            ),
            modifier = Modifier.testTag("nav_${screen.name.lowercase()}")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        ChronoNavScreen.CHAT -> {
          ChatScreen(viewModel = chatViewModel)
        }
        ChronoNavScreen.CODE_VAULT -> {
          CodeLibraryView(
            onSendToChat = { prompt ->
              currentScreen = ChronoNavScreen.CHAT
              chatViewModel.sendMessage(prompt)
            }
          )
        }
        ChronoNavScreen.DUAL_ENGINE -> {
          DualEngineDiagnosticsView(viewModel = chatViewModel)
        }
        ChronoNavScreen.BLUEPRINT -> {
          BlueprintCanvas(
            onNodeSelected = { node -> selectedNodeForDetail = node }
          )
        }
      }

      // Bottom Sheet Details Inspector (When clicking blueprint nodes)
      selectedNodeForDetail?.let { node ->
        NodeDetailBottomSheet(
          node = node,
          onDismiss = { selectedNodeForDetail = null }
        )
      }
    }
  }
}
