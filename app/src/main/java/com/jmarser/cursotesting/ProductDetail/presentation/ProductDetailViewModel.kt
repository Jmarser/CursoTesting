package com.jmarser.cursotesting.ProductDetail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jmarser.cursotesting.ProductDetail.domain.usecase.GetProductDetailWithPromotionUseCase
import com.jmarser.cursotesting.core.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Project: CursoTesting
 * File: DetailViewModel.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailWithPromotionUseCase: GetProductDetailWithPromotionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProductDetailEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<ProductDetailEvent> = _events.asSharedFlow()

    private var productJob: Job? = null


    fun loadProduct(productId: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        productJob?.cancel()

        productJob = getProductDetailWithPromotionUseCase(productId)
            .onEach { product ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    item = product
                )
            }
            .catch { e: Throwable ->
                _uiState.value = _uiState.value.copy(isLoading = false)
                _events.emit(ProductDetailEvent.ShowError(e.message.orEmpty()))
            }
            .launchIn(viewModelScope)
    }

    fun addToCart() {

    }
}