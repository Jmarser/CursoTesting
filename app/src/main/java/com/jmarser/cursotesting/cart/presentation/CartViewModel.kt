package com.jmarser.cursotesting.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.cart.domain.usecase.GetCartItemsWithPromotionsUseCase
import com.jmarser.cursotesting.cart.domain.usecase.GetCartSummaryUseCase
import com.jmarser.cursotesting.cart.domain.usecase.UpdateCartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: CartViewModel.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 20/03/2026
 */

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    getCartSummaryUseCase: GetCartSummaryUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    getCartItemsWithPromotionsUseCase: GetCartItemsWithPromotionsUseCase
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = combine(
        getCartItemsWithPromotionsUseCase(), getCartSummaryUseCase()
    ) { cartItemWithPromotion, summary ->
        CartUiState.Success(
            summary = summary,
            cartItems = cartItemWithPromotion,
            isLoading = false
        ) as CartUiState
    }.catch { e ->
        _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
        emit(CartUiState.Error(e.message.orEmpty()))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartUiState.Loading
    )

    private val _events = MutableSharedFlow<CartEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CartEvent> = _events.asSharedFlow()


    fun updateCartItem(productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                updateCartItemUseCase(productId = productId, quantity)
            } catch (e: Exception) {
                _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
            }
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            try {
                cartRepository.deleteCartItem(productId)
            } catch (e: Exception) {
                _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
            }
        }
    }

    fun increaseQuantity(productId: String, currentQuantity: Int) {
        updateCartItem(productId = productId, quantity = currentQuantity + 1)
    }

    fun decreaseQuantity(productId: String, currentQuantity: Int) {
        if (currentQuantity > 1) {
            updateCartItem(productId = productId, quantity = currentQuantity - 1)
        } else {
            removeFromCart(productId)
        }
    }

}