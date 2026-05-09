package com.jmarser.cursotesting.core.data.util

import com.jmarser.cursotesting.core.domain.util.Clock
import jakarta.inject.Inject
import java.time.Instant

/**
 * Project: CursoTesting
 * File: ClockImpl.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 03/05/2026
 */

class ClockImpl @Inject constructor(): Clock {
    override fun now(): Instant {
        return Instant.now()
    }
}