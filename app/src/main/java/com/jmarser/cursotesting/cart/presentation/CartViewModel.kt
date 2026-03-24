package com.jmarser.cursotesting.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.cart.domain.usecase.GetCartItemsWithPromotionsUseCase
import com.jmarser.cursotesting.cart.domain.usecase.GetCartSummaryUseCase
import com.jmarser.cursotesting.cart.domain.usecase.UpdateCartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val getCartItemsWithPromotionsUseCase: GetCartItemsWithPromotionsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CartEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CartEvent> = _events.asSharedFlow()

    private var cartJob: Job? = null

    init {
        loadCart()
    }

    fun loadCart(){
        _uiState.value = CartUiState.Loading
        cartJob?.cancel()

        cartJob = combine(
            getCartItemsWithPromotionsUseCase(), getCartSummaryUseCase()
        ){cartItemWithPromotion, summary ->
            CartUiState.Success(
                summary = summary,
                cartItems = cartItemWithPromotion,
                isLoading = false
            )

        }.onEach {
            _uiState.value = it
        }.catch {e ->
            _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
        }.launchIn(viewModelScope)
    }

    fun updateCartItem(productId: String, quantity: Int){
        viewModelScope.launch {
            try{
                updateCartItemUseCase(productId = productId, quantity)
            }catch(e: Exception){
                _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
            }
        }
    }

    fun removeFromCart(productId: String){
        viewModelScope.launch {
            try{
                cartRepository.deleteCartItem(productId)
            }catch(e: Exception){
                _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
            }
        }
    }

    fun increaseQuantity(productId: String, currentQuantity: Int){
        updateCartItem(productId = productId, quantity = currentQuantity + 1)
    }

    fun decreaseQuantity(productId: String, currentQuantity: Int){
        if (currentQuantity > 1) {
            updateCartItem(productId = productId, quantity = currentQuantity - 1)
        }else{
            removeFromCart(productId)
        }
    }

}