package com.jmarser.cursotesting.core.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Project: CursoTesting
 * File: Screen.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

@Serializable
sealed interface Screen: NavKey {

    @Serializable
    data object ProductList: Screen

    @Serializable
    data object Cart: Screen

    @Serializable
    data object Setting: Screen

    @Serializable
    data class ProductDetail(val productId: String): Screen

    @Serializable
    data object Checkout: Screen
}