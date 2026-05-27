package com.jmarser.cursotesting.cart.data.local.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.jmarser.cursotesting.core.builder.cartEntity
import com.jmarser.cursotesting.core.builder.productEntity
import com.jmarser.cursotesting.core.data.local.database.MarketDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartDaoTest {

    private lateinit var dataBase: MarketDatabase
    private lateinit var dao: CartDao

    @Before
    fun setUp() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MarketDatabase::class.java
        ).build()

        dao = dataBase.cartDao()
    }

    @After
    fun tearDown() {
        dataBase.close()
    }

    @Test
    fun given_empty_database_when_getAllCartItems_then_emits_empty_list() = runTest {
        val cart = dao.getAllCartItems().first()

        assertTrue(cart.isEmpty())
    }

    @Test
    fun given_insert_cartItem_when_getCartItemById_then_return_row() = runTest {
        val productId = "p1"
        val cartEntity = cartEntity {
            withProductId(productId)
        }

        dao.insertCartItem(cartEntity)

        val cart = dao.getCartItemById(productId)

        assertNotNull(cart)
        assertEquals(productId, cart?.productId)
    }

    @Test
    fun given_empty_database_when_getCartItemById_then_return_null() = runTest {
        val result = dao.getCartItemById("p1")

        assertNull(result)
    }

    @Test
    fun given_existing_cartItem_when_insert_same_cartItem_with_different_quantity_then_update_data() = runTest {
        val productId = "p1"
        val oldProduct = cartEntity {
            withProductId(productId)
            withQuantity(2)
        }
        dao.insertCartItem(oldProduct)

        val newQuantity = cartEntity {
            withProductId(productId)
            withQuantity(5)
        }
        dao.updateCartItem(newQuantity)

        val result = dao.getAllCartItems().first()

        assertTrue(result.size == 1)
        assertEquals(5, result.first().quantity)
    }

    @Test
    fun given_exixting_cartItem_when_insert_same_idCartItem_with_different_quantity_then_update_data() = runTest {
        val productId = "p1"
        val oldProduct = cartEntity {
            withProductId(productId)
            withQuantity(2)
        }
        dao.insertCartItem(oldProduct)

        val newQuantity = cartEntity {
            withProductId(productId)
            withQuantity(5)
        }
        dao.insertCartItem(newQuantity)

        val result = dao.getAllCartItems().first()

        assertTrue(result.size == 1)
        assertEquals(5, result.first().quantity)
    }

    @Test
    fun given_flow_subscribed_when_insert_after_subscribed_then_emits_updateList() = runTest {
        dao.getAllCartItems().test {
            val initial = awaitItem()

            dao.insertCartItem(cartEntity { withProductId("1") })

            val update = awaitItem()

            assertTrue(initial.isEmpty())
            assertEquals(1, update.size)
            assertEquals("1", update.first().productId)
        }
    }

    @Test
    fun given_item_cart_when_delete_item_then_cart_becomes_empty() = runTest {
        val cartItem = cartEntity { withProductId("1"); withQuantity(1) }
        dao.insertCartItem(cartItem)

        val resultGiven = dao.getAllCartItems().first()
        assertTrue(resultGiven.size == 1)
        assertEquals("1", resultGiven.first().productId)

        dao.deleteCartItem(cartItem)

        val resultWhen = dao.getAllCartItems().first()
        assertTrue(resultWhen.isEmpty())
    }
}