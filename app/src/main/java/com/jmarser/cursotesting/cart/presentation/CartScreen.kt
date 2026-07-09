package com.jmarser.cursotesting.cart.presentation


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.cart.domain.model.CartItem
import com.jmarser.cursotesting.cart.domain.model.CartSummary
import com.jmarser.cursotesting.cart.presentation.model.CartItemWithPromotion
import com.jmarser.cursotesting.core.presentation.components.MarketTopAppBar
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_ERROR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_ERROR_RETRY_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_LOADING
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_SUCESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_SUCESS_EMPTY
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.ui.theme.MyAppTheme
import java.text.NumberFormat
import java.util.Currency

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navigateToCheckout: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CartEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }
    CartContentScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onRetry = {

        },
        onIncreaseQuantity = { productId, Int ->
            viewModel.increaseQuantity(productId, Int)
        },
        onDecreaseQuantity = { productId, Int ->
            viewModel.decreaseQuantity(productId, Int)
        },
        onRemoveItem = { productId ->
            viewModel.removeFromCart(productId)
        },
        navigateToCheckout = navigateToCheckout
    )
}

@Composable
fun CartContentScreen(
    uiState: CartUiState,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onIncreaseQuantity: (String, Int) -> Unit,
    onDecreaseQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    navigateToCheckout: () -> Unit
) {
    Scaffold(
        snackbarHost = {
            snackbarHostState
        },
        topBar = {
            MarketTopAppBar(
                title = stringResource(R.string.cart_title),
                onBackSelected = {
                    onBack()
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            CartUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(CART_STATE_LOADING)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CartUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(CART_STATE_ERROR)
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${uiState.message}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.testTag(CART_STATE_ERROR_RETRY_BUTTON),
                        onClick = onRetry
                    ) {
                        Text(stringResource(R.string.cart_retry))
                    }
                }
            }

            is CartUiState.Success -> {

                val currencyFormatter = remember {
                    NumberFormat.getCurrencyInstance().apply {
                        currency = Currency.getInstance("USD")
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    AnimatedContent(
                        targetState = uiState.cartItems.isEmpty()
                    ) { isEmpty ->
                        if (isEmpty) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag(CART_STATE_SUCESS_EMPTY),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.cart_empty_message),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(R.string.cart_empty_sub_message),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .testTag(CART_STATE_SUCESS)
                                    .weight(1f),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.cartItems, key = { it.cartItem.productId }) { item ->
                                    CartItemCard(
                                        modifier = Modifier.animateItem(),
                                        itemWithProduct = item,
                                        currencyFormatter = currencyFormatter,
                                        onIncreaseQuantity = onIncreaseQuantity,
                                        onDecreaseQuantity = onDecreaseQuantity,
                                        onRemove = onRemoveItem
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.cartItems.isNotEmpty() && uiState.summary != null) {
                        CartSummaryCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            summary = uiState.summary,
                            currencyFormatter = currencyFormatter
                        )


                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = navigateToCheckout
                        ) {
                            Text("Finalizar compra")
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla Loading"
)
@Composable
private fun CartScreenContentPreview1(){
    MyAppTheme {
        CartContentScreen(
            uiState = CartUiState.Loading,
            onBack = {},
            onRetry = {},
            onIncreaseQuantity = {_, _ -> },
            onDecreaseQuantity = {_, _ -> },
            onRemoveItem = {_ ->},
            navigateToCheckout = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla Error"
)
@Composable
private fun CartScreenContentPreview2(){
    MyAppTheme {
        CartContentScreen(
            uiState = CartUiState.Error("Error"),
            onBack = {},
            onRetry = {},
            onIncreaseQuantity = {_, _ -> },
            onDecreaseQuantity = {_, _ -> },
            onRemoveItem = {_ ->},
            navigateToCheckout = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla Success Empty"
)
@Composable
private fun CartScreenContentPreview3(){
    MyAppTheme {
        CartContentScreen(
            uiState = CartUiState.Success(
                summary = null,
                cartItems = emptyList(),
                isLoading = false
            ),
            onBack = {},
            onRetry = {},
            onIncreaseQuantity = {_, _ -> },
            onDecreaseQuantity = {_, _ -> },
            onRemoveItem = {_ ->},
            navigateToCheckout = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla Success"
)
@Composable
private fun CartScreenContentPreview4(){
    val product1 = Product(
        id = "1",
        name = "Teclado Mecánico RGB",
        description = "Teclado con switches red y retroiluminación personalizable.",
        price = 89.99,
        category = "Electrónica",
        stock = 15,
        imageUrl = null
    )

    val product2 = Product(
        id = "2",
        name = "Ratón Inalámbrico Ergonómico",
        description = "Ratón óptico de alta precisión con batería recargable.",
        price = 45.00,
        category = "Electrónica",
        stock = 42,
        imageUrl = null
    )

    val product3 = Product(
        id = "3",
        name = "Cafetera Express Pro",
        description = "Bomba de 20 bares para un espresso perfecto en casa.",
        price = 150.00,
        category = "Hogar",
        stock = 5,
        imageUrl = null
    )

    val productWithPromo1 = ProductWithPromotion(
        product = product1,
        promotion = ProductPromotion.Percent(percent = 10.0, discountedPrice = 80.99)
    )
    val productWithPromo2 = ProductWithPromotion(
        product = product2,
        promotion = null
    )

    // 3. Creamos la relación de agregación con el carrito (CartItemWithPromotion)
    val mockCartItems = listOf(
        CartItemWithPromotion(
            cartItem = CartItem(productId = "1", quantity = 1),
            item = productWithPromo1
        ),
        CartItemWithPromotion(
            cartItem = CartItem(productId = "2", quantity = 2),
            item = productWithPromo2
        )
    )

    val summary: CartSummary = CartSummary(
        subTotal = 179.99,
        discountTotal = 9.00,
        finalTotal = 170.99
    )

    MyAppTheme {
        CartContentScreen(
            uiState = CartUiState.Success(
                cartItems = mockCartItems,
                summary = summary,
                isLoading = false
            ),
            onBack = {},
            onRetry = {},
            onIncreaseQuantity = {_, _ -> },
            onDecreaseQuantity = {_, _ -> },
            onRemoveItem = {_ ->},
            navigateToCheckout = {}
        )
    }
}
