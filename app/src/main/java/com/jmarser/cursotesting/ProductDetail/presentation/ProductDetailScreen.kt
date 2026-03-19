package com.jmarser.cursotesting.ProductDetail.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jmarser.cursotesting.ProductDetail.presentation.components.AddToCardButton
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.core.presentation.components.MarketTopAppBar
import com.jmarser.cursotesting.core.utils.toPriceAmount
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: String,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    Scaffold(
        topBar = {
            MarketTopAppBar(
                title = "Detalles del producto",
                onBackSelected = {
                    onBack()
                }
            )
        },
        bottomBar = {
            AddToCardButton(
                product = uiState.item?.product,
                isLoading = uiState.isLoading,
                addToCard = {
                    viewModel.addToCart()
                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (uiState.isLoading){
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }else{
                uiState.item?.let {
                    val product = it.product
                    val promotion = it.promotion
                    val discountedPrice = when(promotion){
                        is ProductPromotion.BuyXPayY -> null
                        is ProductPromotion.Percent -> promotion.discountedPrice
                        null -> null
                    }

                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                                    error = painterResource(R.drawable.ic_launcher_foreground)
                                )
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = product.category,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                if (product.description.isNotBlank()) {
                                    Text(
                                        text = product.description
                                    )
                                }

                                HorizontalDivider()

                                if (discountedPrice != null){
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = product.price.toPriceAmount(),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                        Text(
                                            text = discountedPrice.toPriceAmount(),
                                            style = MaterialTheme.typography.displaySmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.errorContainer
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                ),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            text = "${(promotion as ProductPromotion.Percent).percent}% OFF"
                                        )
                                    }
                                }else{
                                    Text(
                                        text = product.price.toPriceAmount(),
                                        style = MaterialTheme.typography.displaySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (promotion is ProductPromotion.BuyXPayY){
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.errorContainer
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                ),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            text = "PROMO: ${promotion.label}"
                                        )
                                    }
                                }

                                HorizontalDivider()

                                val hasStock = product.stock > 0
                                val colorStock = if (hasStock){
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                }else{
                                    MaterialTheme.colorScheme.onErrorContainer
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Stock disponible",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = colorStock
                                    ) {
                                        Text(
                                            text = if (hasStock) "${product.stock} unidades" else "Sin stock",
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                ),
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


