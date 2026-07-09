package com.jmarser.cursotesting.checkout.presentation

import com.jmarser.cursotesting.checkout.domain.model.OrderConfirmation

/**
 * Project: CursoTesting
 * File: Submission.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

sealed interface Submission {
    data object Idle: Submission
    data object submitting: Submission
    data class Success(val confirmation: OrderConfirmation): Submission
    data class Failure(val message: String): Submission
}