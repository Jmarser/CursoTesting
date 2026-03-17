package com.jmarser.cursotesting.core.utils

import java.util.Locale
import kotlin.math.roundToInt

/**
 * Project: CursoTesting
 * File: DoubleEx.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 17/03/2026
 */

fun Double.roundTo2Decimals(): Double{
    return (this * 100).roundToInt()/100.0
}

fun Double.toPriceAmount(): String{
    return String.format(Locale.getDefault(), "%.2f €", this)
}