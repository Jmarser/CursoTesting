package com.jmarser.cursotesting.productlist.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jmarser.cursotesting.core.builder.cartEntity
import com.jmarser.cursotesting.core.builder.productEntity
import com.jmarser.cursotesting.core.builder.promotionEntity
import com.jmarser.cursotesting.core.data.local.database.MarketDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDataSourceTest {

    private lateinit var database: MarketDatabase
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MarketDatabase::class.java
        ).build()

        localDataSource = LocalDataSource(
            database.productDao(),
            database.promotionDao(),
            database.cartDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun given_products_when_save_and_get_all_then_returns_persisted_product() = runTest {
        val products = listOf(
            productEntity { withId("1") },
            productEntity { withId("2") }
        )

        localDataSource.saveProducts(products)

        val result = localDataSource.getAllProducts().first()

        assertEquals(2, result.size)
    }

    @Test
    fun given_save_products_when_get_productById_then_returns_correct_product() = runTest {
        val products = listOf(
            productEntity { withId("1"); withName("Leche") },
            productEntity { withId("2"); withName("Cerveza") }
        )

        localDataSource.saveProducts(products)

        val result = localDataSource.getProductById("1").first()

        assertNotNull(result)
        assertEquals("Leche", result?.name)
    }

    @Test
    fun given_three_products_when_getProductsById_then_returns_requestd_subset() = runTest {
        val products = listOf(
            productEntity { withId("1"); withName("Leche") },
            productEntity { withId("2"); withName("Cerveza") },
            productEntity { withId("3"); withName("Refresco") }
        )

        localDataSource.saveProducts(products)

        val result = localDataSource.getProductsByIds(setOf("1", "3")).first()

        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Leche" })
        assertTrue(result.any { it.name == "Refresco" })

    }

    @Test
    fun given_promotions_when_save_and_get_all_then_returns_persisted_promotions() = runTest {
        val promotions = listOf(
            promotionEntity { withId("1") },
            promotionEntity { withId("2"); withProductIds("""["p-1"]""") }
        )

        localDataSource.savePromotions(promotions)

        val result = localDataSource.getAllPromotions().first()

        assertEquals(2, result.size)
    }

    @Test
    fun given_cartItem_when_insert_cartItem_then_returns_success_and_item_saved() = runTest {
        val cartItem = cartEntity { withProductId("id1"); withQuantity(2) }

        val result = localDataSource.insertcartItem(cartItem)
        val items = localDataSource.getAllCartItems().first()

        assertTrue(result.isSuccess)
        assertEquals(1, items.size)
        assertTrue(items.first().productId == "id1")
    }

    @Test
    fun given_existing_item_when_update_cartItem_then_returns_success_and_cartItem_updated() = runTest {
        val cartItem = cartEntity { withProductId("id1"); withQuantity(2) }

        localDataSource.insertcartItem(cartItem)

        val cartItem2 = cartEntity { withProductId("id1"); withQuantity(67) }
        val result = localDataSource.updateCartItem(cartItem2)
        val item = localDataSource.getCartItemById("id1")

        assertTrue(result.isSuccess)
        assertNotNull(item)
        assertEquals(67, item?.quantity)
    }

    @Test
    fun given_cartItem_when_delete_cartItem_then_returns_success_and_cart_isEmpty() = runTest {
        val cartItem = cartEntity { withProductId("id1"); withQuantity(2) }

        localDataSource.insertcartItem(cartItem)

        val result = localDataSource.deleteCartItem(cartItem)
        val items = localDataSource.getAllCartItems().first()

        assertTrue(result.isSuccess)
        assertTrue(items.isEmpty())
    }

    @Test
    fun given_multiple_cartItem_when_delete_cartItem_then_returns_success_and_cartItem_list_update() = runTest {
        val cartItem1 = cartEntity { withProductId("id1"); withQuantity(2) }
        val cartItem2 = cartEntity { withProductId("id2"); withQuantity(10) }
        val cartItem3 = cartEntity { withProductId("id3"); withQuantity(5) }

        localDataSource.insertcartItem(cartItem1)
        localDataSource.insertcartItem(cartItem2)
        localDataSource.insertcartItem(cartItem3)

        val result = localDataSource.deleteCartItem(cartItem2)
        val items = localDataSource.getAllCartItems().first()

        assertTrue(result.isSuccess)
        assertEquals(2, items.size)
        assertTrue(items.any { it.productId != "id2" })
    }

    @Test
    fun given_multiple_cartItem_when_clear_cartItem_then_returns_success_and_cart_isEmpty() = runTest {
        val cartItem1 = cartEntity { withProductId("id1"); withQuantity(2) }
        val cartItem2 = cartEntity { withProductId("id2"); withQuantity(10) }
        val cartItem3 = cartEntity { withProductId("id3"); withQuantity(5) }

        localDataSource.insertcartItem(cartItem1)
        localDataSource.insertcartItem(cartItem2)
        localDataSource.insertcartItem(cartItem3)

        val result = localDataSource.clearCart()
        val items = localDataSource.getAllCartItems().first()

        assertTrue(result.isSuccess)
        assertTrue(items.isEmpty())
    }
}