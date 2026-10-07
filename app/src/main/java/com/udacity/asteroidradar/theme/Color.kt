package com.udacity.asteroidradar.theme

import androidx.compose.ui.graphics.Color

// =============================================================================
// LIGHT MODE — NASA Palette Aligned (Standard Contrast)
// =============================================================================

// NOTE: Using onBackground for text on dark surfaces (NASA space theme in light mode)

// Primary: NASA Blue
val primaryLight = Color(0xFF105BD8)                // NASA $color-primary
val onPrimaryLight = Color(0xFFFFFFFF)              // NASA $color-white
val primaryContainerLight = Color(0xFFDCE4EF)       // NASA $color-gray-cool-light
val onPrimaryContainerLight = Color(0xFF0B3D91)     // NASA $color-primary-darker

// Secondary: NASA Cyan (for safe/info states)
val secondaryLight = Color(0xFF02BFE7)              // NASA $color-primary-alt
val onSecondaryLight = Color(0xFFFFFFFF)            // NASA $color-white
val secondaryContainerLight = Color(0xFFE1F3F8)     // NASA $color-primary-alt-lightest
val onSecondaryContainerLight = Color(0xFF046B99)   // NASA $color-primary-alt-darkest

// Tertiary: NASA Gold (for highlights/accents)
val tertiaryLight = Color(0xFFFF9D1E)               // NASA $color-gold
val onTertiaryLight = Color(0xFFFFFFFF)             // NASA $color-white
val tertiaryContainerLight = Color(0xFFFFEBD1)      // NASA $color-gold-lightest
val onTertiaryContainerLight = Color(0xFF5C4A00)    // Derived dark gold

// Error: NASA Red (for danger/hazardous states)
val errorLight = Color(0xFFDD361C)                  // NASA $color-secondary
val onErrorLight = Color(0xFFFFFFFF)                // NASA $color-white
val errorContainerLight = Color(0xFFF9E0DE)         // NASA $color-secondary-lightest
val onErrorContainerLight = Color(0xFF99231B)       // NASA $color-secondary-darkest

// Background & Surface
val backgroundLight = Color(0xFF000000)             // Black space theme
val onBackgroundLight = Color(0xFFFFFFFF)           // NASA $color-white
val surfaceLight = Color(0xFF212121)                // NASA $color-gray-lightest
val onSurfaceLight = Color(0xFFFFFFFF)              // NASA $color-base
val surfaceVariantLight = Color(0xFF323A45)         // NASA $color-gray-dark
val onSurfaceVariantLight = Color(0xFFAEB0B5)       // NASA $color-gray-light

// Outline & Utility
val outlineLight = Color(0xFF5B616B)                // NASA $color-gray
val outlineVariantLight = Color(0xFFD6D7D9)         // NASA $color-gray-lighter
val scrimLight = Color(0x55010613)                  // Semi-transparent dark
val inverseSurfaceLight = Color(0xFFE4E2E0)         // NASA $color-gray-warm-light
val inverseOnSurfaceLight = Color(0xFF061F4A)       // NASA $color-primary-darkest
val inversePrimaryLight = Color(0xFF9BDAF1)         // NASA $color-primary-alt-light
val surfaceTintLight = Color(0xFF105BD8)            // NASA $color-primary

// Surface Containers: cool charcoal grays derived from NASA $color-gray-dark (#323A45).
// The "light" scheme sits on a black space background with white onSurface text,
// so containers stay dark-cool to keep onSurface legible (>= 10:1 on every tier).
val surfaceDimLight = Color(0xFF121418)
val surfaceBrightLight = Color(0xFF3A414C)
val surfaceContainerLowestLight = Color(0xFF000000)
val surfaceContainerLowLight = Color(0xFF15181D)
val surfaceContainerLight = Color(0xFF1C2027)
val surfaceContainerHighLight = Color(0xFF262B33)
val surfaceContainerHighestLight = Color(0xFF30363F)

// =============================================================================
// LIGHT MODE — Medium Contrast
// (Containers deepen / accents darken so every on-color pair reaches >= 7:1)
// =============================================================================

val primaryLightMediumContrast = Color(0xFF0C49B0)
val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
val primaryContainerLightMediumContrast = Color(0xFFC9D6EA)
val onPrimaryContainerLightMediumContrast = Color(0xFF082F73)

val secondaryLightMediumContrast = Color(0xFF04597F)
val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
val secondaryContainerLightMediumContrast = Color(0xFFC5E6F0)
val onSecondaryContainerLightMediumContrast = Color(0xFF03435F)

val tertiaryLightMediumContrast = Color(0xFF7A4800)
val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightMediumContrast = Color(0xFFFFDDB0)
val onTertiaryContainerLightMediumContrast = Color(0xFF4A3800)

val errorLightMediumContrast = Color(0xFFA81D0D)
val onErrorLightMediumContrast = Color(0xFFFFFFFF)
val errorContainerLightMediumContrast = Color(0xFFF5C9C5)
val onErrorContainerLightMediumContrast = Color(0xFF6E0F07)

val backgroundLightMediumContrast = Color(0xFF000000)
val onBackgroundLightMediumContrast = Color(0xFFFFFFFF)
val surfaceLightMediumContrast = Color(0xFF1A1A1A)
val onSurfaceLightMediumContrast = Color(0xFFFFFFFF)
val surfaceVariantLightMediumContrast = Color(0xFF2A313B)
val onSurfaceVariantLightMediumContrast = Color(0xFFC9CBCF)

val outlineLightMediumContrast = Color(0xFF7C828D)
val outlineVariantLightMediumContrast = Color(0xFFE0E1E3)
val scrimLightMediumContrast = Color(0x66010613)
val inverseSurfaceLightMediumContrast = Color(0xFFEAE8E6)
val inverseOnSurfaceLightMediumContrast = Color(0xFF04163A)
val inversePrimaryLightMediumContrast = Color(0xFF48B1DC)
val surfaceTintLightMediumContrast = Color(0xFF0C49B0)
val surfaceDimLightMediumContrast = Color(0xFF0D0F12)
val surfaceBrightLightMediumContrast = Color(0xFF333A44)
val surfaceContainerLowestLightMediumContrast = Color(0xFF000000)
val surfaceContainerLowLightMediumContrast = Color(0xFF101318)
val surfaceContainerLightMediumContrast = Color(0xFF171B21)
val surfaceContainerHighLightMediumContrast = Color(0xFF20252C)
val surfaceContainerHighestLightMediumContrast = Color(0xFF2A3038)

// =============================================================================
// LIGHT MODE — High Contrast
// (Every on-color pair targets >= 11:1, approaching AAA for all text)
// =============================================================================

val primaryLightHighContrast = Color(0xFF082F73)
val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
val primaryContainerLightHighContrast = Color(0xFFC9D6EA)
val onPrimaryContainerLightHighContrast = Color(0xFF03153A)

val secondaryLightHighContrast = Color(0xFF033A52)
val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
val secondaryContainerLightHighContrast = Color(0xFFC5E6F0)
val onSecondaryContainerLightHighContrast = Color(0xFF021F2D)

val tertiaryLightHighContrast = Color(0xFF4F2D00)
val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightHighContrast = Color(0xFFFFDDB0)
val onTertiaryContainerLightHighContrast = Color(0xFF2E2200)

val errorLightHighContrast = Color(0xFF6E0F07)
val onErrorLightHighContrast = Color(0xFFFFFFFF)
val errorContainerLightHighContrast = Color(0xFFF5C9C5)
val onErrorContainerLightHighContrast = Color(0xFF3B0703)

val backgroundLightHighContrast = Color(0xFF000000)
val onBackgroundLightHighContrast = Color(0xFFFFFFFF)
val surfaceLightHighContrast = Color(0xFF0F0F0F)
val onSurfaceLightHighContrast = Color(0xFFFFFFFF)
val surfaceVariantLightHighContrast = Color(0xFF222830)
val onSurfaceVariantLightHighContrast = Color(0xFFE4E5E8)

val outlineLightHighContrast = Color(0xFFAEB4BE)
val outlineVariantLightHighContrast = Color(0xFFEDEEEF)
val scrimLightHighContrast = Color(0x80010613)
val inverseSurfaceLightHighContrast = Color(0xFFF5F3F1)
val inverseOnSurfaceLightHighContrast = Color(0xFF020C24)
val inversePrimaryLightHighContrast = Color(0xFF0E86B4)
val surfaceTintLightHighContrast = Color(0xFF082F73)
val surfaceDimLightHighContrast = Color(0xFF08090B)
val surfaceBrightLightHighContrast = Color(0xFF2B313A)
val surfaceContainerLowestLightHighContrast = Color(0xFF000000)
val surfaceContainerLowLightHighContrast = Color(0xFF0B0D10)
val surfaceContainerLightHighContrast = Color(0xFF121519)
val surfaceContainerHighLightHighContrast = Color(0xFF191D22)
val surfaceContainerHighestLightHighContrast = Color(0xFF22272D)

// =============================================================================
// DARK MODE — NASA Palette Aligned (Standard Contrast)
// M3: lighter tones on dark backgrounds
// =============================================================================

// Primary: Lighter Blue for dark mode
val primaryDark = Color(0xFF9BDAF1)                 // NASA $color-primary-alt-light
val onPrimaryDark = Color(0xFF061F4A)               // NASA $color-primary-darkest
val primaryContainerDark = Color(0xFF0B3D91)        // NASA $color-primary-darker
val onPrimaryContainerDark = Color(0xFFDCE4EF)      // NASA $color-gray-cool-light

// Secondary: Light Cyan for dark mode
val secondaryDark = Color(0xFF9BDAF1)               // NASA $color-primary-alt-light
val onSecondaryDark = Color(0xFF046B99)             // NASA $color-primary-alt-darkest
val secondaryContainerDark = Color(0xFF0A4D5C)      // Derived dark cyan
val onSecondaryContainerDark = Color(0xFF9BDAF1)    // NASA $color-primary-alt-light

// Tertiary: Light Gold for dark mode
val tertiaryDark = Color(0xFFFFC375)                // NASA $color-gold-lighter
val onTertiaryDark = Color(0xFF5C4A00)              // Derived dark gold
val tertiaryContainerDark = Color(0xFF7A5C00)       // Derived dark gold container
val onTertiaryContainerDark = Color(0xFFFFEBD1)     // NASA $color-gold-lightest

// Error: Light Red for dark mode
val errorDark = Color(0xFFE59892)                   // NASA $color-secondary-light
val onErrorDark = Color(0xFF99231B)                 // NASA $color-secondary-darkest
val errorContainerDark = Color(0xFF99231B)          // NASA $color-secondary-darkest
val onErrorContainerDark = Color(0xFFF9E0DE)        // NASA $color-secondary-lightest

// Background & Surface: Deep Navy Space Theme
val backgroundDark = Color(0xFF061F4A)              // NASA $color-primary-darkest
val onBackgroundDark = Color(0xFFF1F1F1)            // NASA $color-gray-lightest
val surfaceDark = Color(0xFF212121)                 // NASA $color-base
val onSurfaceDark = Color(0xFFF1F1F1)               // NASA $color-gray-lightest
val surfaceVariantDark = Color(0xFF323A45)          // NASA $color-gray-dark
val onSurfaceVariantDark = Color(0xFFAEB0B5)        // NASA $color-gray-light

// Outline & Utility
val outlineDark = Color(0xFFAEB0B5)                 // NASA $color-gray-light
val outlineVariantDark = Color(0xFF494440)          // NASA $color-gray-warm-dark
val scrimDark = Color(0x55010613)                   // Semi-transparent dark
val inverseSurfaceDark = Color(0xFFF1F1F1)          // NASA $color-gray-lightest
val inverseOnSurfaceDark = Color(0xFF212121)        // NASA $color-base
val inversePrimaryDark = Color(0xFF105BD8)          // NASA $color-primary
val surfaceTintDark = Color(0xFF9BDAF1)             // NASA $color-primary-alt-light

// Surface Containers: elevated tonal steps of the Deep Navy background
// (#061F4A, NASA $color-primary-darkest). Higher tiers move toward
// NASA $color-primary-darker (#0B3D91) — the "closer to the viewer" feel.
val surfaceDimDark = Color(0xFF041838)
val surfaceBrightDark = Color(0xFF1F3E70)
val surfaceContainerLowestDark = Color(0xFF03132E)
val surfaceContainerLowDark = Color(0xFF0A2550)
val surfaceContainerDark = Color(0xFF0E2C5C)
val surfaceContainerHighDark = Color(0xFF133468)
val surfaceContainerHighestDark = Color(0xFF193D75)

// =============================================================================
// DARK MODE — Medium Contrast
// (Accents lighten, containers deepen so every on-color pair reaches >= 7:1)
// =============================================================================

val primaryDarkMediumContrast = Color(0xFFB3E4F6)
val onPrimaryDarkMediumContrast = Color(0xFF04163A)
val primaryContainerDarkMediumContrast = Color(0xFF0A3079)
val onPrimaryContainerDarkMediumContrast = Color(0xFFE9EEF5)

val secondaryDarkMediumContrast = Color(0xFFB3E4F6)
val onSecondaryDarkMediumContrast = Color(0xFF03435F)
val secondaryContainerDarkMediumContrast = Color(0xFF083F4C)
val onSecondaryContainerDarkMediumContrast = Color(0xFFB3E4F6)

val tertiaryDarkMediumContrast = Color(0xFFFFD39A)
val onTertiaryDarkMediumContrast = Color(0xFF3D2F00)
val tertiaryContainerDarkMediumContrast = Color(0xFF5E4600)
val onTertiaryContainerDarkMediumContrast = Color(0xFFFFF1DE)

val errorDarkMediumContrast = Color(0xFFF0B1AC)
val onErrorDarkMediumContrast = Color(0xFF5A0C05)
val errorContainerDarkMediumContrast = Color(0xFF7C1B14)
val onErrorContainerDarkMediumContrast = Color(0xFFFBEAE8)

val backgroundDarkMediumContrast = Color(0xFF051B40)
val onBackgroundDarkMediumContrast = Color(0xFFF7F7F7)
val surfaceDarkMediumContrast = Color(0xFF1C1C1C)
val onSurfaceDarkMediumContrast = Color(0xFFF8F8F8)
val surfaceVariantDarkMediumContrast = Color(0xFF2B323C)
val onSurfaceVariantDarkMediumContrast = Color(0xFFC9CBCF)

val outlineDarkMediumContrast = Color(0xFFBFC1C6)
val outlineVariantDarkMediumContrast = Color(0xFF6B6560)
val scrimDarkMediumContrast = Color(0x66010613)
val inverseSurfaceDarkMediumContrast = Color(0xFFF5F5F5)
val inverseOnSurfaceDarkMediumContrast = Color(0xFF1A1A1A)
val inversePrimaryDarkMediumContrast = Color(0xFF0C49B0)
val surfaceTintDarkMediumContrast = Color(0xFFB3E4F6)
val surfaceDimDarkMediumContrast = Color(0xFF03142F)
val surfaceBrightDarkMediumContrast = Color(0xFF1A3765)
val surfaceContainerLowestDarkMediumContrast = Color(0xFF020E24)
val surfaceContainerLowDarkMediumContrast = Color(0xFF08214A)
val surfaceContainerDarkMediumContrast = Color(0xFF0B2752)
val surfaceContainerHighDarkMediumContrast = Color(0xFF102E5D)
val surfaceContainerHighestDarkMediumContrast = Color(0xFF153668)

// =============================================================================
// DARK MODE — High Contrast
// (Every on-color pair targets >= 11:1, approaching AAA for all text)
// =============================================================================

val primaryDarkHighContrast = Color(0xFFD8F2FC)
val onPrimaryDarkHighContrast = Color(0xFF010A1F)
val primaryContainerDarkHighContrast = Color(0xFF0A2860)
val onPrimaryContainerDarkHighContrast = Color(0xFFF7F9FC)

val secondaryDarkHighContrast = Color(0xFFCDEFFB)
val onSecondaryDarkHighContrast = Color(0xFF022B3E)
val secondaryContainerDarkHighContrast = Color(0xFF052A34)
val onSecondaryContainerDarkHighContrast = Color(0xFFCDEFFB)

val tertiaryDarkHighContrast = Color(0xFFFFE2BD)
val onTertiaryDarkHighContrast = Color(0xFF2B2100)
val tertiaryContainerDarkHighContrast = Color(0xFF3B2B00)
val onTertiaryContainerDarkHighContrast = Color(0xFFFFF8EE)

val errorDarkHighContrast = Color(0xFFF9CFCB)
val onErrorDarkHighContrast = Color(0xFF3B0703)
val errorContainerDarkHighContrast = Color(0xFF66130D)
val onErrorContainerDarkHighContrast = Color(0xFFFDF3F2)

val backgroundDarkHighContrast = Color(0xFF04132F)
val onBackgroundDarkHighContrast = Color(0xFFFFFFFF)
val surfaceDarkHighContrast = Color(0xFF141414)
val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkHighContrast = Color(0xFF232931)
val onSurfaceVariantDarkHighContrast = Color(0xFFE0E1E4)

val outlineDarkHighContrast = Color(0xFFD6D8DC)
val outlineVariantDarkHighContrast = Color(0xFF8A847F)
val scrimDarkHighContrast = Color(0x80010613)
val inverseSurfaceDarkHighContrast = Color(0xFFFFFFFF)
val inverseOnSurfaceDarkHighContrast = Color(0xFF121212)
val inversePrimaryDarkHighContrast = Color(0xFF082F73)
val surfaceTintDarkHighContrast = Color(0xFFD8F2FC)
val surfaceDimDarkHighContrast = Color(0xFF020D22)
val surfaceBrightDarkHighContrast = Color(0xFF142D57)
val surfaceContainerLowestDarkHighContrast = Color(0xFF010818)
val surfaceContainerLowDarkHighContrast = Color(0xFF061A3D)
val surfaceContainerDarkHighContrast = Color(0xFF082045)
val surfaceContainerHighDarkHighContrast = Color(0xFF0B2650)
val surfaceContainerHighestDarkHighContrast = Color(0xFF0F2D5B)
