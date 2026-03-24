package com.jmarser.cursotesting.cart.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmarser.cursotesting.core.presentation.components.MarketTopAppBar

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel(),
    onBack:() -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is CartEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            snackbarHostState
        },
        topBar = {
            MarketTopAppBar(
                title = "Carrito",
                onBackSelected = {
                    onBack()
                }
            )
        }
    ) {paddingValues ->
        when(val state = uiState){
            CartUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            is CartUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${state.message}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {

                        }
                    ) {
                        Text("Reintentar")
                    }
                }
            }
            is CartUiState.Success -> {
                CartSuccessScreen(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = state,
                    onIncreaseQuantity = {productId, quantity ->
                        viewModel.increaseQuantity( productId, quantity)
                    },
                    onDecreaseQuantity = {productId, quantity ->
                        viewModel.decreaseQuantity(productId, quantity)
                    },
                    onRemove = {productId ->
                        viewModel.removeFromCart(productId)
                    }
                )
            }
        }
    }
}


