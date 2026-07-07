package com.jmarser.cursotesting.productlist.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.cart.presentation.CartUiState
import com.jmarser.cursotesting.cart.presentation.CartViewModel
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_ERROR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_LOADING
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_SUCCESS
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.PRODUCT_LIST_STATE_SUCCESS_EMPTY
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.ProductWithPromotion
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import com.jmarser.cursotesting.productlist.presentation.components.FiltersMenu
import com.jmarser.cursotesting.productlist.presentation.components.HomeTopAppBar
import com.jmarser.cursotesting.productlist.presentation.components.ProductItem
import com.jmarser.cursotesting.ui.theme.MyAppTheme

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToProductDetail: (String) -> Unit,
    navigateToCart: () -> Unit
 ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartUiState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val filterVisible by viewModel.filterVisible.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProductListEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val cartItemCount = remember(cartUiState) {
        when(val state = cartUiState){
            is CartUiState.Success -> {
                state.cartItems.sumOf { it.cartItem.quantity }
            }
            else -> 0
        }
    }

    ProductListContent(
        uiState = uiState,
        cartItemCount = cartItemCount,
        filterVisible = filterVisible,
        snackbarHostState = snackbarHostState,
        onFilterSelected = { showFilters -> viewModel.setFilterVisible(showFilters)},
        onCategorySelected = {category -> viewModel.setCategory(category)},
        onSortOptionSelected = {sortOption -> viewModel.setSortOptions(sortOption)},
        navigateToSettings = navigateToSettings,
        navigateToProductDetail = {productWithPromotion -> navigateToProductDetail(productWithPromotion.product.id)},
        navigateToCart = navigateToCart
    )
}

@Composable
fun ProductListContent(
    uiState: ProductListUiState,
    cartItemCount: Int,
    filterVisible: Boolean,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onFilterSelected: (Boolean) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProductDetail: (ProductWithPromotion) -> Unit,
    navigateToCart: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            HomeTopAppBar(
                filtersVisible = filterVisible,
                cartItemCount = cartItemCount,
                onFiltersSelected = onFilterSelected,
                onSettingsSelected = navigateToSettings,
                onNavigateToCart = navigateToCart
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is ProductListUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        Modifier.testTag(PRODUCT_LIST_STATE_LOADING)
                    )
                }
            }

            is ProductListUiState.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.testTag(PRODUCT_LIST_STATE_ERROR),
                        text = stringResource(R.string.product_list_error),
                        fontSize = 30.sp,
                        color = Color.Red
                    )
                }
            }

            is ProductListUiState.Success -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AnimatedVisibility(
                        visible = filterVisible,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()

                    ) {
                        FiltersMenu(
                            state = uiState,
                            onCategorySelected = onCategorySelected,
                            onOrderSelected = onSortOptionSelected
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            ),
                        text = "${uiState.productList.size} productos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    if (uiState.productList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
                                .testTag(PRODUCT_LIST_STATE_SUCCESS_EMPTY),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.product_list_product_not_found),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    } else {

                        LazyColumn(
                            modifier = Modifier.testTag(PRODUCT_LIST_STATE_SUCCESS)
                        ) {
                            items(uiState.productList) { product ->
                                ProductItem(
                                    productWithPromotion = product,
                                    onProductClick = navigateToProductDetail
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Pantalla success")
@Composable
private fun ProductListContentPreview1(){

// 1. Creamos los productos base (Product)
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

    // 2. Construimos la lista de ProductWithPromotion combinando los productos con sus promociones
    val mockProducts = listOf(
        ProductWithPromotion(
            product = product1,
            promotion = ProductPromotion.Percent(
                percent = 15.0, // 15% de descuento
                discountedPrice = 76.49
            )
        ),
        ProductWithPromotion(
            product = product2,
            promotion = null // Sin promoción, precio regular
        ),
        ProductWithPromotion(
            product = product3,
            promotion = ProductPromotion.BuyXPayY(
                buy = 3,
                pay = 2,
                label = "¡Lleva 3 y paga 2!",
                unitPrice = 150.00
            )
        )
    )

    // 2. Construimos el estado simulando un caso de éxito (Success)
    // Nota: Adapta 'ProductListUiState.Success' según cómo esté declarada tu sealed interface
    val mockUiState = ProductListUiState.Success(
        productList = mockProducts,
        categories = listOf(),
        selectedCategory = null,
        sortOption = SortOption.PRICE_ASC
    )

    MyAppTheme {
        ProductListContent(
            uiState = mockUiState,
            cartItemCount = 3,
            filterVisible = false,
            snackbarHostState = remember { SnackbarHostState() },
            onFilterSelected = {},
            onCategorySelected = {},
            onSortOptionSelected = {},
            navigateToSettings = {},
            navigateToProductDetail = {},
            navigateToCart = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Pantalla success empty")
@Composable
private fun ProductListContentPreview2(){
    val mockUiState = ProductListUiState.Success(
        productList = emptyList(),
        categories = listOf(),
        selectedCategory = null,
        sortOption = SortOption.PRICE_ASC
    )

    MyAppTheme {
        ProductListContent(
            uiState = mockUiState,
            cartItemCount = 3,
            filterVisible = false,
            snackbarHostState = remember { SnackbarHostState() },
            onFilterSelected = {},
            onCategorySelected = {},
            onSortOptionSelected = {},
            navigateToSettings = {},
            navigateToProductDetail = {},
            navigateToCart = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Pantalla error")
@Composable
private fun ProductListContentPreview3(){
    MyAppTheme {
        ProductListContent(
            uiState = ProductListUiState.Error(message = "Error"),
            cartItemCount = 3,
            filterVisible = false,
            snackbarHostState = remember { SnackbarHostState() },
            onFilterSelected = {},
            onCategorySelected = {},
            onSortOptionSelected = {},
            navigateToSettings = {},
            navigateToProductDetail = {},
            navigateToCart = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Pantalla loading")
@Composable
private fun ProductListContentPreview4(){
    MyAppTheme {
        ProductListContent(
            uiState = ProductListUiState.Loading,
            cartItemCount = 3,
            filterVisible = false,
            snackbarHostState = remember { SnackbarHostState() },
            onFilterSelected = {},
            onCategorySelected = {},
            onSortOptionSelected = {},
            navigateToSettings = {},
            navigateToProductDetail = {},
            navigateToCart = {}
        )
    }
}