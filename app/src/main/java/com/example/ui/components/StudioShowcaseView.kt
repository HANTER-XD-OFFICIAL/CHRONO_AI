package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ChronoDataRepository
import com.example.model.StudioHotspot
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
fun StudioShowcaseView(
  onNavigateToBlueprint: () -> Unit,
  onNavigateToGlassClient: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedHotspot by remember { mutableStateOf<StudioHotspot?>(ChronoDataRepository.studioHotspots.first()) }
  val infiniteTransition = rememberInfiniteTransition(label = "hotspotPulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Studio Environment Header
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
      border = BorderStroke(1.dp, ChronoCardBorder),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("studio_showcase_header")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(ChronoAccent)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "GOOGLE STUDIO ENVIRONMENT",
              fontFamily = FontFamily.Monospace,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary
            )
          }

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = ChronoCardSurface,
            border = BorderStroke(0.5.dp, ChronoCardBorder)
          ) {
            Text(
              text = "WALL DISPLAY ARCHITECTURE",
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoTextSecondary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "CHRONO Collaborative Studio",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary
        )
        Text(
          text = "Large interactive display showing modular adapters branching out from CHRONO Core Services.",
          fontSize = 13.sp,
          color = ChronoTextSecondary,
          lineHeight = 18.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Interactive Image Canvas with Hotspots
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
      border = BorderStroke(1.5.dp, ChronoPrimary.copy(alpha = 0.6f)),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("studio_image_card")
    ) {
      BoxWithConstraints(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 10f)
      ) {
        val width = maxWidth
        val height = maxHeight

        Image(
          painter = painterResource(id = R.drawable.chrono_architecture_studio),
          contentDescription = "Modern Google Studio Development Environment with Wall Display for CHRONO Framework",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Gradient vignette overlay for contrast
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  Color.Black.copy(alpha = 0.2f),
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.65f)
                )
              )
            )
        )

        // Interactive Hotspot Markers
        ChronoDataRepository.studioHotspots.forEach { hotspot ->
          val isSelected = selectedHotspot?.id == hotspot.id
          val xOffset = width * hotspot.xRatio - 18.dp
          val yOffset = height * hotspot.yRatio - 18.dp

          Box(
            modifier = Modifier
              .offset(x = xOffset, y = yOffset)
              .size(36.dp)
              .clickable { selectedHotspot = hotspot }
              .testTag("hotspot_${hotspot.id}"),
            contentAlignment = Alignment.Center
          ) {
            // Pulsing ring
            Box(
              modifier = Modifier
                .size(if (isSelected) 36.dp * pulseScale else 28.dp)
                .clip(CircleShape)
                .background(
                  (if (isSelected) ChronoPrimary else ChronoAccent).copy(alpha = 0.25f)
                )
            )

            // Inner badge
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (isSelected) ChronoPrimary else ChronoCardSurface)
                .border(
                  1.5.dp,
                  if (isSelected) Color.White else ChronoAccent,
                  CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.RadioButtonChecked,
                contentDescription = hotspot.title,
                tint = if (isSelected) Color(0xFF003544) else ChronoAccent,
                modifier = Modifier.size(12.dp)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Hotspot Info Card
    selectedHotspot?.let { spot ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ChronoCardSurface),
        border = BorderStroke(1.dp, ChronoPrimary.copy(alpha = 0.5f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("selected_hotspot_info")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = ChronoPrimary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = spot.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ChronoTextPrimary
              )
            }

            Surface(
              shape = RoundedCornerShape(4.dp),
              color = ChronoPrimary.copy(alpha = 0.15f),
              border = BorderStroke(0.5.dp, ChronoPrimary)
            ) {
              Text(
                text = spot.tag,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = ChronoPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = spot.description,
            fontSize = 13.sp,
            color = ChronoTextSecondary,
            lineHeight = 18.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Action Navigation Buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = onNavigateToBlueprint,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ChronoPrimary),
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("btn_explore_blueprint")
      ) {
        Icon(
          imageVector = Icons.Default.Layers,
          contentDescription = null,
          tint = Color(0xFF003544),
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Inspect Blueprint",
          color = Color(0xFF003544),
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }

      OutlinedButton(
        onClick = onNavigateToGlassClient,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, ChronoSecondary),
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("btn_preview_glass")
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = ChronoSecondary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Glassmorphism UI",
          color = ChronoSecondary,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
