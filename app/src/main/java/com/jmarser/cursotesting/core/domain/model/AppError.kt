package com.jmarser.cursotesting.core.domain.model

/**
 * Project: CursoTesting
 * File: AppError.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

sealed class AppError: Exception() {
    data object NetworkError: AppError()
    data object NotFoundError: AppError()
    data object DataBaseError: AppError()
    data class ValidationError(override val message: String): AppError()
    data class UnknowError(override val message: String?): AppError()

}