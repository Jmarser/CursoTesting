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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jmarser.cursotesting.ProductDetail.presentation.components.AddToCardButton
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.core.presentation.components.MarketTopAppBar
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_CARD_PRODUCT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_IMAGE_PRODUCT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_CATEGORY
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DESCRIPTION
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_BUY_PAY
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_PERCENT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_NAME
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_PRICE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_PRICE_WITH_DESCOUNT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PRODUCT_STOCK_TITLE
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_PROGRESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_DETAILS_STATE_LOADING
import com.jmarser.cursotesting.core.utils.toPriceAmount
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.ui.theme.MyAppTheme

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: String,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember{
        SnackbarHostState()
    }

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event){
                ProductDetailEvent.INSUFICIENT_STOCK_ERROR -> snackbarHostState.showSnackbar("No hay suficiente stock")
                ProductDetailEvent.NETWORK_ERROR -> snackbarHostState.showSnackbar("No hay internet, compruebe su conexión")
                ProductDetailEvent.UNKNOW_ERROR -> snackbarHostState.showSnackbar("Error inesperado, vuelva a intentarlo")
                ProductDetailEvent.SUCCESS_ADD_TO_CART -> snackbarHostState.showSnackbar("Producto añadido")
            }
        }
    }

    ProductDetailContentScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onAddToCart = {viewModel.addToCart()}
    )

}

@Composable
fun ProductDetailContentScreen(
    uiState: ProductDetailUiState,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onBack: () -> Unit,
    onAddToCart: () -> Unit
) {
    Scaffold(
        topBar = {
            MarketTopAppBar(
                title = stringResource(R.string.product_details_title),
                onBackSelected = {
                    onBack()
                }
            )
        },
        bottomBar = {
            AddToCardButton(
                product = uiState.item?.product,
                isLoading = uiState.isLoading,
                addToCard = onAddToCart
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
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
                    Modifier
                        .fillMaxSize()
                        .testTag(PRODUCT_DETAILS_STATE_LOADING),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(PRODUCT_DETAILS_PROGRESS)
                    )
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
                                .fillMaxWidth()
                                .testTag(PRODUCT_DETAILS_CARD_PRODUCT),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AsyncImage(
                                    modifier = Modifier.testTag(PRODUCT_DETAILS_IMAGE_PRODUCT),
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                                    error = painterResource(R.drawable.ic_launcher_foreground)
                                )
                                Text(
                                    modifier = Modifier.testTag(PRODUCT_DETAILS_PRODUCT_NAME),
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
                                        modifier = Modifier
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                            .testTag(PRODUCT_DETAILS_PRODUCT_CATEGORY),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                if (product.description.isNotBlank()) {
                                    Text(
                                        modifier = Modifier.testTag(PRODUCT_DETAILS_PRODUCT_DESCRIPTION),
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
                                            modifier = Modifier.testTag(PRODUCT_DETAILS_PRODUCT_PRICE),
                                            text = product.price.toPriceAmount(),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                        Text(
                                            modifier = Modifier.testTag(PRODUCT_DETAILS_PRODUCT_PRICE_WITH_DESCOUNT),
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
                                                )
                                                .testTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_PERCENT),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            text = stringResource(R.string.product_details_promotion_percent_name, (promotion as ProductPromotion.Percent).percent)
                                        )
                                    }
                                }else{
                                    Text(
                                        modifier = Modifier.testTag(PRODUCT_DETAILS_PRODUCT_PRICE),
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
                                                )
                                                .testTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_PROMOTION_BUY_PAY),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            text = stringResource(R.string.product_details_promotion_buy_pay_name, promotion.label)
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
                                        modifier = Modifier.testTag(PRODUCT_DETAILS_PRODUCT_STOCK_TITLE),
                                        text = stringResource(R.string.product_details_stock_available),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = colorStock
                                    ) {
                                        Text(
                                            text = if (hasStock) stringResource(R.string.product_details_quantity_stock_available, product.stock) else stringResource(R.string.product_details_no_stock),
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                )
                                                .testTag(PRODUCT_DETAILS_PRODUCT_DISPLAY_UNITS_STOCK),
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

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla loading"
)
@Composable
private fun ProductDetailContentScreenPreview1(){
    MyAppTheme {
        ProductDetailContentScreen(
            uiState = ProductDetailUiState(
                item = null,
                isLoading = true
            ),
            onBack = {},
            onAddToCart = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla success"
)
@Composable
private fun ProductDetailContentScreenPreview2(){
    val product1 = Product(
        id = "1",
        name = "Teclado Mecánico RGB",
        description = "Teclado con switches red y retroiluminación personalizable.",
        price = 89.99,
        category = "Electrónica",
        stock = 15,
        imageUrl = null
    )
    val productWithPromo1 = ProductWithPromotion(
        product = product1,
        promotion = ProductPromotion.Percent(percent = 10.0, discountedPrice = 80.99)
    )
    MyAppTheme {
        ProductDetailContentScreen(
            uiState = ProductDetailUiState(
                item = productWithPromo1,
                isLoading = false
            ),
            onBack = {},
            onAddToCart = {}
        )
    }
}
