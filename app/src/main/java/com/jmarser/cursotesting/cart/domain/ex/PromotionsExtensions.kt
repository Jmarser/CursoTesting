package com.jmarser.cursotesting.cart.domain.ex

import com.jmarser.cursotesting.productlist.domain.model.Promotion
import java.time.Instant

/**
 * Project: CursoTesting
 * File: PromotionsExtensions.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 23/03/2026
 */

fun List<Promotion>.activeAt(now: Instant): List<Promotion> = this.filter {
    it.startTime <= now && it.endTime >= now
}