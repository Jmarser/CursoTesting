package com.jmarser.cursotesting.core.domain.util

import java.time.Instant

/**
 * Project: CursoTesting
 * File: Clock.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 03/05/2026
 */

interface Clock {

    fun now(): Instant
}