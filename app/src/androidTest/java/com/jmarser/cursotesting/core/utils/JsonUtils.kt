package com.jmarser.cursotesting.core.utils

import androidx.test.platform.app.InstrumentationRegistry

object JsonUtils {
    fun readJson(fileName: String): String{
        val context = InstrumentationRegistry.getInstrumentation().context
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}

fun String.asAsset(): String = JsonUtils.readJson(this)
