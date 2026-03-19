package com.jmarser.cursotesting.settings.presentation

import com.jmarser.cursotesting.core.domain.model.ThemeMode

/**
 * Project: CursoTesting
 * File: SettingsUiState.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 18/03/2026
 */

data class SettingsUiState(
    val inStockOnly: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)