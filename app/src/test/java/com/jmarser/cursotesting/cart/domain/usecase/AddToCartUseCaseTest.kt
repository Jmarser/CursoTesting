package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.data.repository.FakeCartRepositoryImpl
import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.domain.model.AppError
import com.jmarser.cursotesting.productlist.domain.model.Product
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddToCartUseCaseTest {

    @Test
    fun zero_quantity_throws_quantityMustBePositive() = runTest{
        // Given
        val fakeCartRepositoryImpl = FakeCartRepositoryImpl()
        val fakeProductRepositoryImpl = FakeProductRepositoryImpl()

        val useCase = AddToCartUseCase(fakeCartRepositoryImpl, fakeProductRepositoryImpl)

        // When
        val exception: Throwable? = runCatching { useCase("id", 0) }.exceptionOrNull()

        // Then
        assertTrue(exception is AppError.Validation.QuantityMustPositive)
    }

    @Test
    fun negative_quantity_throws_quantityMustBePositive() = runTest{
        // Given
        val fakeCartRepositoryImpl = FakeCartRepositoryImpl()
        val fakeProductRepositoryImpl = FakeProductRepositoryImpl().apply {
            setProducts(emptyList())
        }

        val useCase = AddToCartUseCase(fakeCartRepositoryImpl, fakeProductRepositoryImpl)

        // When
        val exception: Throwable? = runCatching { useCase("id", -2) }.exceptionOrNull()

        // Then
        assertTrue(exception is AppError.Validation.QuantityMustPositive)
    }

    @Test
    fun non_existing_product_throws_quantityMustBePositive() = runTest{
        // Given
        val fakeCartRepositoryImpl = FakeCartRepositoryImpl()
        val fakeProductRepositoryImpl = FakeProductRepositoryImpl()

        val useCase = AddToCartUseCase(fakeCartRepositoryImpl, fakeProductRepositoryImpl)

        // When
        val exception: Throwable? = runCatching { useCase("id", 1) }.exceptionOrNull()

        // Then
        assertTrue(exception is AppError.NotFoundError)
    }
    @Test
    fun insufficient_stock_throws_InsufficientStock() = runTest {
        // Given

        val productId = "id-test-1"
        val product: Product = product {
            withId(productId)
            withStock(2)
        }

        val fakeCartRepositoryImpl = FakeCartRepositoryImpl()
        val fakeProductRepositoryImpl = FakeProductRepositoryImpl().apply {
            setProducts(listOf(product))
        }

        val useCase = AddToCartUseCase(fakeCartRepositoryImpl, fakeProductRepositoryImpl)

        // When
        val exception = runCatching {
            useCase(productId, 5)
        }.exceptionOrNull()

        // Then
        assertTrue(exception is AppError.Validation.InsufficientStock)
        assertEquals(2, (exception as AppError.Validation.InsufficientStock).available)
    }

    @Test
    fun successfull_case_adds_item_ti_car() = runTest {

        // Given
        val productId = "id-test-1"
        val product: Product = product {
            withId(productId)
            withStock(10)
        }

        val fakeCartRepositoryImpl = FakeCartRepositoryImpl()
        val fakeProductRepositoryImpl = FakeProductRepositoryImpl().apply {
            setProducts(listOf(product))
        }

        val useCase = AddToCartUseCase(fakeCartRepositoryImpl, fakeProductRepositoryImpl)

        // When
        useCase(productId, 3)

        // Then
        val items = fakeCartRepositoryImpl.getAllCartItems().first()

        assertEquals(productId, items.first().productId)
        assertEquals(1, items.size)
        assertEquals(3, items.first().quantity)
    }

    @Test
    fun default_quantity_adds_one_item() = runTest {

        // Given
        val productId = "id-test-1"
        val product: Product = product {
            withId(productId)
            withStock(10)
        }

        val fakeCartRepositoryImpl = FakeCartRepositoryImpl()
        val fakeProductRepositoryImpl = FakeProductRepositoryImpl().apply {
            setProducts(listOf(product))
        }

        val useCase = AddToCartUseCase(fakeCartRepositoryImpl, fakeProductRepositoryImpl)

        // When
        useCase(productId)

        // Then
        val items = fakeCartRepositoryImpl.getAllCartItems().first()
        assertEquals(1, items.size)
        assertEquals(1, items.first().quantity)
    }

    @Test
    fun zero_quantity_does_not_call_any_repository() = runTest {
        // GIVEN
        val productRepository = mockk<ProductRepository>()
        val cartRepository = mockk<CartRepository>()
        val useCase = AddToCartUseCase(cartRepository, productRepository)

        // WHEN
        runCatching { useCase("id", 0) }.exceptionOrNull()

        // THEN
        coVerify(exactly = 0) { productRepository.getProductById(any()) }
        coVerify(exactly = 0) { cartRepository.getCartItemById(any()) }
        coVerify(exactly = 0) { cartRepository.getCartItemById(any()) }
        coVerify(exactly = 0) { cartRepository.addToCart(any(), any()) }
    }

    @Test
    fun valid_product_calls_addToCart_with_expect_values() = runTest {
        // GIVEN
        val productRepository = mockk<ProductRepository>()
        val cartRepository = mockk<CartRepository>()

        val productId = "custom_id"
        val product = product{
            withId(productId)
            withStock(10)
        }

        coEvery { productRepository.getProductById(productId) } returns flowOf(product)
        coEvery { cartRepository.getCartItemById(productId) } returns null
        coEvery { cartRepository.addToCart(productId, 3) } just Runs

        val useCase = AddToCartUseCase(cartRepository, productRepository)

        // WHEN
        useCase(productId, 3)

        // THEN
        coVerify(exactly = 1) { productRepository.getProductById(productId) }
        coVerify(exactly = 1) { cartRepository.getCartItemById(productId) }
        coVerify { cartRepository.addToCart(productId, 3) }
    }
}