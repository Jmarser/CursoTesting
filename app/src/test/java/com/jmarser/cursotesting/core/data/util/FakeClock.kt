package com.jmarser.cursotesting.core.data.util

import com.jmarser.cursotesting.core.domain.util.Clock
import java.time.Instant

class FakeClock(private var currentTime: Instant = Instant.now()): Clock  {

    fun setTime(time: Instant){
        currentTime = time
    }

    fun advanceTime(seconds: Long){
        currentTime = currentTime.plusSeconds(seconds)
    }

    override fun now(): Instant = currentTime
}