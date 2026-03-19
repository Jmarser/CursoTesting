package com.jmarser.cursotesting.ProductDetail.presentation.components


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jmarser.cursotesting.productlist.domain.model.Product

@Composable
fun AddToCardButton(
    modifier: Modifier = Modifier,
    product: Product?,
    isLoading: Boolean,
    addToCard: () -> Unit
) {

    product?.let {
        if (it.stock > 0) {
            AddToCardButtonWithStock(
                modifier = modifier,
                product = it,
                isLoading = isLoading,
                addToCard = addToCard
            )
        } else {
            AddToCardButtonNoStock()
        }
    }
}


