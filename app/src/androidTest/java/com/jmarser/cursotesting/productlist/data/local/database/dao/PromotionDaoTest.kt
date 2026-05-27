package com.jmarser.cursotesting.productlist.data.local.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jmarser.cursotesting.core.builder.promotionEntity
import com.jmarser.cursotesting.core.data.local.database.MarketDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.jetbrains.annotations.TestOnly
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PromotionDaoTest {

    private lateinit var database: MarketDatabase
    private lateinit var dao: PromotionDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MarketDatabase::class.java
        ).build()

        dao = database.promotionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun given_empty_database_when_get_all_promotions_then_emits_empty_list() = runTest {
        val promotions = dao.getAllPromotions().first()

        assertTrue(promotions.isEmpty())
    }

    @Test
    fun given_insert_promotions_when_getAllPromotions_then_returns_row() = runTest {
        val promotionId = "id1"
        val promotion = promotionEntity { withId(promotionId) }

        dao.insertPromotions(listOf(promotion))

        val result = dao.getAllPromotions().first()

        assertNotNull(result)
        assertEquals(promotionId, result.first().id)
    }

    @Test
    fun given_multiple_promotions_when_replaceAll_then_returns_new_promotions() = runTest {
        dao.insertPromotions(listOf(
            promotionEntity { withId("id1") },
            promotionEntity { withId("id2") },
            promotionEntity { withId("id3") }
        ))

        val newPromotions = listOf(
            promotionEntity { withId("id5") },
            promotionEntity { withId("id6") }
        )

        dao.replaceAll(newPromotions)

        val result = dao.getAllPromotions().first()

        assertEquals(2, result.size)
        assertTrue(result.any {it.id == "id5" })
        assertTrue(result.any {it.id == "id6" })
        assertTrue(result.none {it.id == "id1" })
        assertTrue(result.none {it.id == "id2" })
        assertTrue(result.none {it.id == "id3" })
    }

    @Test
    fun given_multiple_promotions_when_clearPromotions_then_returns_empty_list() = runTest {
        dao.insertPromotions(listOf(
            promotionEntity { withId("id1") },
            promotionEntity { withId("id2") },
            promotionEntity { withId("id3") }
        ))

        val result = dao.clearPromotions()

        assertEquals(Unit, result)
    }

}