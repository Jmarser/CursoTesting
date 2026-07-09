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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmarser.cursotesting.core.presentation.components.MarketTopAppBar
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
        onNameChange = {viewModel.onNameChange(it)},
        onEmailChange = {viewModel.onEmailChange(it)},
        onAddressChange = {viewModel.onAddressChange(it)},
        onConfirm = {viewModel.onConfirm()},
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MarketTopAppBar(
                title = "Checkout",
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
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CheckoutUiState.Failed -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(uiState.message)
                    Button(
                        onClick = { onRetry() }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            is CheckoutUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Pedido confirmado: ${uiState.confirmation.orderId}")
                    Text("Tiempo estimado: ${uiState.confirmation.etaMinutes}")
                    Text("Precio: ${uiState.confirmation.total}")

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
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Total: ${uiState.summary.finalTotal}",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = uiState.form.name,
            onValueChange = onNameChange,
            label = {
                Text("Nombre")
            },
            isError = uiState.errors.nameError != null
        )

        OutlinedTextField(
            value = uiState.form.address,
            onValueChange = onAddressChange,
            label = {
                Text("Dirección")
            },
            isError = uiState.errors.addressError != null
        )
        OutlinedTextField(
            value = uiState.form.email,
            onValueChange = onEmailChange,
            label = {
                Text("Email")
            },
            isError = uiState.errors.emailError != null
        )

        if (uiState.isCartEmpty){
            Text("Tu carrito está vacío")
        }else{
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onConfirm,
                enabled = uiState.canSubmit
            ) {
                Text(if(uiState.isSubmitting) "Procesando el pago..." else "Confirmar pedido")
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
    MyAppTheme() {
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
