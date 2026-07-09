package com.jmarser.cursotesting.cart.presentation


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_SUCESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.CART_STATE_SUCESS_EMPTY
import java.text.NumberFormat
import java.util.Currency

@Composable
fun CartSuccessScreen(
    modifier: Modifier = Modifier,
    state: CartUiState.Success,
    onIncreaseQuantity: (String, Int) -> Unit,
    onDecreaseQuantity: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
    navigateToCheckout: () -> Unit
) {

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance("USD")
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        AnimatedContent(
            targetState = state.cartItems.isEmpty()
        ) {isEmpty ->
            if (isEmpty){
                Column(
                    modifier = Modifier.testTag(CART_STATE_SUCESS_EMPTY),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
            }else{
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
                    items(state.cartItems, key = {it.cartItem.productId}){item ->
                        CartItemCard(
                            modifier = Modifier.
                            animateItem(),
                            itemWithProduct = item,
                            currencyFormatter = currencyFormatter,
                            onIncreaseQuantity = {productId, quantity ->
                                onIncreaseQuantity(productId, quantity)
                            },
                            onDecreaseQuantity = {productId, quantity ->
                                onDecreaseQuantity(productId, quantity)
                            },
                            onRemove = {productId ->
                                onRemove(productId)
                            }
                        )
                    }
                }
            }
        }

        if (state.cartItems.isNotEmpty() && state.summary != null){
            CartSummaryCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                summary = state.summary,
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

