package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = ChronoPrimary,
  onPrimary = Color(0xFF003544),
  primaryContainer = Color(0xFF004D63),
  onPrimaryContainer = Color(0xFFBBE9FF),
  secondary = ChronoSecondary,
  onSecondary = Color(0xFF1E1B4B),
  secondaryContainer = Color(0xFF312E81),
  onSecondaryContainer = Color(0xFFE0E7FF),
  tertiary = ChronoTertiary,
  onTertiary = Color(0xFF2E1065),
  background = ChronoBackgroundDark,
  onBackground = ChronoTextPrimary,
  surface = ChronoSurfaceDark,
  onSurface = ChronoTextPrimary,
  surfaceVariant = ChronoCardSurface,
  onSurfaceVariant = ChronoTextSecondary,
  outline = ChronoCardBorder,
  outlineVariant = Color(0xFF1F2937)
)

private val LightColorScheme = darkColorScheme(
  primary = ChronoPrimary,
  onPrimary = Color(0xFF003544),
  primaryContainer = Color(0xFF004D63),
  onPrimaryContainer = Color(0xFFBBE9FF),
  secondary = ChronoSecondary,
  onSecondary = Color(0xFF1E1B4B),
  secondaryContainer = Color(0xFF312E81),
  onSecondaryContainer = Color(0xFFE0E7FF),
  tertiary = ChronoTertiary,
  onTertiary = Color(0xFF2E1065),
  background = ChronoBackgroundDark,
  onBackground = ChronoTextPrimary,
  surface = ChronoSurfaceDark,
  onSurface = ChronoTextPrimary,
  surfaceVariant = ChronoCardSurface,
  onSurfaceVariant = ChronoTextSecondary,
  outline = ChronoCardBorder,
  outlineVariant = Color(0xFF1F2937)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our customized high-fidelity dark studio theme
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
