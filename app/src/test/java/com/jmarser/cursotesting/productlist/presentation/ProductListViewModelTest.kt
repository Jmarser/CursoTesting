package com.jmarser.cursotesting.productlist.presentation

import app.cash.turbine.test
import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.core.stubs.FailingProductRepositoryStub
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.data.repository.FakeSettingsRepository
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetProductsUseCase
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProductListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        fakeProduct: ProductRepository = FakeProductRepositoryImpl(),
        fakeSettings: FakeSettingsRepository = FakeSettingsRepository(),
        fakePromotion: FakePromotionRepository = FakePromotionRepository(),
        fakeClock: FakeClock = FakeClock()
    ): ProductListViewModel {
        val getProductUseCase = GetProductsUseCase(
            fakeProduct,
            fakePromotion,
            GetPromotionForProduct(),
            fakeSettings,
            fakeClock
        )
        return ProductListViewModel(
            getProductUseCase,
            fakeSettings
        )
    }

    @Test
    fun `given products when initialized then emits success state`() =
        runTest(mainDispatcherRule.scheduler) {
            val productId = "id1"
            val product = product {
                withId(productId)
            }
            val fakeProduct = FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }

            val viewModel = createViewModel(fakeProduct)

            viewModel.uiState.test {
                val state = awaitItem()

                assertTrue(state is ProductListUiState.Success)
                assertEquals(1, (state as ProductListUiState.Success).productList.size)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given selected category when set category then filters products`() =
        runTest(mainDispatcherRule.scheduler) {
            val product1 = product {
                withId("1")
                withCategory("carne")
            }
            val product2 = product {
                withId("2")
                withCategory("pasta")
            }

            val fakeProduct =
                FakeProductRepositoryImpl().apply { setProducts(listOf(product1, product2)) }

            val viewModel = createViewModel(fakeProduct = fakeProduct)

            viewModel.uiState.test {
                awaitItem()

                viewModel.setCategory("pasta")
                val state = awaitItem()

                assertTrue(state is ProductListUiState.Success)
                assertEquals(1, (state as ProductListUiState.Success).productList.size)
                assertEquals("pasta", (state).selectedCategory)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given price asc sort option when set sort option then sorts by effective price`() =
        runTest(mainDispatcherRule.scheduler) {
            val product1 = product {
                withId("1")
                withPrice(30.0)
            }
            val product2 = product {
                withId("2")
                withPrice(15.0)
            }

            val fakeProduct =
                FakeProductRepositoryImpl().apply { setProducts(listOf(product1, product2)) }

            val viewModel = createViewModel(fakeProduct = fakeProduct)

            viewModel.uiState.test {
                awaitItem()

                viewModel.setSortOptions(SortOption.PRICE_ASC)

                val state = awaitItem() as ProductListUiState.Success

                assertEquals(15.0, state.productList[0].product.price, 0.0)
                assertEquals(30.0, state.productList[1].product.price, 0.0)
                assertEquals(SortOption.PRICE_ASC, state.sortOption)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given repository error when loading products then emits error state`() = runTest(mainDispatcherRule.scheduler) {

        val failingRepository = FailingProductRepositoryStub(Exception("Prueba test"))

        val viewModel = createViewModel(fakeProduct = failingRepository)

        viewModel.uiState.test {
            val state = awaitItem()

            assertTrue(state is ProductListUiState.Error)
            assertEquals("Prueba test", (state as ProductListUiState.Error).message)

            cancelAndConsumeRemainingEvents()
        }
    }



}