package com.jmarser.cursotesting.productlist.domain.usecase

import com.jmarser.cursotesting.core.builders.product
import com.jmarser.cursotesting.core.builders.promotion
import com.jmarser.cursotesting.productlist.domain.model.ProductPromotion
import com.jmarser.cursotesting.productlist.domain.model.PromotionType
import org.junit.Assert.*
import org.junit.Test

class GetPromotionForProductTest {

    private val useCase = GetPromotionForProduct()

    @Test
    fun given_no_promotions_when_invoke_then_returns_null(){
        // GIVEN
        val product = product()

        // WHEN
        val response = useCase(product, emptyList())

        // THEN
        assertNull(response)
    }

    @Test
    fun given_percent_promotion_when_invoke_then_returns_discounted_price_rounded_to_2_decimals(){

        // GIVEN
        val productId = "product-id"
        val product = product {
            withPrice(10.0)
            withId(productId)
        }

        val promotion = promotion {
            withType(PromotionType.PERCENT)
            withProductIds(listOf(productId))
            withValue(15.0)
        }

        // WHEN
        val response = useCase(product, listOf(promotion))

        // THEN
        assertTrue(response is ProductPromotion.Percent)
        response as ProductPromotion.Percent
        assertEquals(8.50, response.discountedPrice, 0.001)
        assertEquals(15.0, response.percent, 0.001)
    }

    @Test
    fun given_buy_x_pay_and_percent_promotions_when_invoke_then_prioritizes_buy_x_pay_y(){
        // GIVEN
        val productId = "product-id"
        val product = product {
            withPrice(10.0)
            withId(productId)
        }

        val promotionPercent = promotion {
            withType(PromotionType.PERCENT)
            withProductIds(listOf(productId))
            withValue(15.0)
        }

        val promotionBuyXPayY = promotion {
            withType(PromotionType.BUY_X_PAY_Y)
            withProductIds(listOf(productId))
            withBuyQuantity(3)
            withValue(2.0)
        }

        // WHEN
        val response = useCase(product, listOf(promotionPercent, promotionBuyXPayY))

        // THEN
        assertTrue(response is ProductPromotion.BuyXPayY)
        response as ProductPromotion.BuyXPayY
        assertEquals(3, response.buy)
        assertEquals(2, response.pay)
        assertEquals("3x2", response.label)
    }

    @Test
    fun `given multiple percent promotions then invoke then returns highest discount`(){

        // GIVEN
        val productId = "product-id"
        val product = product {
            withPrice(10.0)
            withId(productId)
        }

        val promotionLow = promotion {
            withType(PromotionType.PERCENT)
            withProductIds(listOf(productId))
            withValue(5.0)
        }

        val promotionHigh = promotion {
            withType(PromotionType.PERCENT)
            withProductIds(listOf(productId))
            withValue(50.0)
        }

        // WHEN
        val response = useCase(product, listOf(promotionLow, promotionHigh))

        // THEN
        assertTrue(response is ProductPromotion.Percent)
        response as ProductPromotion.Percent
        assertEquals(50.0, response.percent, 0.001)

    }

    @Test
    fun `given buy x pay y without buy quantity when invoke then returns null`(){

        // GIVEN
        val productId = "product-id"
        val product = product {
            withPrice(10.0)
            withId(productId)
        }

        val promotionLow = promotion {
            withType(PromotionType.PERCENT)
            withProductIds(listOf(productId))
            withValue(5.0)
        }

        val brokenBuyXPromotion = promotion {
            withType(PromotionType.BUY_X_PAY_Y)
            withProductIds(listOf(productId))
            withBuyQuantity(null)
        }


        // WHEN
        val response = useCase(product, listOf(promotionLow, brokenBuyXPromotion))

        // THEN
        assertNull(response)
    }

    @Test
    fun `given percentage over 100 then it should be capped at 100`(){
        val productId = "product-id"
        val product = product {
            withId(productId)
            withPrice(100.0)
        }
        val crazyPromo = promotion {
            withProductIds(listOf(productId))
            withType(PromotionType.PERCENT)
            withValue(150.0)
        }

        val result = useCase(product, listOf(crazyPromo))

        assertTrue(result is ProductPromotion.Percent)
        result as ProductPromotion.Percent
        assertEquals(100.0, result.percent, 0.001)
    }
}