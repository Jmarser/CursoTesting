package com.jmarser.cursotesting.core.mothers

import com.jmarser.cursotesting.core.builders.product

object ProductMother {

    fun bread(stock: Int = 8) = product {
        withId("id-bread")
        withName("Pan")
        withDescription("Calentito")
        withCategory("Bread")
        withPrice(2.50)
        withStock(stock)
    }
    fun milk(stock: Int = 3) = product {
        withId("id-milk")
        withName("Leche")
        withDescription("Lecha de vaca entera")
        withCategory("Milk")
        withPrice(1.50)
        withStock(stock)
    }

    fun coffe(stock: Int = 5) = product {
        withId("id-coffe")
        withName("Café")
        withDescription("Americano")
        withCategory("Drinks")
        withPrice(4.50)
        withStock(stock)
    }

    fun cola(stock: Int = 6) = product {
        withId("id-cola")
        withName("Cola")
        withCategory("Drinks")
        withPrice(2.50)
        withStock(stock)
    }
}