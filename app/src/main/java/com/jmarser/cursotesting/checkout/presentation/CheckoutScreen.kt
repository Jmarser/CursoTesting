package com.jmarser.cursotesting.checkout.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.cart.domain.model.CartSummary
import com.jmarser.cursotesting.checkout.domain.model.OrderConfirmation
import com.jmarser.cursotesting.core.presentation.components.MarketTopAppBar
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_CART_NOT_EMPTY_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_FAILED_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_FAILED_MESSAGE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_IDLE_TEXT_TOTAL
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_OUTLINEDTEXTFIELD_ADDRESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_OUTLINEDTEXTFIELD_EMAIL
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_OUTLINEDTEXTFIELD_NAME
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_OUTLINEDTEXTFILED_TEXT_ADDRESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_OUTLINEDTEXTFILED_TEXT_EMAIL
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_OUTLINEDTEXTFILED_TEXT_NAME
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_PROGRESS_INDICATOR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_STATE_FAILED
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_STATE_IDLE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_STATE_LOADING
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_STATE_SUCCESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_SUCCESS_TEXT_CONFIRMED
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_SUCCESS_TEXT_ETA_MINUTES
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_SUCCESS_TEXT_PRICE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CHECKOUT_TEXT_CART_EMPTY
import com.jmarser.cursotesting.ui.theme.MyAppTheme

@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CheckoutEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    CheckoutContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onRetry = { viewModel.onRetry() },
        onNameChange = { viewModel.onNameChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onAddressChange = { viewModel.onAddressChange(it) },
        onConfirm = { viewModel.onConfirm() },
        onBack = onBack
    )
}

@Composable
fun CheckoutContent(
    modifier: Modifier = Modifier,
    uiState: CheckoutUiState,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onRetry: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MarketTopAppBar(
                title = stringResource(R.string.checkout_title),
                onBackSelected = onBack
            )
        }
    ) { paddingValues ->

        when (uiState) {
            is CheckoutUiState.Idle -> {
                CheckoutContentIdle(
                    uiState = uiState,
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onAddressChange = onAddressChange,
                    onConfirm = onConfirm
                )
            }

            CheckoutUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(CHECKOUT_STATE_LOADING)
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(CHECKOUT_PROGRESS_INDICATOR)
                    )
                }
            }

            is CheckoutUiState.Failed -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(CHECKOUT_STATE_FAILED)
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.testTag(CHECKOUT_FAILED_MESSAGE),
                        text = uiState.message
                    )
                    Button(
                        modifier = Modifier.testTag(CHECKOUT_FAILED_BUTTON),
                        onClick = { onRetry() }
                    ) {
                        Text(stringResource(R.string.checkout_retry))
                    }
                }
            }

            is CheckoutUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(CHECKOUT_STATE_SUCCESS)
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.testTag(CHECKOUT_SUCCESS_TEXT_CONFIRMED),
                        text = stringResource(R.string.checkout_order_confirmed,uiState.confirmation.orderId)
                    )
                    Text(
                        modifier = Modifier.testTag(CHECKOUT_SUCCESS_TEXT_ETA_MINUTES),
                        text = stringResource(R.string.checkout_order_eta_minutes, uiState.confirmation.etaMinutes)
                    )
                    Text(
                        modifier = Modifier.testTag(CHECKOUT_SUCCESS_TEXT_PRICE),
                        text =  stringResource(R.string.checkout_order_price, uiState.confirmation.total)
                    )

                }
            }
        }
    }
}

@Composable
fun CheckoutContentIdle(
    uiState: CheckoutUiState.Idle,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(CHECKOUT_STATE_IDLE)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.testTag(CHECKOUT_IDLE_TEXT_TOTAL),
            text = stringResource(
                R.string.checkout_total,
                uiState.summary.finalTotal
            ),//"Total: ${uiState.summary.finalTotal}",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            modifier = Modifier
                .testTag(CHECKOUT_OUTLINEDTEXTFIELD_NAME)
                .fillMaxWidth(),
            value = uiState.form.name,
            onValueChange = onNameChange,
            label = {
                Text(
                    modifier = Modifier.testTag(CHECKOUT_OUTLINEDTEXTFILED_TEXT_NAME),
                    text = stringResource(R.string.checkout_form_name))
            },
            isError = uiState.errors.nameError != null
        )

        OutlinedTextField(
            modifier = Modifier
                .testTag(CHECKOUT_OUTLINEDTEXTFIELD_ADDRESS)
                .fillMaxWidth(),
            value = uiState.form.address,
            onValueChange = onAddressChange,
            label = {
                Text(
                    modifier = Modifier.testTag(CHECKOUT_OUTLINEDTEXTFILED_TEXT_ADDRESS),
                    text = stringResource(R.string.checkout_form_address)
                )
            },
            isError = uiState.errors.addressError != null
        )
        OutlinedTextField(
            modifier = Modifier
                .testTag(CHECKOUT_OUTLINEDTEXTFIELD_EMAIL)
                .fillMaxWidth(),
            value = uiState.form.email,
            onValueChange = onEmailChange,
            label = {
                Text(
                    modifier = Modifier.testTag(CHECKOUT_OUTLINEDTEXTFILED_TEXT_EMAIL),
                    text = stringResource(R.string.checkout_form_email)
                )
            },
            isError = uiState.errors.emailError != null
        )

        if (uiState.isCartEmpty) {
            Text(
                modifier = Modifier.testTag(CHECKOUT_TEXT_CART_EMPTY),
                text = stringResource(R.string.checkout_your_cart_is_empty)
            )
        } else {
            Button(
                modifier = Modifier
                    .testTag(CHECKOUT_CART_NOT_EMPTY_BUTTON)
                    .fillMaxWidth(),
                onClick = onConfirm,
                enabled = uiState.canSubmit
            ) {
                Text(
                    modifier = Modifier.testTag(CHECKOUT_TEXT_CART_EMPTY),
                    text = if (uiState.isSubmitting) stringResource(R.string.checkout_processing_payment) else stringResource(
                        R.string.checkout_confirm_order
                    )
                )
            }
        }
    }
}

@Preview(
    name = "Pantalla Checkout Loading",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun CheckoutContentPreview() {
    MyAppTheme {
        CheckoutContent(
            uiState = CheckoutUiState.Loading,
            onRetry = {},
            onNameChange = {},
            onEmailChange = {},
            onAddressChange = {},
            onConfirm = {},
            onBack = {},
        )
    }
}

@Preview(
    name = "Pantalla Checkout Failure",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun CheckoutContentPreview2() {
    MyAppTheme {
        CheckoutContent(
            uiState = CheckoutUiState.Failed("Error de carga"),
            onRetry = {},
            onNameChange = {},
            onEmailChange = {},
            onAddressChange = {},
            onConfirm = {},
            onBack = {},
        )
    }
}

@Preview(
    name = "Pantalla Checkout Success",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun CheckoutContentPreview3() {
    MyAppTheme {
        CheckoutContent(
            uiState = CheckoutUiState.Success(OrderConfirmation("1", 120, 20.0)),
            onRetry = {},
            onNameChange = {},
            onEmailChange = {},
            onAddressChange = {},
            onConfirm = {},
            onBack = {},
        )
    }
}

@Preview(
    name = "Pantalla Checkout Idle",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun CheckoutContentPreview4() {
    MyAppTheme {
        CheckoutContent(
            uiState = CheckoutUiState.Idle(
                summary = CartSummary(
                    subTotal = 23.0,
                    discountTotal = 2.0,
                    finalTotal = 20.0
                ),
                form = CheckoutForm(
                    name = "Juan",
                    address = "Mi casa",
                    email = "user@user.com"
                ),
                errors = CheckoutFormErrors(
                    nameError = null,
                    emailError = null,
                    addressError = null
                ),
                isCartEmpty = false,
                isSubmitting = false,
                canSubmit = true
            ),
            onRetry = {},
            onNameChange = {},
            onEmailChange = {},
            onAddressChange = {},
            onConfirm = {},
            onBack = {},
        )
    }
}
