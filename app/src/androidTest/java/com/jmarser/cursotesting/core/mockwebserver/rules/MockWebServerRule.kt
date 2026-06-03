package com.jmarser.cursotesting.core.mockwebserver.rules

import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Project: CursoTesting
 * File: MockWebServerRule.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 30/05/2026
 */

class MockWebServerRule: TestWatcher() {

    val server = MockWebServer()

    override fun starting(description: Description?) {
        super.starting(description)
        server.start()

        MockWebServerUrlHolder.baseUrl = server.url("/").toString()
    }

    override fun finished(description: Description?) {
        server.shutdown()
        super.finished(description)
    }
}