package com.jmarser.cursotesting.core.domain.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Project: CursoTesting
 * File: DispatchersProvider.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 16/03/2026
 */

interface DispatchersProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}