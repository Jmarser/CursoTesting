package com.jmarser.cursotesting.core.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Project: CursoTesting
 * File: MainDispatcherRule.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 09/05/2026
 */

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule  constructor(
    val scheduler: TestCoroutineScheduler = TestCoroutineScheduler(),
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(scheduler)
): TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}