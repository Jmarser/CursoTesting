package com.jmarser.cursotesting.cart.domain.usecase

import com.jmarser.cursotesting.cart.data.repository.FakeCartRepositoryImpl
import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.builders.cartItem
import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.domain.model.AppError
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateCartItemUseCaseTest {

    @Test
    fun `given negative quantity when invokes then throws quantity must be positive`() = runTest {
        // GIVEN
        val fakeProductRepository = FakeProductRepositoryImpl()
        val fakeCartRepository = FakeCartRepositoryImpl()

        val useCase = UpdateCartItemUseCase(fakeCartRepository, fakeProductRepository)

        // WHEN
        val exception = runCatching { useCase("id", -1) }.exceptionOrNull()

        // THEN
        assertTrue(exception is AppError.Validation.QuantityMustPositive)
    }

    @Test
    fun `given zero quantity when invokes then removes items from cart`() = runTest {
        // GIVEN
        val productId = "id1"
        val product = product {
            withId(productId)
        }

        val cartItemProduct = cartItem {
            withProductId(productId)
            withQuantity(3)
        }

        val fakeProductRepository =
            FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val fakeCartRepository =
            FakeCartRepositoryImpl().apply { setCartItems(listOf(cartItemProduct)) }

        val useCase = UpdateCartItemUseCase(fakeCartRepository, fakeProductRepository)

        // WHEN
        useCase(productId, 0)

        // THEN
        val items = fakeCartRepository.getAllCartItems().first()
        assertEquals(0, items.size)
    }

    @Test
    fun `given missing product when invoke then throws not found`() = runTest {
        // GIVEN
        val fakeProductRepository = FakeProductRepositoryImpl().apply { setProducts(emptyList()) }
        val fakeCartRepository = FakeCartRepositoryImpl()

        val useCase = UpdateCartItemUseCase(fakeCartRepository, fakeProductRepository)

        // WHEN
        val exception = runCatching { useCase("not", 1) }.exceptionOrNull()

        // THEN
        assertTrue(exception is AppError.NotFoundError)
    }

    @Test
    fun `given requested quantity greater than stock when invoke then throws insufficient stock`() =
        runTest {
            // GIVEN

            val productId = "id1"
            val product = product {
                withId(productId)
            }

            val fakeProductRepository =
                FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
            val fakeCartRepository = FakeCartRepositoryImpl().apply {
                setCartItems(listOf(cartItem {
                    withProductId(productId)
                    withQuantity(1)
                }))
            }

            val useCase = UpdateCartItemUseCase(fakeCartRepository, fakeProductRepository)

            // WHEN
            val exception = runCatching { useCase(productId, 5) }.exceptionOrNull()

            // THEN
            assertTrue(exception is AppError.Validation.InsufficientStock)
        }

    @Test
    fun `given valid product and quantity when invoke then updates cart item`() = runTest {
        // GIVEN

        val productId = "id1"
        val product = product {
            withId(productId)
            withStock(20)
        }

        val fakeProductRepository =
            FakeProductRepositoryImpl().apply { setProducts(listOf(product)) }
        val fakeCartRepository = FakeCartRepositoryImpl().apply {
            setCartItems(listOf(cartItem {
                withProductId(productId)
                withQuantity(1)
            }))
        }

        // WHEN
        val useCase = UpdateCartItemUseCase(fakeCartRepository, fakeProductRepository)
        useCase(productId, 5)

        // THEN
        val items = fakeCartRepository.getAllCartItems().first()
        assertEquals(1, items.size)
        assertEquals(5, items.first().quantity)


    }

}


// GIVEN

// WHEN

// THEN