package com.jmarser.cursotesting.productlist.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.jmarser.cursotesting.core.mockwebserver.MarketApiDispatcher
import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import com.jmarser.cursotesting.core.mockwebserver.rules.MockWebServerRule
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.core.utils.asAsset
import com.jmarser.cursotesting.productlist.data.repository.SettingsRepositoryImpl
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.PromotionRepository
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetProductsUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProductListViewModelIntegrationTest {

    private companion object{
        const val EXPECTED_PRODUCT_SIZE = 3
        const val DAIRY_CATEGORY = "Lácteos"
    }

    @get:Rule(order = 0)
    val mockWebServer = MockWebServerRule()

    @get:Rule(order = 1)
    val hilt = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val mainDispacherRule = MainDispatcherRule()

    @Inject
    lateinit var getProductsUseCase: GetProductsUseCase

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var promotionRepository: PromotionRepository

    @Inject
    lateinit var productRepository: ProductRepository

    @Before
    fun setUp() = runTest{
        mockWebServer.server.dispatcher = MarketApiDispatcher(productJson = "product_list_default.json".asAsset())
        hilt.inject()
        (settingsRepository as? SettingsRepositoryImpl)?.clear()

        productRepository.refreshProduct()
        promotionRepository.refreshPromotions()
    }

    @After
    fun tearDown(){
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    @Test
    fun given_success_fullApi_when_viewModel_loads_then_show_products() = runTest {
        val viewModel = ProductListViewModel(getProductsUseCase, settingsRepository)
        viewModel.uiState.test {
            val result = awaitSuccessMatching { it.productList.size == EXPECTED_PRODUCT_SIZE }

            assertTrue(result.productList.isNotEmpty())
            assertTrue(result.productList.size == EXPECTED_PRODUCT_SIZE)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun given_dairy_category_selected_when_filtering_then_only_dairy_product_are_show() = runTest {
        val viewModel = ProductListViewModel(getProductsUseCase, settingsRepository)
        viewModel.uiState.test {
            awaitSuccessMatching { it.productList.size == EXPECTED_PRODUCT_SIZE }
            viewModel.setCategory(DAIRY_CATEGORY)

            val result = awaitSuccessMatching { state ->
                state.selectedCategory == DAIRY_CATEGORY && state.productList.isNotEmpty() && state.productList.all {
                    it.product.category == DAIRY_CATEGORY
                }
            }

            assertTrue(result.productList.size == 2)
            assertTrue(result.productList.all { it.product.category == DAIRY_CATEGORY })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun given_products_loaded_when_sortingByPriceAsc_then_list_is_correctly_ordered() = runTest {
        val viewModel = ProductListViewModel(getProductsUseCase, settingsRepository)
        viewModel.uiState.test {
            awaitSuccessMatching { it.productList.size == EXPECTED_PRODUCT_SIZE }
            viewModel.setSortOptions(SortOption.PRICE_ASC)

            val result = awaitSuccessMatching { state ->
                state.sortOption == SortOption.PRICE_ASC && state.productList.map { it.product.price } == state.productList.map { it.product.price }.sorted()
            }

            assertEquals(10.0, result.productList.first().product.price, 0.0)
            assertEquals(listOf(10.0, 20.0, 30.0), result.productList.map { it.product.price })
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun ReceiveTurbine<ProductListUiState>.awaitSuccessMatching(
        predicate: (ProductListUiState.Success) -> Boolean
    ): ProductListUiState.Success{
        while(true){
            when(val item = awaitItem()){
                is ProductListUiState.Success -> if(predicate(item)) return item
                is ProductListUiState.Error -> error("Unexpected error: ${item.message}")
                is ProductListUiState.Loading -> Unit
            }
        }
    }
}