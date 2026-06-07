package com.jmarser.cursotesting.core.mockwebserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * Project: CursoTesting
 * File: ProductErrorDispatcher.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 06/06/2026
 */

class ProductErrorDispatcher: Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when{
            request.path?.contains("products.json") == true -> MockResponse().setResponseCode(500)
            else -> MockResponse().setResponseCode(404)
        }
    }
}