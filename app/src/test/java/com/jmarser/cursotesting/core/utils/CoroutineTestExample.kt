package com.jmarser.cursotesting.core.utils

import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Project: CursoTesting
 * File: CoroutineTestExample.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 17/04/2026
 */

class CoroutineTestExample {

    private fun coroutinesSum(a: Int, b: Int): Int {
        return a + b
    }

    @Test
    fun coroutinesSum_returnsCorrectSum() = runTest {
        val result: Int = coroutinesSum(2, 2)
        assert(result == 4)
    }
}