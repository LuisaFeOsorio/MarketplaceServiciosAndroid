package com.tuaplicacion.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primary.copy(alpha = 0.1f), // Puedes definir un contenedor si lo deseas
    onPrimaryContainer = md_theme_light_primary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondary.copy(alpha = 0.1f),
    onSecondaryContainer = md_theme_light_secondary,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiary.copy(alpha = 0.1f),
    onTertiaryContainer = md_theme_light_tertiary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_error.copy(alpha = 0.1f),
    onErrorContainer = md_theme_light_error,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseSurface = md_theme_dark_surface,
    inverseOnSurface = md_theme_dark_onSurface,
    inversePrimary = md_theme_dark_primary,
    surfaceTint = md_theme_light_primary,
    outlineVariant = md_theme_light_outline.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.6f)
)

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primary.copy(alpha = 0.2f),
    onPrimaryContainer = md_theme_dark_primary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondary.copy(alpha = 0.2f),
    onSecondaryContainer = md_theme_dark_secondary,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiary.copy(alpha = 0.2f),
    onTertiaryContainer = md_theme_dark_tertiary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_error.copy(alpha = 0.2f),
    onErrorContainer = md_theme_dark_error,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseSurface = md_theme_light_surface,
    inverseOnSurface = md_theme_light_onSurface,
    inversePrimary = md_theme_light_primary,
    surfaceTint = md_theme_dark_primary,
    outlineVariant = md_theme_dark_outline.copy(alpha = 0.5f),
    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun MarketplaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Si quieres soporte para Android 12+ dynamic colors
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}