package com.udacity.asteroidradar.theme

import androidx.compose.ui.graphics.Color

// =============================================================================
// LIGHT MODE — NASA Palette Aligned
// =============================================================================

// TODO Comment: Using onBackground for text on dark surfaces (NASA space theme in light mode)

// Primary: NASA Blue
val md_theme_light_primary = Color(0xFF105BD8)           // NASA $color-primary
val md_theme_light_onPrimary = Color(0xFFFFFFFF)         // NASA $color-white
val md_theme_light_primaryContainer = Color(0xFFDCE4EF)  // NASA $color-gray-cool-light
val md_theme_light_onPrimaryContainer = Color(0xFF0B3D91) // NASA $color-primary-darker

// Secondary: NASA Cyan (for safe/info states)
val md_theme_light_secondary = Color(0xFF02BFE7)         // NASA $color-primary-alt
val md_theme_light_onSecondary = Color(0xFFFFFFFF)       // NASA $color-white
val md_theme_light_secondaryContainer = Color(0xFFE1F3F8) // NASA $color-primary-alt-lightest
val md_theme_light_onSecondaryContainer = Color(0xFF046B99) // NASA $color-primary-alt-darkest

// Tertiary: NASA Gold (for highlights/accents)
val md_theme_light_tertiary = Color(0xFFFF9D1E)          // NASA $color-gold
val md_theme_light_onTertiary = Color(0xFFFFFFFF)        // NASA $color-white
val md_theme_light_tertiaryContainer = Color(0xFFFFEBD1) // NASA $color-gold-lightest
val md_theme_light_onTertiaryContainer = Color(0xFF5C4A00) // Derived dark gold

// Error: NASA Red (for danger/hazardous states)
val md_theme_light_error = Color(0xFFDD361C)             // NASA $color-secondary
val md_theme_light_errorContainer = Color(0xFFF9E0DE)    // NASA $color-secondary-lightest
val md_theme_light_onError = Color(0xFFFFFFFF)           // NASA $color-white
val md_theme_light_onErrorContainer = Color(0xFF99231B)  // NASA $color-secondary-darkest

// Background & Surface
val md_theme_light_background = Color(0xFF000000)        // Black space theme
val md_theme_light_onBackground = Color(0xFFFFFFFF)      // NASA $color-white
val md_theme_light_surface = Color(0xFF212121)           // NASA $color-gray-lightest
val md_theme_light_onSurface = Color(0xFFFFFFFF)         // NASA $color-base
val md_theme_light_surfaceVariant = Color(0xFF323A45)    // NASA $color-gray-dark
val md_theme_light_onSurfaceVariant = Color(0xFFAEB0B5)  // NASA $color-gray-light

// Outline & Utility
val md_theme_light_outline = Color(0xFF5B616B)           // NASA $color-gray
val md_theme_light_inverseOnSurface = Color(0xFF061F4A)  // NASA $color-primary-darkest
val md_theme_light_inverseSurface = Color(0xFFE4E2E0)    // NASA $color-gray-warm-light
val md_theme_light_inversePrimary = Color(0xFF9BDAF1)    // NASA $color-primary-alt-light
val md_theme_light_surfaceTint = Color(0xFF105BD8)       // NASA $color-primary
val md_theme_light_outlineVariant = Color(0xFFD6D7D9)    // NASA $color-gray-lighter
val md_theme_light_scrim = Color(0x55010613)             // Semi-transparent dark

// =============================================================================
// DARK MODE — NASA Palette Aligned (M3: lighter tones on dark backgrounds)
// =============================================================================

// Primary: Lighter Blue for dark mode
val md_theme_dark_primary = Color(0xFF9BDAF1)            // NASA $color-primary-alt-light
val md_theme_dark_onPrimary = Color(0xFF061F4A)          // NASA $color-primary-darkest
val md_theme_dark_primaryContainer = Color(0xFF0B3D91)   // NASA $color-primary-darker
val md_theme_dark_onPrimaryContainer = Color(0xFFDCE4EF) // NASA $color-gray-cool-light

// Secondary: Light Cyan for dark mode
val md_theme_dark_secondary = Color(0xFF9BDAF1)          // NASA $color-primary-alt-light
val md_theme_dark_onSecondary = Color(0xFF046B99)        // NASA $color-primary-alt-darkest
val md_theme_dark_secondaryContainer = Color(0xFF0A4D5C) // Derived dark cyan
val md_theme_dark_onSecondaryContainer = Color(0xFF9BDAF1) // NASA $color-primary-alt-light

// Tertiary: Light Gold for dark mode
val md_theme_dark_tertiary = Color(0xFFFFC375)           // NASA $color-gold-lighter
val md_theme_dark_onTertiary = Color(0xFF5C4A00)         // Derived dark gold
val md_theme_dark_tertiaryContainer = Color(0xFF7A5C00)  // Derived dark gold container
val md_theme_dark_onTertiaryContainer = Color(0xFFFFEBD1) // NASA $color-gold-lightest

// Error: Light Red for dark mode
val md_theme_dark_error = Color(0xFFE59892)              // NASA $color-secondary-light
val md_theme_dark_errorContainer = Color(0xFF99231B)     // NASA $color-secondary-darkest
val md_theme_dark_onError = Color(0xFF99231B)            // NASA $color-secondary-darkest
val md_theme_dark_onErrorContainer = Color(0xFFF9E0DE)   // NASA $color-secondary-lightest

// Background & Surface: Deep Navy Space Theme
val md_theme_dark_background = Color(0xFF061F4A)         // NASA $color-primary-darkest
val md_theme_dark_onBackground = Color(0xFFF1F1F1)       // NASA $color-gray-lightest
val md_theme_dark_surface = Color(0xFF212121)            // NASA $color-base
val md_theme_dark_onSurface = Color(0xFFF1F1F1)          // NASA $color-gray-lightest
val md_theme_dark_surfaceVariant = Color(0xFF323A45)     // NASA $color-gray-dark
val md_theme_dark_onSurfaceVariant = Color(0xFFAEB0B5)   // NASA $color-gray-light

// Outline & Utility
val md_theme_dark_outline = Color(0xFFAEB0B5)            // NASA $color-gray-light
val md_theme_dark_inverseOnSurface = Color(0xFF212121)   // NASA $color-base
val md_theme_dark_inverseSurface = Color(0xFFF1F1F1)     // NASA $color-gray-lightest
val md_theme_dark_inversePrimary = Color(0xFF105BD8)     // NASA $color-primary
val md_theme_dark_surfaceTint = Color(0xFF9BDAF1)        // NASA $color-primary-alt-light
val md_theme_dark_outlineVariant = Color(0xFF494440)     // NASA $color-gray-warm-dark
val md_theme_dark_scrim = Color(0x55010613)              // Semi-transparent dark
