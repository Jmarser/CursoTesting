package com.jmarser.cursotesting.core.domain.model

/**
 * Project: CursoTesting
 * File: ThemeMode.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 18/03/2026
 */

sealed class ThemeMode(val id: Int) {
    data object SYSTEM: ThemeMode(0)
    data object LIGHT: ThemeMode(1)
    data object DARK: ThemeMode(2)
}