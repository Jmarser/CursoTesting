package com.jmarser.cursotesting.checkout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmarser.cursotesting.cart.domain.usecase.GetCartSummaryUseCase
import com.jmarser.cursotesting.checkout.domain.useCase.PlaceOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Project: CursoTesting
 * File: CheckoutViewModel.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 07/07/2026
 */

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val placeOrderUseCase: PlaceOrderUseCase,
    getCartSummaryUseCase: GetCartSummaryUseCase
): ViewModel(){

    private val formState = MutableStateFlow(CheckoutForm())
    private val submission = MutableStateFlow<Submission>(Submission.Idle)

    private val _event = MutableSharedFlow<CheckoutEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CheckoutEvent> = _event.asSharedFlow()

    val uiState: StateFlow<CheckoutUiState> = combine(
        getCartSummaryUseCase(),
        formState,
        submission
    ){summary, form, sub ->
        when(sub){
            is Submission.Success -> CheckoutUiState.Success(sub.confirmation)
            is Submission.Failure -> CheckoutUiState.Failed(sub.message)
            Submission.Idle, Submission.submitting -> {
                val errors = form.validate()
                val isCartEmpty = summary.subTotal <= 0.0
                val isSubmitting = sub == Submission.submitting
                CheckoutUiState.Idle(
                    summary = summary,
                    form = form,
                    errors = errors,
                    isCartEmpty = isCartEmpty,
                    isSubmitting = isSubmitting,
                    canSubmit = !isCartEmpty && !isSubmitting  && errors.isValid
                )
            }
        }
    }.catch { e ->
        _event.emit(CheckoutEvent.ShowMessage(e.message.orEmpty()))
        emit(CheckoutUiState.Failed(e.message.orEmpty()))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CheckoutUiState.Loading
    )

    fun onRetry() {
        submission.value = Submission.Idle
    }

    fun onNameChange(name: String) {
        formState.update { it.copy(name = name) }
    }
    fun onEmailChange(email: String) {
        formState.update { it.copy(email = email) }
    }
    fun onAddressChange(address: String) {
        formState.update { it.copy(address = address) }
    }
    fun onConfirm() {
        if (!formState.value.validate().isValid) return

        viewModelScope.launch {
            submission.value = Submission.submitting
            placeOrderUseCase()
                .onSuccess {
                    submission.value = Submission.Success(it)
                }
                .onFailure { e ->
                    submission.value = Submission.Failure(e.message.orEmpty())
                }
        }
    }
}