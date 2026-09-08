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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoCardSurface
import com.example.ui.theme.ChronoPrimary
import com.example.ui.theme.ChronoSurfaceDark
import com.example.ui.theme.ChronoTextMuted
import com.example.ui.theme.ChronoTextPrimary
import com.example.ui.theme.ChronoTextSecondary

@Composable
fun ChatMessageItem(
  message: ChatMessage,
  modifier: Modifier = Modifier
) {
  val isUser = message.isUser

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
    verticalAlignment = Alignment.Top
  ) {
    if (!isUser) {
      // AI Avatar
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(ChronoPrimary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = "AI",
          tint = ChronoPrimary,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    Column(
      modifier = Modifier.weight(1f, fill = false),
      horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
      // Message Bubble
      Card(
        shape = RoundedCornerShape(
          topStart = 16.dp,
          topEnd = 16.dp,
          bottomStart = if (isUser) 16.dp else 4.dp,
          bottomEnd = if (isUser) 4.dp else 16.dp
        ),
        colors = CardDefaults.cardColors(
          containerColor = if (isUser) ChronoCardSurface else ChronoSurfaceDark
        ),
        border = BorderStroke(
          width = 1.dp,
          color = if (isUser) ChronoPrimary.copy(alpha = 0.4f) else ChronoCardBorder
        )
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          // AI Header with engine tag
          if (!isUser) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "CHRONO AI",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = ChronoPrimary
              )

              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (message.engineUsed.contains("Online")) ChronoAccent.copy(alpha = 0.15f)
                else Color(0xFFF59E0B).copy(alpha = 0.15f),
                border = BorderStroke(
                  0.5.dp,
                  if (message.engineUsed.contains("Online")) ChronoAccent else Color(0xFFF59E0B)
                )
              ) {
                Text(
                  text = message.engineUsed,
                  fontSize = 9.sp,
                  fontFamily = FontFamily.Monospace,
                  color = if (message.engineUsed.contains("Online")) ChronoAccent else Color(0xFFF59E0B),
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
          }

          // Message Body Text
          Text(
            text = message.text,
            fontSize = 14.sp,
            color = ChronoTextPrimary,
            lineHeight = 20.sp
          )

          // Attached Code Snippets
          if (message.codeBlocks.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            message.codeBlocks.forEach { snippet ->
              CodeBlockCard(snippet = snippet)
              Spacer(modifier = Modifier.height(8.dp))
            }
          }
        }
      }

      // Latency tag
      if (!isUser && message.latencyMs > 0) {
        Text(
          text = "${message.latencyMs}ms response time",
          fontSize = 10.sp,
          color = ChronoTextMuted,
          modifier = Modifier.padding(top = 2.dp, start = 4.dp)
        )
      }
    }

    if (isUser) {
      Spacer(modifier = Modifier.width(8.dp))
      // User Avatar
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(Color(0xFF1E293B)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = "User",
          tint = ChronoTextSecondary,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
