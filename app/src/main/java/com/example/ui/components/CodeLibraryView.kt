package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.IntegrationInstructions
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CodeLibraryItem
import com.example.model.CodeSnippet
import com.example.model.OfflineCodeLibrary
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
fun CodeLibraryView(
  onSendToChat: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedLanguage by remember { mutableStateOf("All") }

  val languages = listOf("All", "Python", "Kotlin", "JavaScript", "C++", "SQL")

  val filteredItems = remember(searchQuery, selectedLanguage) {
    OfflineCodeLibrary.snippets.filter { item ->
      (selectedLanguage == "All" || item.language.equals(selectedLanguage, ignoreCase = true)) &&
        (searchQuery.isBlank() ||
          item.title.contains(searchQuery, ignoreCase = true) ||
          item.description.contains(searchQuery, ignoreCase = true) ||
          item.tags.any { it.contains(searchQuery, ignoreCase = true) })
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ChronoBackgroundDark)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "OFFLINE CODE VAULT",
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          color = ChronoPrimary
        )
        Text(
          text = "Curated Code & Algorithms",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = ChronoTextPrimary
        )
      }

      Surface(
        shape = RoundedCornerShape(4.dp),
        color = ChronoAccent.copy(alpha = 0.15f),
        border = BorderStroke(0.5.dp, ChronoAccent)
      ) {
        Text(
          text = "100% OFFLINE",
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          color = ChronoAccent,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Search Box
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Search code, algorithms, frameworks...", fontSize = 13.sp, color = ChronoTextMuted) },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ChronoTextSecondary) },
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = ChronoSurfaceDark,
        unfocusedContainerColor = ChronoSurfaceDark,
        focusedBorderColor = ChronoPrimary,
        unfocusedBorderColor = ChronoCardBorder,
        focusedTextColor = ChronoTextPrimary,
        unfocusedTextColor = ChronoTextPrimary
      ),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Language Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      languages.forEach { lang ->
        val isSelected = selectedLanguage == lang
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (isSelected) ChronoPrimary else ChronoSurfaceDark,
          border = BorderStroke(1.dp, if (isSelected) ChronoPrimary else ChronoCardBorder),
          modifier = Modifier.clickable { selectedLanguage = lang }
        ) {
          Text(
            text = lang,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF003544) else ChronoTextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Code Items List
    LazyColumn(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(filteredItems, key = { it.id }) { item ->
        CodeVaultCard(item = item, onAskAi = { onSendToChat("Explain this code in detail and optimize it:\n${item.code}") })
      }
    }
  }
}

@Composable
private fun CodeVaultCard(
  item: CodeLibraryItem,
  onAskAi: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = ChronoSurfaceDark),
    border = BorderStroke(1.dp, ChronoCardBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(ChronoPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = null,
              tint = ChronoPrimary,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ChronoTextPrimary
          )
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = ChronoCardSurface,
          border = BorderStroke(0.5.dp, ChronoCardBorder)
        ) {
          Text(
            text = item.complexity,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            color = ChronoAccent,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = item.description,
        fontSize = 12.sp,
        color = ChronoTextSecondary,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Embedded Code Block
      CodeBlockCard(
        snippet = CodeSnippet(
          language = item.language.lowercase(),
          code = item.code,
          explanation = "Complexity: ${item.complexity}"
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          item.tags.take(3).forEach { tag ->
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color(0xFF0F172A)
            ) {
              Text(
                text = "#$tag",
                fontSize = 10.sp,
                color = ChronoTextMuted,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = ChronoPrimary.copy(alpha = 0.15f),
          border = BorderStroke(0.5.dp, ChronoPrimary),
          modifier = Modifier.clickable(onClick = onAskAi)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = ChronoPrimary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Ask AI to Explain",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = ChronoPrimary
            )
          }
        }
      }
    }
  }
}
