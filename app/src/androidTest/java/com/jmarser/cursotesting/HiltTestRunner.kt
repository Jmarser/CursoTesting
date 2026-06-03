package com.jmarser.cursotesting

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Project: CursoTesting
 * File: HiltTestRunner.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 30/05/2026
 */

class HiltTestRunner: AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}