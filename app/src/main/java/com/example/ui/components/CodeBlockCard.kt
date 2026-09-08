package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CodeSnippet
import com.example.ui.theme.ChronoAccent
import com.example.ui.theme.ChronoCardBorder
import com.example.ui.theme.ChronoPrimary

@Composable
fun CodeBlockCard(
  snippet: CodeSnippet,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var isCopied by remember { mutableStateOf(false) }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF070B14)),
    border = BorderStroke(1.dp, ChronoCardBorder),
    modifier = modifier.fillMaxWidth()
  ) {
    Column {
      // Header: Language pill & Copy button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFF0F172A))
          .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Code,
            contentDescription = null,
            tint = ChronoPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = ChronoPrimary.copy(alpha = 0.15f),
            border = BorderStroke(0.5.dp, ChronoPrimary)
          ) {
            Text(
              text = snippet.language.uppercase(),
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        IconButton(
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("CHRONO AI Code", snippet.code)
            clipboard.setPrimaryClip(clip)
            isCopied = true
            Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = if (isCopied) Icons.Default.Done else Icons.Default.ContentCopy,
            contentDescription = "Copy code",
            tint = if (isCopied) ChronoAccent else Color(0xFF94A3B8),
            modifier = Modifier.size(16.dp)
          )
        }
      }

      // Code text area with horizontal scrolling
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(14.dp)
      ) {
        Text(
          text = snippet.code,
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          color = Color(0xFFE2E8F0),
          lineHeight = 18.sp
        )
      }

      // Optional explanation text
      if (snippet.explanation.isNotBlank()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0C1322))
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Text(
            text = "💡 ${snippet.explanation}",
            fontSize = 11.sp,
            color = Color(0xFF94A3B8),
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}
