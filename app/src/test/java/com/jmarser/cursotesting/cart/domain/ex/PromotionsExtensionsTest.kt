package com.jmarser.cursotesting.cart.domain.ex

import com.jmarser.cursotesting.core.builders.promotion
import com.jmarser.cursotesting.productlist.domain.model.Promotion
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant
import kotlin.collections.emptyList

class PromotionsExtensionsTest {

    private val now = Instant.parse("2026-05-02T10:00:00Z")

    @Test
    fun `given future promotion when atciveAt then exclude`(){

        // GIVEN
        val futurePromotion = promotion {
            withStartTime(now.plusSeconds(10))
            withEndTime(now.plusSeconds(100))
        }

        val promotions = listOf(futurePromotion)

        // WHEN
        val result = promotions.activeAt(now)

        // THEN
        assertEquals(0, result.size)
    }

    @Test
    fun `given expired promotion when activeAt then exclude`(){
        // GIVEN
        val expiredPromotion = promotion {
            withStartTime(now.minusSeconds(100))
            withEndTime(now.minusSeconds(10))
        }

        val promotions = listOf(expiredPromotion)

        // WHEN
        val result = promotions.activeAt(now)

        // THEN
        assertEquals(0, result.size)
    }

    @Test
    fun `given on going promotion when activeAt then include`(){
        // GIVEN
        val activePromotion = promotion {
            withStartTime(now.minusSeconds(1))
            withEndTime(now.plusSeconds(1))
        }

        val promotions = listOf(activePromotion)

        // WHEN
        val result = promotions.activeAt(now)

        // THEN
        assertEquals(1, result.size)
    }

    @Test
    fun `given exact start time promotion when activeAt then include`(){
        // GIVEN
        val activePromotion = promotion {
            withStartTime(now)
            withEndTime(now.plusSeconds(10))
        }

        val promotions = listOf(activePromotion)

        // WHEN
        val result = promotions.activeAt(now)

        // THEN
        assertEquals(1, result.size)
    }

    @Test
    fun `given exact end time promotion when activeAt then include`(){
        // GIVEN
        val activePromotion = promotion {
            withStartTime(now.minusSeconds(10))
            withEndTime(now)
        }

        val promotions = listOf(activePromotion)

        // WHEN
        val result = promotions.activeAt(now)

        // THEN
        assertEquals(1, result.size)
    }

    @Test
    fun `given emptyList when activeAt then return empty`(){
        // GIVEN
        val promotions = emptyList<Promotion>()

        // WHEN
        val result = promotions.activeAt(now)

        // THEN
        assertEquals(0, result.size)
    }
}