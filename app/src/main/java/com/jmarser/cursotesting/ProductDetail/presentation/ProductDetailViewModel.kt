package com.jmarser.cursotesting.ProductDetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmarser.cursotesting.ProductDetail.domain.usecase.GetProductDetailWithPromotionUseCase
import com.jmarser.cursotesting.cart.domain.usecase.AddToCartUseCase
import com.jmarser.cursotesting.core.domain.model.AppError
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
import kotlinx.coroutines.launch

/**
 * Project: CursoTesting
 * File: DetailViewModel.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailWithPromotionUseCase: GetProductDetailWithPromotionUseCase,
    private val addToCartUseCase: AddToCartUseCase
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
                if (e is AppError){
                    handleError(e)
                }else{
                    handleError(AppError.UnknowError(e.message))
                }
            }
            .launchIn(viewModelScope)
    }

    fun addToCart() {
        val product = _uiState.value.item?.product?.id ?: return
        viewModelScope.launch {
            try{
                addToCartUseCase(product)
                _events.emit(ProductDetailEvent.SUCCESS_ADD_TO_CART)
            }catch(e: AppError){
                handleError(e)
            }catch (e: Exception){
                handleError(AppError.UnknowError(e.message))
            }
        }
    }

    private suspend fun handleError(e: AppError) {
        val error = when(e){
            AppError.DataBaseError, is AppError.UnknowError, AppError.Validation.QuantityMustPositive, AppError.NotFoundError -> ProductDetailEvent.UNKNOW_ERROR
            is AppError.Validation.InsufficientStock -> ProductDetailEvent.INSUFICIENT_STOCK_ERROR
            AppError.NetworkError -> ProductDetailEvent.NETWORK_ERROR
        }
        _events.emit(error)
    }
}