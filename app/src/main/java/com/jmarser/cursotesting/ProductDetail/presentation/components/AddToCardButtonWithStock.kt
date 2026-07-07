package com.jmarser.cursotesting.ProductDetail.presentation.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.ADD_TO_CART_BUTTON_WITH_STOCK
import com.jmarser.cursotesting.productlist.domain.model.Product

@Composable
fun AddToCardButtonWithStock(
    modifier: Modifier = Modifier,
    product: Product,
    isLoading: Boolean,
    addToCard: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag(ADD_TO_CART_BUTTON_WITH_STOCK),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ){
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = !isLoading,
                onClick = {
                    addToCard()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Agregar producto",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.add_to_cart_button_add_to_cart),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


