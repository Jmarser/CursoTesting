package com.jmarser.cursotesting.cart.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jmarser.cursotesting.cart.domain.repository.CartRepository
import com.jmarser.cursotesting.core.domain.model.AppError
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CartRepositoryImplTest {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        hilt.inject()
    }

    @Test
    fun given_emptyDatabase_when_getAllCartItems_isCalled_then_emits_emptyList() = runTest {
        val cartItem = cartRepository.getAllCartItems().first()
        assertTrue(cartItem.isEmpty())
    }

    @Test
    fun given_multipleItemsInserted_when_getAllCartItems_isCalled_then_emits_allStoredItems() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        val cartItems = cartRepository.getAllCartItems().first()

        assertTrue(cartItems.isNotEmpty())
        assertEquals(3, cartItems.size)
    }

    @Test
    fun given_existingItemInCart_when_updateQuantity_isCalled_then_modifies_storedQuantity() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        cartRepository.updateQuantity(productId = "item2", quantity = 7)

        val cartItems = cartRepository.getAllCartItems().first()

        assertTrue(cartItems.isNotEmpty())
        assertEquals(3, cartItems.size)
        assertEquals(7, cartItems.find { it.productId == "item2" }?.quantity)
    }

    @Test
    fun given_existingItemInCart_when_addToCart_isCalled_then_increments_itsQuantity() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        val cartItems = cartRepository.getAllCartItems().first()

        assertTrue(cartItems.isNotEmpty())
        assertEquals(3, cartItems.size)
        assertEquals(3, cartItems.find { it.productId == "item2" }?.quantity)

        cartRepository.addToCart(productId = "item2", quantity = 3)

        val newCartItems = cartRepository.getAllCartItems().first()
        assertEquals(3, newCartItems.size)
        assertEquals(6, newCartItems.find { it.productId == "item2" }?.quantity)
    }

    @Test
    fun given_nonExistingItem_when_updateQuantity_isCalled_then_throws_NotFoundError() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        try {
            cartRepository.updateQuantity("item4", 3)
        }catch (e: Exception){
            assertTrue(e is AppError.NotFoundError)
        }
    }

    @Test
    fun given_existingItemInCart_when_deleteCartItem_isCalled_then_removes_itemFromDatabase() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        cartRepository.deleteCartItem("item2")

        val cartItems = cartRepository.getAllCartItems().first()
        assertTrue(cartItems.isNotEmpty())
        assertEquals(2, cartItems.size)
        assertTrue(cartItems.none { it.productId == "item2" })
    }

    @Test
    fun given_exist_cartItems_when_deleteCartItem_no_exists_then_returns_error() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        try {
            cartRepository.deleteCartItem("item4")
        }catch (e: Exception){
            assertTrue(e is AppError.NotFoundError)
        }
    }

    @Test
    fun given_existingItem_when_getCartItemById_isCalled_then_returns_null() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        val cartItem = cartRepository.getCartItemById("item4")

        assertNull(cartItem)
    }

    @Test
    fun given_existingItem_when_getCartItemById_isCalled_then_returns_correctCartItem() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        val cartItem = cartRepository.getCartItemById("item3")

        assertNotNull(cartItem)
        assertTrue(cartItem?.productId == "item3")
    }

    @Test
    fun given_populatedCart_when_clearCart_isCalled_then_deletes_allRowsFromDatabase() = runTest {
        cartRepository.addToCart(productId = "item1", quantity = 1)
        cartRepository.addToCart(productId = "item2", quantity = 3)
        cartRepository.addToCart(productId = "item3", quantity = 5)

        cartRepository.clearCart()

        val cartItems = cartRepository.getAllCartItems().first()

        assertTrue(cartItems.isEmpty())
    }
}