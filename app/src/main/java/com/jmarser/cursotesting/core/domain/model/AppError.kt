package com.jmarser.cursotesting.core.domain.model

/**
 * Project: CursoTesting
 * File: AppError.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 13/03/2026
 */

sealed class AppError: Exception() {
    data object NetworkError: AppError() {
        private fun readResolve(): Any = NetworkError
    }

    data object NotFoundError: AppError() {
        private fun readResolve(): Any = NotFoundError
    }

    data object DataBaseError: AppError() {
        private fun readResolve(): Any = DataBaseError
    }

    //data class ValidationError(override val message: String): AppError()

    sealed class Validation: AppError(){
        data object QuantityMustPositive: Validation(){
            private fun readResolve(): Any = QuantityMustPositive
        }

        data class InsufficientStock(val available: Int): Validation()
    }

    data class UnknowError(override val message: String?): AppError()

}