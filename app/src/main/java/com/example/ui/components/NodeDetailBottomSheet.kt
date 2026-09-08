package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BlueprintNode
import com.example.model.NodeCategory
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeDetailBottomSheet(
  node: BlueprintNode,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = ChronoSurfaceDark,
    contentColor = ChronoTextPrimary,
    dragHandle = null
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 24.dp)
        .testTag("node_detail_sheet")
    ) {
      // Top bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        val categoryColor = when (node.category) {
          NodeCategory.FOUNDATION -> ChronoTertiary
          NodeCategory.CORE_SERVICE -> ChronoPrimary
          NodeCategory.COMMUNICATION_HUB -> ChronoSecondary
          NodeCategory.ADAPTER_PLUG -> ChronoAccent
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = categoryColor.copy(alpha = 0.15f),
          border = BorderStroke(0.8.dp, categoryColor)
        ) {
          Text(
            text = node.category.name.replace("_", " "),
            color = categoryColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("close_sheet_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = ChronoTextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = node.title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = ChronoTextPrimary,
        lineHeight = 26.sp
      )
      Text(
        text = node.subtitle,
        fontSize = 13.sp,
        color = ChronoTextSecondary
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Status pill
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(ChronoCardSurface)
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(ChronoAccent)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = node.status,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = ChronoTextPrimary
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider(color = ChronoCardBorder)
      Spacer(modifier = Modifier.height(16.dp))

      // Architecture Description
      Text(
        text = "ARCHITECTURAL OVERVIEW",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        color = ChronoPrimary
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = node.description,
        fontSize = 14.sp,
        color = ChronoTextSecondary,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Decoupling Feature Highlight Card
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ChronoCardSurface),
        border = BorderStroke(1.dp, ChronoPrimary.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Cable,
              contentDescription = null,
              tint = ChronoPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "DECOUPLING & ADAPTABILITY CONTRACT",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = ChronoPrimary
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = node.decouplingFeature,
            fontSize = 13.sp,
            color = ChronoTextPrimary,
            lineHeight = 18.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Technical Specifications
      Text(
        text = "TECHNICAL SPECIFICATIONS",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        color = ChronoPrimary
      )
      Spacer(modifier = Modifier.height(8.dp))

      node.specifications.forEach { spec ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = null,
            tint = ChronoAccent,
            modifier = Modifier
              .size(16.dp)
              .padding(top = 2.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = spec,
            fontSize = 13.sp,
            color = ChronoTextSecondary,
            lineHeight = 18.sp
          )
        }
      }

      if (node.subModules.isNotEmpty()) {
        Spacer(modifier = Modifier.height(18.dp))
        Text(
          text = "INTERNAL MODULE PIPELINE",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          color = ChronoPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          node.subModules.forEach { sub ->
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = ChronoCardSurface,
              border = BorderStroke(0.5.dp, ChronoCardBorder),
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = sub,
                fontSize = 10.sp,
                color = ChronoTextPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onDismiss,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ChronoPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text(
          text = "Return to Blueprint",
          color = Color(0xFF003544),
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
