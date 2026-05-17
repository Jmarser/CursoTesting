package com.jmarser.cursotesting.ProductDetail.presentation

import app.cash.turbine.test
import com.jmarser.cursotesting.ProductDetail.domain.usecase.GetProductDetailWithPromotionUseCase
import com.jmarser.cursotesting.cart.data.repository.FakeCartRepositoryImpl
import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.cart.domain.usecase.AddToCartUseCase
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ProductDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeProduct = FakeProductRepositoryImpl()
    private val fakeCart = FakeCartRepositoryImpl()
    private val fakePromotion = FakePromotionRepository()
    private val fakeClock = FakeClock()

    private fun createViewModel() = ProductDetailViewModel(
        getProductDetailWithPromotionUseCase = GetProductDetailWithPromotionUseCase(
            fakeProduct,
            fakePromotion,
            GetPromotionForProduct(),
            fakeClock
        ),
        addToCartUseCase = AddToCartUseCase(
            fakeCart,
            fakeProduct
        )
    )

    @Test
    fun `given valid product id when load product then emits item`() = runTest(mainDispatcherRule.scheduler) {
        val productId = "p1"
        val product = product {
            withId(productId)
        }

        fakeProduct.setProducts(listOf(product))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.loadProduct(productId)

            val finalState = awaitItem()

            assertEquals(productId, finalState.item?.product?.id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given missing product id when load product then end with item null`() = runTest(mainDispatcherRule.scheduler) {
        fakeProduct.setProducts(emptyList())
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.loadProduct("producto1")

            val state = awaitItem()

            assertNull(state.item)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given loaded product when add ti cart success then emits succes event`() = runTest(mainDispatcherRule.scheduler) {
        val produtId = "p1"
        val product = product {
            withId(produtId)
            withStock(10)
        }

        fakeProduct.setProducts(listOf(product))
        val viewModel = createViewModel()

        viewModel.loadProduct(produtId)

        viewModel.events.test {
            viewModel.addToCart()

            val result = awaitItem()
            assertEquals(ProductDetailEvent.SUCCESS_ADD_TO_CART, result)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given loaded product without stock when add to card then emits insufficient stock error`() = runTest(mainDispatcherRule.scheduler) {
        val produtId = "p1"
        val product = product {
            withId(produtId)
            withStock(0)
        }

        fakeProduct.setProducts(listOf(product))
        val viewModel = createViewModel()

        viewModel.loadProduct(produtId)

        viewModel.events.test {
            viewModel.addToCart()

            val result = awaitItem()
            assertEquals(ProductDetailEvent.INSUFICIENT_STOCK_ERROR, result)

            cancelAndIgnoreRemainingEvents()
        }
    }
}