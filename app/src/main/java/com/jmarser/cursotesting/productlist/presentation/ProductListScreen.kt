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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmarser.cursotesting.productlist.presentation.components.FiltersMenu
import com.jmarser.cursotesting.productlist.presentation.components.HomeTopAppBar
import com.jmarser.cursotesting.productlist.presentation.components.ProductItem
import com.jmarser.cursotesting.ui.theme.MyAppTheme
import kotlinx.coroutines.flow.collect

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            HomeTopAppBar(
                filtersVisible = filterVisible,
                onFiltersSelected = {showFilters ->
                    viewModel.setFilterVisible(showFilters)
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is ProductListUiState.Loading -> {
                Box(
                    modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductListUiState.Error -> {
                Box(
                    modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ERROR",
                        fontSize = 30.sp,
                        color = Color.Red
                    )
                }
            }

            is ProductListUiState.Success -> {
                Column(
                    modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AnimatedVisibility(
                        visible = filterVisible,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()

                    ) {
                        FiltersMenu(
                            state = state,
                            onCategorySelected = {
                                viewModel.setCategory(it)
                            },
                            onOrderSelected = {
                                viewModel.setSortOptions(it)
                            }
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            ),
                        text = "${state.productList.size} productos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    if (state.productList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "No se encontrarón productos",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    } else {

                        LazyColumn() {
                            items(state.productList) { product ->
                                ProductItem(
                                    productWithPromotion = product,
                                    onProductClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ProductListScreenPreview() {
    MyAppTheme() {
        ProductListScreen(modifier = Modifier)
    }
}
