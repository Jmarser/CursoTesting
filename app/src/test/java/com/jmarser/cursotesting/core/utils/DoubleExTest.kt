package com.jmarser.cursotesting.core.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DoubleExTest {


    @Test
    fun roundTo2Decimals_roundsCorrectly(){
        assertEquals(4.66, 4.6578.roundTo2Decimals(), 0.0)
        assertEquals(1.01, 1.0138.roundTo2Decimals(), 0.0)
    }
}